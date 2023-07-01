package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentMapper;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoCommentResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemEntityRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserEntityRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemEntityRepository repository;

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    BookingRepository bookingRepository;


    private final UserService userService;

    private final CommentMapper commentMapper;

    private final ItemMapper itemMapper;

    public ItemServiceImpl(UserService userService, CommentMapper commentMapper, ItemMapper itemMapper) {
        this.userService = userService;
        this.commentMapper = commentMapper;
        this.itemMapper = itemMapper;
    }

    public Item create(Item item, long id) {
        if (userService.getUserId(id) != null) {
            item.setOwner(userService.getUserId(id));
            item.setRequestId(item.getRequestId());
            repository.save(item);
            return item;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public Comment postComment(CommentDto commentDto, long itemId, long userId) {
        commentDto.setCreated(LocalDateTime.now());
        List<Booking> booking = bookingRepository.findAllByBookerIdAndItemIdAndStatusNotAndStartBefore(userId, itemId,
                BookingStatus.REJECTED, LocalDateTime.now());
        if (booking.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user = userEntityRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Item item = repository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Comment comment = commentMapper.toComment(commentDto, item, user);
        return commentRepository.save(comment);
    }

    @Transactional
    public Item update(long idUser, long itemId, Item item) {
        Item item1 = repository.getReferenceById(itemId);
        if (item1.getOwner().getId() == idUser) {
            if (item.getName() != null) item1.setName(item.getName());
            if (item.getDescription() != null) item1.setDescription(item.getDescription());
            if (item.getAvailable() != null) item1.setAvailable(item.getAvailable());
            repository.save(item1);
            return item1;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
    }

    public ItemDtoCommentResponse get(long id, long userId) {
        Item item = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Booking> booking = bookingRepository.findAllByItemIdAndOwnerId(id, userId);
        List<Comment> comment = commentRepository.findAllByItemId(id);

        return itemMapper.toItemResponseDto(item, booking, comment);
    }

    @Transactional
    public List<ItemDtoCommentResponse> getItemOwner(long id) {
        User user = userService.getUserId(id);
        List<Item> itemList = repository.findAllByOwnerOrderById(user);
        List<Long> itemIdList = itemList.stream().map(Item::getId).collect(Collectors.toList());

        List<Booking> booking = bookingRepository.findAllByOwnerIdAndItemIn(id, itemIdList);
        List<Comment> comment = commentRepository.findAllByAndAuthorName(user.getName());

        return itemList.stream()
                .map(item -> itemMapper.toItemResponseDto(item, booking, comment)).collect(Collectors.toList());
    }

    public List<Item> searchItem(String text) {
        ArrayList<Item> items = new ArrayList<>();
        if (!text.isEmpty()) {
            for (Item item : repository.search(text)) {
                if (item.getAvailable()) items.add(item);
            }
            return items;
        }
        return items;
    }
}

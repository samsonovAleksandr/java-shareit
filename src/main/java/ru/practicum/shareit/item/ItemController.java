package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoCommentResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")

public class ItemController {

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    private final CommentMapper commentMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper, CommentMapper commentMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.commentMapper = commentMapper;
    }

    @PostMapping
    public ItemDto postItem(@RequestBody @Valid Item item,
                            @RequestHeader("X-Sharer-User-Id") int id) {
        return itemMapper.toItemDto(itemService.postItem(item, id));
    }

    @PostMapping("{itemId}/comment")
    public Comment comments(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                            @PathVariable("itemId") long itemId,
                            @Valid @RequestBody CommentDto comment) {
        return itemService.postComment(comment, itemId, userId);
    }

    @PatchMapping("{itemId}")
    public ItemDto patchItem(@RequestHeader("X-Sharer-User-Id") int idUser,
                             @PathVariable int itemId,
                             @RequestBody Item item) {
        return itemMapper.toItemDto(itemService.patchItem(idUser, itemId, item));
    }

    @GetMapping("{itemId}")
    public ItemDtoCommentResponse getItem(@PathVariable long itemId,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoCommentResponse> getItemOwner(@RequestHeader("X-Sharer-User-Id") int id) {
        return itemService.getItemOwner(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String text) {
        return itemMapper.itemDtoList(itemService.searchItem(text));
    }
}

package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemEntityRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserEntityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private UserEntityRepository userEntityRepository;

    private RequestRepository repository;

    private RequestMapper requestMapper;

    private ItemEntityRepository itemRepository;

    public RequestServiceImpl(UserEntityRepository userEntityRepository, RequestRepository repository, RequestMapper requestMapper, ItemEntityRepository itemRepository) {
        this.userEntityRepository = userEntityRepository;
        this.repository = repository;
        this.requestMapper = requestMapper;
        this.itemRepository = itemRepository;
    }


    @Override
    public ItemRequestResponseDto create(long userId, ItemRequestDto itemRequestDto) {
        User user = userEntityRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ItemRequest itemRequest = requestMapper.toItemRequest(itemRequestDto, user);
        List<RequestItemDto> requestItemDtoList = new ArrayList<>();
        return requestMapper.toItemRequestResponseDto(repository.save(itemRequest), requestItemDtoList);
    }

    @Override
    public List<ItemRequestResponseDto> get(long userId, int from, int size) {
        User user = userEntityRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<ItemRequest> itemRequest = repository.findAllByRequestorIsNotOrderByCreatedDesc(user.getId(), PageRequest.of(from, size));
        List<Item> items = itemRepository.findAllByRequestIdIn(
                itemRequest.stream()
                        .map(ItemRequest::getId)
                        .collect(Collectors.toList()));
        List<RequestItemDto> requestItemDtos = requestMapper.toRequestItemDtoList(items);
        return requestMapper.toiItemRequestResponseDtoList(itemRequest, requestItemDtos);
    }

    @Override
    public List<ItemRequestResponseDto> getRequest(long userId) {
        User user = userEntityRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<ItemRequest> itemRequest = repository.findAllByRequestorOrderByCreatedDesc(user.getId());
        List<Item> items = itemRepository.findAllByRequestIdIn(
                itemRequest.stream()
                        .map(ItemRequest::getId)
                        .collect(Collectors.toList()));
        List<RequestItemDto> requestItemDtos = requestMapper.toRequestItemDtoList(items);
        return requestMapper.toiItemRequestResponseDtoList(itemRequest, requestItemDtos);
    }

    @Override
    public ItemRequestResponseDto getRequestId(long requestId, long userId) {
        User user = userEntityRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ItemRequest itemRequest = repository.findById(requestId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Item> items = itemRepository.findAllByRequestIdIn(List.of(itemRequest.getId()));
        List<RequestItemDto> requestItemDtos = requestMapper.toRequestItemDtoList(items);
        return requestMapper.toItemRequestResponseDto(itemRequest, requestItemDtos);
    }


}

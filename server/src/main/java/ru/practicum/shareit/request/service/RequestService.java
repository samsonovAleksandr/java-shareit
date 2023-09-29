package ru.practicum.shareit.request.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface RequestService {

    ItemRequestResponseDto create(long userId, ItemDto itemRequestDto);

    List<ItemRequestResponseDto> get(long userId, int from, int size);

    List<ItemRequestResponseDto> getRequest(long userId);

    ItemRequestResponseDto getRequestId(long requestId, long userId);
}

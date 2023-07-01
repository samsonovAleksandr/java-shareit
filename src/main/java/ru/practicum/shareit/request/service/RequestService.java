package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface RequestService {

    ItemRequestResponseDto create(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestResponseDto> get(long userId, int from, int size);

    List<ItemRequestResponseDto> getRequest(long userId);

    ItemRequestResponseDto getRequestId(long requestId, long userId);
}

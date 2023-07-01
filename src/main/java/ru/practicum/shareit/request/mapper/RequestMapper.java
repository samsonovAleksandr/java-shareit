package ru.practicum.shareit.request.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestMapper {

    public ItemRequest toItemRequest(ItemRequestDto dto, User user) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .created(LocalDateTime.now())
                .requestor(user.getId())
                .build();
    }

    public ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest, List<RequestItemDto> items) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }

    public List<ItemRequestResponseDto> toiItemRequestResponseDtoList(List<ItemRequest> itemRequests, List<RequestItemDto> requestItemDtos) {
        List<ItemRequestResponseDto> list = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            list.add(toItemRequestResponseDto(itemRequest, requestItemDtos));
        }
        return list;
    }

    public RequestItemDto toRequestItemDto(Item items) {
        return RequestItemDto.builder()
                .id(items.getId())
                .name(items.getName())
                .description(items.getDescription())
                .available(items.getAvailable())
                .requestId(items.getRequestId())
                .build();
    }

    public List<RequestItemDto> toRequestItemDtoList(List<Item> items) {
        List<RequestItemDto> requestItemDtoList = new ArrayList<>();
        for (Item item : items) {
            requestItemDtoList.add(toRequestItemDto(item));
        }
        return requestItemDtoList;
    }
}
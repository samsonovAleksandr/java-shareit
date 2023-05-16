package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDto {

    String name;

    String description;

    boolean isAvailable;

    long request;

    public ItemDto(String name, String description, boolean available, Long request) {

    }
}

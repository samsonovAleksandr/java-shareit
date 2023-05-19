package ru.practicum.shareit.item.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class Item {

    int id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    Boolean available;

    User owner;

    ItemRequest request;

}

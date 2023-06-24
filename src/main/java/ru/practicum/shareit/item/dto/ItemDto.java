package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {

    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    private User owner;
    @NotNull
    private Boolean available;

    private long request;

}

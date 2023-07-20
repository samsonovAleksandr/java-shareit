package ru.practicum.shareit.request.dto;

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

    private String name;
    @NotBlank
    private String description;

    private User owner;
    private Boolean available;

    private Long requestId;

    public ItemDto(String description) {
        this.description = description;
    }
}

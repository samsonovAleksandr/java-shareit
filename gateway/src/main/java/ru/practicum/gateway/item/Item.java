package ru.practicum.gateway.item;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.gateway.user.dto.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Item {
    long id;
    @NotBlank String name;
    @NotBlank String description;
    @NotNull Boolean available;
    User owner;
    Long requestId;

}

package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class User {

    private int id;
    @NotBlank
    private String name;
    @Email(message = "Email is not valid")
    @NotNull
    @NotEmpty(message = "Email cannot be empty")
    private String email;

}

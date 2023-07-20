package ru.practicum.gateway.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {

    private long id;
    @NotBlank
    private String name;
    @Email(message = "Email is not valid")
    @NotNull
    @NotEmpty(message = "Email cannot be empty")
    private String email;

}

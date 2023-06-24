package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDtoCommentResponse {

    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private User owner;

    @NotNull
    private Boolean available;

    private ItemRequest request;

    private BookingDtoItem lastBooking;

    private BookingDtoItem nextBooking;

    private List<Comment> comments;
}

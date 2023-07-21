package ru.practicum.shareit.booking.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.dto.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSerialize
public class BookingDto {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private User booker;

    private Item item;

    private BookingStatus status;


}

package ru.practicum.gateway.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoItem {
    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long bookerId;

    private BookingStatus status;
}

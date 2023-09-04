package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {

    BookingDto create(BookingRequestDto booking, long idUser);

    BookingDto update(long userId, long bookingId, boolean approved);

    BookingDto get(long userId, long bookingId);

    List<BookingDto> getBookingByUserId(State state, long userId, String typeUser, int from, int size);
}

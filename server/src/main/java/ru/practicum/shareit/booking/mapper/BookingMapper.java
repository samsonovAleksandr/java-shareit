package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingMapper {

    public Booking toBooking(BookingRequestDto booking, Item item, User user) {
        return Booking.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(user)
                .item(item)
                .status(BookingStatus.WAITING)
                .build();
    }

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public List<BookingDto> toListBooking(List<Booking> bookings) {
        ArrayList<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking book : bookings) {
            bookingDtoList.add(toBookingDto(book));
        }
        return bookingDtoList;
    }

    public static BookingDtoItem toBookingDtoItem(Booking booking) {
        return BookingDtoItem.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBooker().getId())
                .status(booking.getStatus())
                .build();
    }
}

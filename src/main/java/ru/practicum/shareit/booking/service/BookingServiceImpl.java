package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.StateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemEntityRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserEntityRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository repository;

    @Autowired
    UserEntityRepository userRepository;

    @Autowired
    ItemEntityRepository itemRepository;

    @Autowired
    BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto postBooking(BookingRequestDto bookingDto, long idUser) {
        if (!(itemRepository.existsById(bookingDto.getItemId())))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (bookingDto.getStart().equals(bookingDto.getEnd()) || bookingDto.getEnd().isBefore(bookingDto.getStart()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);


        if (item.getOwner().getId() == idUser) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if (!userRepository.existsById(idUser)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if (item.getAvailable()) {
            User booker = userRepository.getReferenceById(idUser);
            Booking booking = bookingMapper.toBooking(bookingDto, item, booker);
            return bookingMapper.toBookingDto(repository.save(booking));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public BookingDto patchBooking(long userId, long bookingId, boolean approved) {
        Booking booking = repository.findBookingOwner(bookingId, userId);

        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (approved) {
            if (booking.getStatus().equals(BookingStatus.APPROVED)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingMapper.toBookingDto(repository.save(booking));

    }

    @Override
    public BookingDto getBooking(long userId, long bookingId) {
        Booking booking = repository.findBookingOwnerOrBooker(bookingId, userId);
        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingByUserId(State state, long userId, String typeUser) {


        List<Booking> bookings;
        LocalDateTime time = LocalDateTime.now();
        boolean isOwner = typeUser.equals("owner");


        switch (state) {
            case ALL:
                if (isOwner) {
                    bookings = repository.findAllByOwnerIdOrderByStartDesc(userId);
                } else {
                    bookings = repository.findAllByBookerIdOrderByStartDesc(userId);
                }
                break;
            case FUTURE:
                if (isOwner) {
                    bookings = repository.findAllByOwnerIdAndStartAfterOrderByStartDesc(userId, time);
                } else {
                    bookings = repository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, time);
                }
                break;
            case WAITING:
                if (isOwner) {
                    bookings = repository.findAllByOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                } else {
                    bookings = repository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                }
                break;
            case CURRENT:
                if (isOwner) {
                    bookings = repository.findAllByOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, time, time);
                } else {
                    bookings = repository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, time, time);
                }
                break;
            case PAST:
                if (isOwner) {
                    bookings = repository.findAllByOwnerIdAndEndBeforeOrderByStartDesc(userId, time);
                } else {
                    bookings = repository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, time);
                }
                break;
            case REJECTED:
                if (isOwner) {
                    bookings = repository.findAllByOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                } else {
                    bookings = repository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                }
                break;
            default:
                throw new StateException("UNSUPPORTED_STATUS");
        }

        if (bookings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return bookingMapper.toListBooking(bookings);
    }

}

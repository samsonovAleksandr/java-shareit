package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.ErrorResponse;
import ru.practicum.shareit.exeption.StateException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@ResponseBody
public class BookingController {


    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto postBooking(@RequestBody BookingRequestDto bookingDto,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patchBooking(@PathVariable long bookingId,
                                   @RequestParam boolean approved,
                                   @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingService.update(idUser, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable("bookingId") long bookingId,
                                 @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingService.get(idUser, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingByUserId(@RequestParam(value = "state", defaultValue = "ALL") State state,
                                               @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(value = "size", defaultValue = "20") @Positive int size,
                                               @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingService.getBookingByUserId(state, idUser, "booker", from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingOwner(@RequestParam(value = "state", defaultValue = "ALL", required = false) State state,
                                            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(value = "size", defaultValue = "20") @Positive int size,
                                            @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingService.getBookingByUserId(state, idUser, "owner", from, size);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final StateException e) {
        return new ErrorResponse(
                "Unknown state: UNSUPPORTED_STATUS", e.getMessage()
        );
    }
}

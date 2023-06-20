package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.ErrorResponse;
import ru.practicum.shareit.exeption.StateException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
@ResponseBody
public class BookingController {


    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto postBooking(@RequestBody @Valid BookingRequestDto booking,
                                  @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingService.postBooking(booking, idUser);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patchBooking(@PathVariable long bookingId,
                                   @RequestParam boolean approved,
                                   @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingService.patchBooking(idUser, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable("bookingId") long bookingId,
                                 @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingService.getBooking(idUser, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingByUserId(@RequestParam(value = "state", defaultValue = "ALL", required = false) State state,
                                               @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingService.getBookingByUserId(state, idUser, "booker");
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingOwner(@RequestParam(value = "state", defaultValue = "ALL", required = false) State state,
                                            @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingService.getBookingByUserId(state, idUser, "owner");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final StateException e) {
        return new ErrorResponse(
                "Unknown state: UNSUPPORTED_STATUS", e.getMessage()
        );
    }
}

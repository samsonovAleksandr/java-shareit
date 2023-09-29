package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@Validated
@ResponseBody
@AllArgsConstructor
public class BookingController {


    private BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> postBooking(@RequestBody @Valid BookingRequestDto bookingDto,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingClient.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> patchBooking(@PathVariable long bookingId,
                                               @RequestParam boolean approved,
                                               @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingClient.patch(idUser, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable("bookingId") long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingClient.getById(idUser, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingByUserId(@RequestParam(value = "state", defaultValue = "ALL") State state,
                                                     @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                                     @RequestParam(value = "size", defaultValue = "20") @Positive int size,
                                                     @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingClient.getBookings(idUser, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingOwner(@RequestParam(value = "state", defaultValue = "ALL", required = false) State state,
                                                  @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(value = "size", defaultValue = "20") @Positive int size,
                                                  @RequestHeader("X-Sharer-User-Id") long idUser) {
        return bookingClient.getBookingsOwner(idUser, state, from, size);
    }
}

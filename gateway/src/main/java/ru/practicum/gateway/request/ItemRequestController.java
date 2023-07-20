package ru.practicum.gateway.request;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.request.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@ResponseBody
@AllArgsConstructor
@Validated
public class ItemRequestController {
    @Autowired
    private ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> postRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody @Valid ItemDto itemRequestDto) {
        return itemRequestClient.create(userId, itemRequestDto);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestAll(@RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(value = "size", defaultValue = "20") @Positive int size,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getRequestAll(userId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getRequest(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestId(@PathVariable @Positive long requestId,
                                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getRequestById(userId, requestId);
    }

}

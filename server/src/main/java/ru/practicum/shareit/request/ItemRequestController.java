package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@ResponseBody
public class ItemRequestController {

    private final RequestService requestService;

    public ItemRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ItemRequestResponseDto postRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody ItemDto itemRequestDto) {
        return requestService.create(userId, itemRequestDto);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getRequestAll(@RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                                      @RequestParam(value = "size", defaultValue = "20") @Positive int size,
                                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.get(userId, from, size);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequest(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getRequestId(@PathVariable @Positive long requestId,
                                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequestId(requestId, userId);
    }

}

package ru.practicum.gateway.item;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.item.dto.CommentDto;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {
    @Autowired
    private ItemClient itemClient;


    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ru.practicum.gateway.item.dto.ItemDto item,
                                         @RequestHeader("X-Sharer-User-Id") int id) {
        return itemClient.create(item, id);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> comments(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                           @PathVariable("itemId") long itemId,
                                           @RequestBody @Valid CommentDto comment) {
        return itemClient.createComment(itemId, userId, comment);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> patchItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                            @PathVariable int itemId,
                                            @RequestBody Map<Object, Object> fields) {
        return itemClient.update(itemId, userId, fields);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemOwner(@RequestHeader("X-Sharer-User-Id") int id) {
        return itemClient.getAll(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam("text") String text) {
        return itemClient.search(text);
    }
}

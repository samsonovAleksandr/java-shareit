package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoCommentResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemMapper itemMapper;


    @PostMapping
    public ItemDto postItem(@RequestBody @Valid Item item,
                            @RequestHeader("X-Sharer-User-Id") int id) {
        return itemMapper.toItemDto(itemService.create(item, id));
    }

    @PostMapping("{itemId}/comment")
    public Comment comments(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                            @PathVariable("itemId") long itemId,
                            @Valid @RequestBody CommentDto comment) {
        return itemService.postComment(comment, itemId, userId);
    }

    @PatchMapping("{itemId}")
    public ItemDto patchItem(@RequestHeader("X-Sharer-User-Id") int userId,
                             @PathVariable int itemId,
                             @RequestBody Item item) {
        return itemMapper.toItemDto(itemService.update(userId, itemId, item));
    }

    @GetMapping("{itemId}")
    public ItemDtoCommentResponse getItem(@PathVariable long itemId,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.get(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoCommentResponse> getItemOwner(@RequestHeader("X-Sharer-User-Id") int id) {
        return itemService.getItemOwner(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String text) {
        return itemMapper.itemDtoList(itemService.searchItem(text));
    }
}

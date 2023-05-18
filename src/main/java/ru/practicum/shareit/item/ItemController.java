package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")

public class ItemController {

    ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto postItem(@RequestBody @Valid Item item,
                            @RequestHeader("X-Sharer-User-Id") int id) {
        return itemService.postItem(item, id);
    }

    @PatchMapping("{itemId}")
    public Item patchItem(@RequestHeader("X-Sharer-User-Id") int idUser,
                          @PathVariable int itemId,
                          @RequestBody Item item) {
        return itemService.patchItem(idUser, itemId, item);
    }

    @GetMapping("{itemId}")
    public Item getItem(@PathVariable int itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<Item> getItemOwner(@RequestHeader("X-Sharer-User-Id") int id) {
        return itemService.getItemOwner(id);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam("text") String text) {
        return itemService.searchItem(text);
    }
}

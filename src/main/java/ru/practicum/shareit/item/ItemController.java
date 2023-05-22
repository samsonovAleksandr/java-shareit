package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")

public class ItemController {

   private final ItemService itemService;

   private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ItemDto postItem(@RequestBody @Valid Item item,
                            @RequestHeader("X-Sharer-User-Id") int id) {
        return itemMapper.toItemDto(itemService.postItem(item, id));
    }

    @PatchMapping("{itemId}")
    public ItemDto patchItem(@RequestHeader("X-Sharer-User-Id") int idUser,
                          @PathVariable int itemId,
                          @RequestBody Item item) {
        return itemMapper.toItemDto(itemService.patchItem(idUser, itemId, item));
    }

    @GetMapping("{itemId}")
    public ItemDto getItem(@PathVariable int itemId) {
        return itemMapper.toItemDto(itemService.getItem(itemId));
    }

    @GetMapping
    public List<ItemDto> getItemOwner(@RequestHeader("X-Sharer-User-Id") int id) {
        return itemMapper.itemDtoList(itemService.getItemOwner(id));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String text) {
        return itemMapper.itemDtoList(itemService.searchItem(text));
    }
}

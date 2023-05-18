package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto postItem(Item item, int id);

    Item patchItem(int idUser, int itemId, Item item);

    Item getItem(int id);

    List<Item> getItemOwner(int id);

    List<Item> searchItem(String text);
}

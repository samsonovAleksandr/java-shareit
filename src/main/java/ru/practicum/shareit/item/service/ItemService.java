package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item postItem(Item item, long id);

    Item patchItem(long idUser, long itemId, Item item);

    Item getItem(long id);

    List<Item> getItemOwner(long id);

    List<Item> searchItem(String text);
}

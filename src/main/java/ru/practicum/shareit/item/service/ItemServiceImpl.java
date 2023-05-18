package ru.practicum.shareit.item.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    private final UserServiceImpl userServiceImpl;

    private final ItemMapper itemMapper;

    public ItemServiceImpl(UserServiceImpl userServiceImpl, ItemMapper itemMapper) {
        this.userServiceImpl = userServiceImpl;
        this.itemMapper = itemMapper;
    }

    private int i = 0;

    private final HashMap<Integer, Item> items = new HashMap<Integer, Item>();

    private int newId() {
        return ++i;
    }

    public ItemDto postItem(Item item, int id) {
        if (userServiceImpl.users.get(id) != null) {
            item.setOwner(userServiceImpl.users.get(id));
            item.setId(newId());
            items.put(item.getId(), item);
            return itemMapper.toItemDto(items.get(item.getId()));
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
    }

    public Item patchItem(int idUser, int itemId, Item item) {
        Item item1 = items.get(itemId);
        if (item1.getOwner().getId() == idUser) {
            if (item.getName() != null) item1.setName(item.getName());
            if (item.getDescription() != null) item1.setDescription(item.getDescription());
            if (item.getAvailable() != null) item1.setAvailable(item.getAvailable());
            return item1;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
    }

    public Item getItem(int id) {
        return items.get(id);
    }

    public List<Item> getItemOwner(int id) {
        ArrayList<Item> itemArrayList = new ArrayList<>();
        for (Item it : items.values()) {
            if (it.getOwner().getId() == id) {
                itemArrayList.add(it);
            }
        }
        return itemArrayList;
    }

    public List<Item> searchItem(String text) {
        List<Item> itemArrayList = new ArrayList<>();
        if (text.isEmpty()) return itemArrayList;
        for (Item it : items.values()) {
            if (it.getAvailable()) {
                boolean b1 = it.getName().toLowerCase().contains(text.toLowerCase());
                boolean b2 = it.getDescription().toLowerCase().contains(text.toLowerCase());
                if (b1 || b2) {
                    itemArrayList.add(it);
                }
            }
        }
        return itemArrayList;
    }
}

package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemEntityRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemEntityRepository repository;
    private final UserService userService;

    public ItemServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public Item postItem(Item item, long id) {
        if (userService.getUserId(id) != null) {
            item.setOwner(userService.getUserId(id));
            repository.save(item);
            return item;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
    }

    public Item patchItem(long idUser, long itemId, Item item) {
        Item item1 = repository.getReferenceById(itemId);
        if (item1.getOwner().getId() == idUser) {
            if (item.getName() != null) item1.setName(item.getName());
            if (item.getDescription() != null) item1.setDescription(item.getDescription());
            if (item.getAvailable() != null) item1.setAvailable(item.getAvailable());
            repository.save(item1);
            return item1;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
    }

    public Item getItem(long id) {
        if (repository.existsById(id)) {
            return repository.getReferenceById(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND);
        }
    }

    public List<Item> getItemOwner(long id) {
        return repository.searchAllItemOwner(id);
    }

    public List<Item> searchItem(String text) {
        ArrayList<Item> items = new ArrayList<>();
        if (!text.isEmpty()) {
            for (Item item : repository.search(text)) {
                if (item.getAvailable()) items.add(item);
            }
            return items;
        }
        return items;
    }
}

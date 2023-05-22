package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public List<ItemDto> itemDtoList(List<Item> itemList) {
        ArrayList<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : itemList) {
            itemDtos.add(toItemDto(item));
        }
        return itemDtos;
    }

}

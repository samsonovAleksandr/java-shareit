package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoCommentResponse;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

public interface ItemService {

    Item create(Item item, long id);

    Item update(long idUser, long itemId, Item item);

    ItemDtoCommentResponse get(long id, long userId);

    List<ItemDtoCommentResponse> getItemOwner(long id);

    List<Item> searchItem(String text);

    Comment postComment(@Valid CommentDto commentDto, long itemId, long userId);
}

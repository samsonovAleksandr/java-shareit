package ru.practicum.shareit.item.comments;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Service
@AllArgsConstructor
@Component
public class CommentMapper {

    public Comment toComment(CommentDto commentDto, Item item, User user) {
        return Comment.builder()
                .text(commentDto.getText())
                .authorName(user.getName())
                .item(item)
                .created(commentDto.getCreated())
                .build();
    }
}

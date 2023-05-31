package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@Data
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
    @Column(name = "text")
   private String text;
    @Column(name = "item_id")
   private Long itemId;
    @Column(name = "author_id")
   private Long authorId;

}

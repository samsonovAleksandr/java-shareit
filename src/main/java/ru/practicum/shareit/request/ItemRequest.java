package ru.practicum.shareit.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "description")
    String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id", referencedColumnName = "id")
    User requestor;
    @Column(name = "created")
    LocalDateTime created;
}

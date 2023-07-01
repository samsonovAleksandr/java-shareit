package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemEntityRepository extends JpaRepository<Item, Long> {
    @Query(" SELECT i FROM Item i " +
            "WHERE owner_id = ?1")
    List<Item> searchAllItemOwner(long id);

    List<Item> findAllByOwnerOrderById(User user);

    @Query(" SELECT i FROM Item i " +
            "WHERE upper(i.name) LIKE upper(concat('%', ?1, '%')) " +
            " OR upper(i.description) LIKE upper(concat('%', ?1, '%'))")
    List<Item> search(String text);

    List<Item> findAllByRequestIdIn(List<Long> requestsId);

}

package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestorOrderByCreatedDesc(long userId);

    List<ItemRequest> findAllByRequestorIsNotOrderByCreatedDesc(long requestor, Pageable page);
}

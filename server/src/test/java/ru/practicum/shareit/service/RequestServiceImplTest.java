package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemEntityRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserEntityRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RequestServiceImplTest {

    private final RequestService requestService;
    private final UserEntityRepository userRepository;
    private final ItemEntityRepository itemRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    private User user;
    private User user1;
    private Item item;


    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Sima")
                .email("sima@sima.com")
                .build();
        user1 = User.builder()
                .name("Sima111")
                .email("sima11@sima11.com")
                .build();
        item = Item.builder()
                .name("Drill")
                .description("Mini Drill")
                .owner(user)
                .available(true)
                .build();
    }

    @Test
    @Transactional
    void create() {
        itemRepository.save(item);
        userRepository.save(user);
        userRepository.save(user1);
        ItemDto itemRequestDto = ItemDto.builder()
                .description("Drill")
                .build();
        ItemRequestResponseDto itemRequestResponseDto = requestService.create(1, itemRequestDto);

        assertEquals(itemRequestResponseDto.getDescription(), itemRequestDto.getDescription());

    }

    @Test
    @Transactional
    void createUserNotFound() {
        itemRepository.save(item);
        userRepository.save(user);
        userRepository.save(user1);
        ItemDto itemRequestDto = ItemDto.builder()
                .description("Drill")
                .build();

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> requestService.create(99, itemRequestDto));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

    }

    @Test
    @Transactional
    void get() {
        itemRepository.save(item);
        userRepository.save(user);
        userRepository.save(user1);
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Drill")
                .requestor(1)
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(itemRequest);
        ItemDto itemRequestDto = ItemDto.builder()
                .description("Drill")
                .build();
        requestService.create(1, itemRequestDto);
        List<ItemRequestResponseDto> itemRequestResponseDto = requestService.get(2, 1, 5);

        assertEquals(itemRequestResponseDto.size(), 0);
    }

    @Test
    @Transactional
    void getUserNotFound() {
        itemRepository.save(item);
        userRepository.save(user);
        userRepository.save(user1);
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Drill")
                .requestor(1)
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(itemRequest);
        ItemDto itemRequestDto = ItemDto.builder()
                .description("Drill")
                .build();
        requestService.create(1, itemRequestDto);
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> requestService.get(99, 1, 5));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Transactional
    void getRequest() {
        itemRepository.save(item);
        userRepository.save(user);
        userRepository.save(user1);
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Drill")
                .requestor(1)
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(itemRequest);
        ItemDto itemRequestDto = ItemDto.builder()
                .description("Drill")
                .build();
        List<ItemRequestResponseDto> itemRequestResponseDto = requestService.getRequest(1);

        assertEquals(itemRequestResponseDto.size(), 1);
    }

    @Test
    @Transactional
    void getRequestUserNotFound() {
        itemRepository.save(item);
        userRepository.save(user);
        userRepository.save(user1);
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Drill")
                .requestor(1)
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(itemRequest);
        ItemDto itemRequestDto = ItemDto.builder()
                .description("Drill")
                .build();
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> requestService.getRequest(99));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Transactional
    void getRequestId() {
        itemRepository.save(item);
        userRepository.save(user);
        userRepository.save(user1);
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Drill")
                .requestor(1)
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(itemRequest);
        ItemDto itemRequestDto = ItemDto.builder()
                .description("Drill")
                .build();
        requestService.create(1, itemRequestDto);
        ItemRequestResponseDto itemRequestResponseDto = requestService.getRequestId(1, 1);
        assertEquals(itemRequestResponseDto.getDescription(), itemRequestDto.getDescription());
    }

    @Test
    @Transactional
    void getRequestIdUserNotFound() {
        itemRepository.save(item);
        userRepository.save(user);
        userRepository.save(user1);
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Drill")
                .requestor(1)
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(itemRequest);
        ItemDto itemRequestDto = ItemDto.builder()
                .description("Drill")
                .build();
        requestService.create(1, itemRequestDto);
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> requestService.getRequestId(1, 99));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Transactional
    void getRequestIdRequestNotFound() {
        itemRepository.save(item);
        userRepository.save(user);
        userRepository.save(user1);
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Drill")
                .requestor(1)
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(itemRequest);
        ItemDto itemRequestDto = ItemDto.builder()
                .description("Drill")
                .build();
        requestService.create(1, itemRequestDto);
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> requestService.getRequestId(99, 1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Transactional
    void getRequestIdMapper() {
        item.setRequestId(1L);
        itemRepository.save(item);
        userRepository.save(user);
        userRepository.save(user1);
        ItemRequest itemRequest = ItemRequest.builder()
                .description("Drill")
                .requestor(1)
                .created(LocalDateTime.now())
                .build();
        requestRepository.save(itemRequest);
        RequestItemDto requestItemDto = requestMapper.toRequestItemDto(item);
        assertEquals(requestItemDto.getDescription(), "Mini Drill");
    }
}
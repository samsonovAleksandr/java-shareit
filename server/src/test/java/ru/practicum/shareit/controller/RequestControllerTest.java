package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private User user;
    private User userTwo;
    private ItemDto item;
    private ItemRequestResponseDto itemResponse;
    @MockBean
    private RequestServiceImpl requestService;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User(1, "user", "user@user.com");

        userTwo = new User(2, "userNew", "userNew@userNew.com");

        item = new ItemDto("Brush for footwear");

        itemResponse = ItemRequestResponseDto.builder()
                .id(1L)
                .description(item.getDescription())
                .items(new ArrayList<>())
                .created(LocalDateTime.now())
                .build();
    }


    @Test
    public void shouldItemRequestWithEmptyDescription() throws Exception {
        ItemDto item = ItemDto.builder()
                .description(null)
                .build();
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk());
    }


    @Test
    public void shouldGetItemRequestWithoutUser() throws Exception {
        mockMvc.perform(get("/requests"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void shouldGetItemRequestWithoutRequest() throws Exception {
        when(requestService.getRequest(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetItemRequestWithoutPaginationParams() throws Exception {
        when(requestService.getRequest(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    public void shouldGetItemRequestWithFrom0Size20() throws Exception {
        when(requestService.get(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemResponse));

        mockMvc.perform(get("/requests/all?from=0&size=20")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldItemRequestAdd() throws Exception {
        String jsonItem = objectMapper.writeValueAsString(itemResponse);

        when(requestService.create(anyLong(), any())).thenReturn(itemResponse);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Brush for footwear"))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void shouldItemRequestById() throws Exception {
        when(requestService.getRequestId(anyLong(), anyLong())).thenReturn(itemResponse);

        mockMvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Brush for footwear"))
                .andExpect(jsonPath("$.created").isNotEmpty())
                .andExpect(jsonPath("$.items", hasSize(0)));
    }
}
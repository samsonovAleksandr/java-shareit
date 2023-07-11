package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoCommentResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return ItemDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner())
                .available(item.getAvailable())
                .requestId(requestId(item))
                .build();
    }

    public Long requestId(Item item) {
        if (item.getRequestId() != null) {
            return item.getRequestId();
        }
        return null;
    }

    public List<ItemDto> itemDtoList(List<Item> itemList) {
        ArrayList<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : itemList) {
            itemDtos.add(toItemDto(item));
        }
        return itemDtos;
    }

    public ItemDtoCommentResponse toItemResponseDto(Item item, List<Booking> booking, List<Comment> comment) {
        BookingDtoItem bookingLast = null;
        BookingDtoItem bookingNext = null;
        LocalDateTime time = LocalDateTime.now();

        if (!booking.isEmpty()) {

            Optional<Booking> bookingLastOld = booking.stream()
                    .filter(b -> (b.getItem().getId() == (item.getId()) && b.getStatus().equals(BookingStatus.APPROVED)))
                    .filter(b -> (b.getStart().isBefore(time) && b.getEnd().isAfter(time)) || b.getEnd().isBefore(time))
                    .sorted(Comparator.comparing(Booking::getId).reversed())
                    .findFirst();

            Optional<Booking> bookingNextOld = booking.stream()
                    .filter(b -> b.getItem().getId() == (item.getId()) && b.getStatus().equals(BookingStatus.APPROVED))
                    .sorted(Comparator.comparing(Booking::getStart))
                    .filter(b -> b.getStart().isAfter(time))
                    .findFirst();
            if (bookingLastOld.isPresent()) {
                bookingLast = BookingMapper.toBookingDtoItem(bookingLastOld.get());
            }
            if (bookingNextOld.isPresent()) {
                bookingNext = BookingMapper.toBookingDtoItem(bookingNextOld.get());
            }

        }
        return ItemDtoCommentResponse
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner())
                .available(item.getAvailable())
                .lastBooking(bookingLast)
                .nextBooking(bookingNext)
                .comments(comment)
                .build();
    }

}

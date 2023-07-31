package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemWithBookingDto {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;
    private List<CommentDto> comments;

    public static ItemWithBookingDto consBookingForItemDto(Item item, BookingForItemDto lastBooking, BookingForItemDto nextBooking, List<CommentDto> comments) {
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto();
        itemWithBookingDto.setId(item.getId());
        itemWithBookingDto.setName(item.getName());
        itemWithBookingDto.setDescription(item.getDescription());
        itemWithBookingDto.setAvailable(item.getIsAvailable());
        itemWithBookingDto.setLastBooking(lastBooking);
        itemWithBookingDto.setNextBooking(nextBooking);
        itemWithBookingDto.setComments(comments);
        return itemWithBookingDto;
    }

}

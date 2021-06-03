package org.example.data.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingInfoDto {
    private List<BookingPersonDto> bookingInfoDto;

    public BookingInfoDto(List<BookingPersonDto> bookingInfoDto) {
        this.bookingInfoDto = bookingInfoDto;
    }
}

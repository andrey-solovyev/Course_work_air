package org.example.data.dto;

import lombok.Data;

@Data
public class CreatePassengerRecordDto {
    private String flightNumber;
    private String destinationLocation;
    private String markingAirline;
    private String originLocation;
    private String departureDateTime;
    private String resBookDesigCode;
    private BookingInfoDto bookingInfoDtol;
}

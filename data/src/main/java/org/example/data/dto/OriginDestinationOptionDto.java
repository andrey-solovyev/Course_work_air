package org.example.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OriginDestinationOptionDto {
    private String originLocation;
    private String destinationLocation;
    private String airline;
    private String flightNumber;
    private String departureDateTime;
    private String arrivalDateTime;
}

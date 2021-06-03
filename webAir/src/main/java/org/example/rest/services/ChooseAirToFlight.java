package org.example.rest.services;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChooseAirToFlight {
    private String originLocation;
    private String destinationLocation;
    private String airline;
    private String flightNumber;
    private String departureDateTime;
    private String arrivalDateTime;
    private String cabin;
    private String amount;
    private int amountPeople;

}

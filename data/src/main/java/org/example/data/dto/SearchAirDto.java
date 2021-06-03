package org.example.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class SearchAirDto {
    private ArrayList<OriginDestinationOptionDto> originDestinationOptionDtos;
    private String cabin;
    private String amount;
    private String additionalInformation;
    private int amountPeople;
}

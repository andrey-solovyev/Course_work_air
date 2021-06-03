package org.example.data.dto;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Data
public class BfmSearchAirDto {
    private String originLocation;
    private String destinationLocation;
    private String startDate;
    private String returningDate;
    private int amountADTPeople;
    private int amountCNNPeople;
    private int amountINFPeople;
    private String cabinPrefCabin;
    private boolean baggage =false;
    private boolean food= false;
    private boolean transportationOfAnimals =false;
}



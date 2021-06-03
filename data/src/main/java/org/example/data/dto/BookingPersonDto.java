package org.example.data.dto;

import lombok.Data;

@Data
public class BookingPersonDto {
    private String name;
    private String surname;
    private String middle;
    private String passengerType;
    private String passportNumber;
    private String ExpirationDate;
    private String DateOfBirth;
    private String genderOfThePerson;
    //    private String passengerTypeQuantityCode;
//    private int passengerTypeQuantityQuantity;
}

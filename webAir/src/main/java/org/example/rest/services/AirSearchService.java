package org.example.rest.services;

import org.example.contractAir.bargainfindermax.AirItineraryType;
import org.example.contractAir.bargainfindermax.OTAAirLowFareSearchRS;
import org.example.contractAir.createpassengernamerecord.CreatePassengerNameRecordRS;
import org.example.data.dto.*;
import org.example.request.services.AirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AirSearchService {
    private AirService airService;
    private ParserCsvFile parserCsvFile;

    @Autowired
    public AirSearchService(AirService airService, ParserCsvFile parserCsvFile) {
        this.airService = airService;
        this.parserCsvFile = parserCsvFile;
    }
    public void bookingAir(BookingInfoDto bookingInfoDto,ChooseAirToFlight chooseAirToFlight){
        bookingInfoDto.getBookingInfoDto().remove(0);
        CreatePassengerRecordDto createPassengerRecordDto=new CreatePassengerRecordDto();
        createPassengerRecordDto.setFlightNumber(chooseAirToFlight.getFlightNumber());
        createPassengerRecordDto.setOriginLocation(chooseAirToFlight.getOriginLocation());
        createPassengerRecordDto.setDestinationLocation(chooseAirToFlight.getDestinationLocation());
        createPassengerRecordDto.setMarkingAirline(chooseAirToFlight.getAirline());
        createPassengerRecordDto.setResBookDesigCode(chooseAirToFlight.getCabin());
        createPassengerRecordDto.setBookingInfoDtol(bookingInfoDto);
        CreatePassengerNameRecordRS createPassengerNameRecordRS = airService.bookingAirRS(createPassengerRecordDto);
    }

    private ArrayList<OriginDestinationOptionDto> getOriginDestinationOption(AirItineraryType.OriginDestinationOptions originDestinationOptions){
        var result = new ArrayList<OriginDestinationOptionDto>();
        for (var originDestinationOption:originDestinationOptions.getOriginDestinationOption()){
            var flightSegment = originDestinationOption.getFlightSegment().get(0);
            var originLocation = flightSegment.getDepartureAirport().getLocationCode();
            var destinationLocation = flightSegment.getArrivalAirport().getLocationCode();
            var flightNumber = flightSegment.getOperatingAirline().getFlightNumber();
            var airline = flightSegment.getMarketingAirline().getCode();
            var departureDateTime = flightSegment.getDepartureDateTime();
            var arrivalDateTime = flightSegment.getArrivalDateTime();
            result.add(new OriginDestinationOptionDto(originLocation,destinationLocation,airline,flightNumber,departureDateTime,arrivalDateTime));
        }
        return result;
    }

    public ArrayList<SearchAirDto> searchBfmOneWay(BfmSearchAirDto bfmSearchAirDto) {

        OTAAirLowFareSearchRS otaAirLowFareSearchRS = airService.searchRS(bfmSearchAirDto);
        var list = new ArrayList<SearchAirDto>();
        int amount = (int)bfmSearchAirDto.getAmountINFPeople()+(int)bfmSearchAirDto.getAmountADTPeople()+(int)bfmSearchAirDto.getAmountCNNPeople();
        for (var q : otaAirLowFareSearchRS.getPricedItineraries().getPricedItinerary()) {
            var originDestinationOptionDtos = getOriginDestinationOption(q.getAirItinerary().getOriginDestinationOptions());
            var cabin = q.getAirItineraryPricingInfo().get(0).getFareInfos().getFareInfo().get(0).getTPAExtensions().getCabin().getCabin();
            var baggage = q.getAirItineraryPricingInfo().get(0).getPTCFareBreakdowns().getPTCFareBreakdown().get(0).getPassengerFare().getTPAExtensions().getBaggageInformationList().getBaggageInformation().get(0).getAllowance().get(0).getDescription1();
            if (baggage==null) baggage="Standart";
            var totalText = q.getAirItineraryPricingInfo().get(0).getItinTotalFare().getTaxes().getTax().get(0).getAmount().toString();
            totalText += " " + q.getAirItineraryPricingInfo().get(0).getItinTotalFare().getTaxes().getTax().get(0).getCurrencyCode();
            list.add(new SearchAirDto(originDestinationOptionDtos,cabin,totalText,baggage,amount));
        }

        return list;
    }
}

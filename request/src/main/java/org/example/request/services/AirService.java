package org.example.request.services;

import org.example.contractAir.bargainfindermax.OTAAirLowFareSearchRS;
import org.example.contractAir.createpassengernamerecord.CreatePassengerNameRecordRS;
import org.example.contractAir.travelitinerary.TravelItineraryReadRQ;
import org.example.contractAir.travelitinerary.TravelItineraryReadRS;
import org.example.data.dto.BfmSearchAirDto;
import org.example.data.dto.CreatePassengerRecordDto;
import org.example.data.dto.TravelItineraryReadDto;
import org.example.request.orchestratedflow.BargainFinderMaxSoapActivity;
import org.example.request.orchestratedflow.CreatePassengerRecordSoapActivity;
import org.example.request.orchestratedflow.TravelItineraryReadActivity;
import org.example.workflow.SharedContext;
import org.example.workflow.Workflow;
import org.springframework.stereotype.Service;

@Service
public class AirService {
    private BargainFinderMaxSoapActivity bargainFinderMaxSoapActivity;
    private CreatePassengerRecordSoapActivity createPassengerRecordSoapActivity;
    private TravelItineraryReadActivity travelItineraryReadActivity;

    public AirService(BargainFinderMaxSoapActivity bargainFinderMaxSoapActivity, CreatePassengerRecordSoapActivity createPassengerRecordSoapActivity, TravelItineraryReadActivity travelItineraryReadActivity) {
        this.bargainFinderMaxSoapActivity = bargainFinderMaxSoapActivity;
        this.createPassengerRecordSoapActivity = createPassengerRecordSoapActivity;
        this.travelItineraryReadActivity = travelItineraryReadActivity;
    }

    public OTAAirLowFareSearchRS searchRS(BfmSearchAirDto bfmSearchAirDto){
        Workflow workflow=new Workflow(bargainFinderMaxSoapActivity);
        bargainFinderMaxSoapActivity.setBfmSearchAirDto(bfmSearchAirDto);
        SharedContext wfResult = workflow.run();
        return (OTAAirLowFareSearchRS) wfResult.getResult("BargainFinderMaxRSObj");
    }

    public CreatePassengerNameRecordRS bookingAirRS(CreatePassengerRecordDto createPassengerRecordDto){
        Workflow workflow=new Workflow(createPassengerRecordSoapActivity);
        createPassengerRecordSoapActivity.setCreatePassengerRecordDto(createPassengerRecordDto);
        SharedContext wfResult = workflow.run();
        return (CreatePassengerNameRecordRS) wfResult.getResult("CreatePassengerNameRecordRSObj");
    }
    public TravelItineraryReadRS getBooked(TravelItineraryReadDto travelItineraryReadDto){
        Workflow workflow=new Workflow(travelItineraryReadActivity);
        travelItineraryReadActivity.setTravelItineraryReadDto(travelItineraryReadDto);
        SharedContext wfResult = workflow.run();
        return (TravelItineraryReadRS) wfResult.getResult("TravelItineraryReadRSObj");
    }
}

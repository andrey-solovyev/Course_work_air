package org.example.request.orchestratedflow;

import com.sabre.api.sacs.errors.ErrorHandlingSchedule;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.configuration.SacsConfiguration;
import org.example.contractAir.bargainfindermax.*;
import org.example.contractAir.bargainfindermax.OriginDestinationInformationType.DestinationLocation;
import org.example.contractAir.bargainfindermax.OriginDestinationInformationType.OriginLocation;
import org.example.contractAir.bargainfindermax.TransactionType.RequestType;
import org.example.contractAir.bargainfindermax.AirSearchPrefsType;
import org.example.contractAir.bargainfindermax.CabinPrefType;
import org.example.contractAir.bargainfindermax.CabinType;
import org.example.contractAir.bargainfindermax.CompanyNameType;
import org.example.contractAir.bargainfindermax.ExchangeOriginDestinationInformationType.SegmentType;
import org.example.contractAir.bargainfindermax.OTAAirLowFareSearchRQ;
import org.example.contractAir.bargainfindermax.OTAAirLowFareSearchRQ.TPAExtensions;
import org.example.contractAir.bargainfindermax.OTAAirLowFareSearchRS;
import org.example.contractAir.bargainfindermax.OTAAirLowFareSearchRQ.OriginDestinationInformation;
import org.example.contractAir.bargainfindermax.OriginDestinationInformationType.DestinationLocation;
import org.example.contractAir.bargainfindermax.OriginDestinationInformationType.OriginLocation;
import org.example.contractAir.bargainfindermax.POSType;
import org.example.contractAir.bargainfindermax.PassengerTypeQuantityType;
import org.example.contractAir.bargainfindermax.PreferLevelType;
import org.example.contractAir.bargainfindermax.SourceType;
import org.example.contractAir.bargainfindermax.TransactionType;
import org.example.contractAir.bargainfindermax.TransactionType.RequestType;
import org.example.contractAir.bargainfindermax.TravelerInfoSummaryType;
import org.example.contractAir.bargainfindermax.TravelerInformationType;
import org.example.contractAir.bargainfindermax.UniqueIDType;
import org.example.data.dto.BfmSearchAirDto;
import org.example.request.common.GenericRequestWrapper;
import org.example.request.pool.SessionPool;
import org.example.workflow.Activity;
import org.example.workflow.SharedContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Controller
@Scope("prototype")
@Data
public class BargainFinderMaxSoapActivity implements Activity {

    private static final Logger LOG = LogManager.getLogger(BargainFinderMaxSoapActivity.class);

    private final GenericRequestWrapper<OTAAirLowFareSearchRQ, OTAAirLowFareSearchRS> bfm;

    private final ErrorHandlingSchedule errorHandler;

    private final SacsConfiguration config;

    private final SessionPool sessionPool;

    private BfmSearchAirDto bfmSearchAirDto;

    @Autowired
    public BargainFinderMaxSoapActivity(@Qualifier("bargainFinderMaxWrapper") GenericRequestWrapper<OTAAirLowFareSearchRQ, OTAAirLowFareSearchRS> bfm, ErrorHandlingSchedule errorHandler, SacsConfiguration config, SessionPool sessionPool) {
        this.bfm = bfm;
        this.errorHandler = errorHandler;
        this.config = config;
        this.sessionPool = sessionPool;
    }


    @Override
    public void run(SharedContext context) {
        Marshaller marsh;
        try {
            marsh = JAXBContext.newInstance("org.example.contractAir.bargainfindermax").createMarshaller();
            StringWriter sw = new StringWriter();
            OTAAirLowFareSearchRQ request = getRequestBody();
            bfm.setRequest(request);
            bfm.setLastInFlow(false);
            marsh.marshal(request, sw);
            context.putResult("BargainFinderMaxRQ", sw.toString());
            OTAAirLowFareSearchRS result = bfm.executeRequest(context);
            if (result.getErrors() != null && result.getErrors().getError() != null && !result.getErrors().getError().isEmpty()) {
                context.setFaulty(true);
                LOG.warn("Error found, adding context to ErrorHandler. ConversationID: " + context.getConversationId());
                errorHandler.addSystemFailure(context);
                sessionPool.returnToPool(context.getConversationId());
                return;
            }
            sw = new StringWriter();
            marsh.marshal(result, sw);
            context.putResult("BargainFinderMaxRSObj", result);
            context.putResult("BargainFinderMaxRS", sw.toString());
        } catch (JAXBException e) {
            LOG.error("Error while marshalling the response.", e);
        } catch (InterruptedException e) {
            LOG.catching(e);
        }
    }

    private OTAAirLowFareSearchRQ getRequestBody() {
        OTAAirLowFareSearchRQ result = new OTAAirLowFareSearchRQ();

        //<POS>
        POSType pos = new POSType();
        SourceType srcType = new SourceType();
        srcType.setPseudoCityCode(config.getSoapProperty("group"));

        UniqueIDType uidType = new UniqueIDType();
        uidType.setType("1");
        uidType.setID("1");
        CompanyNameType compNameType = new CompanyNameType();
        compNameType.setCode("TN");
        uidType.setCompanyName(compNameType);
        srcType.setRequestorID(uidType);

        pos.getSource().add(srcType);
        result.setPOS(pos);

        //<OriginDestinationInformation
        OriginDestinationInformation odi = new OriginDestinationInformation();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parsed = format.parse(bfmSearchAirDto.getStartDate());
            Calendar cal = new GregorianCalendar();
            cal.setTime(parsed);
            cal.set(Calendar.YEAR,2021);
            cal.set(Calendar.HOUR,11);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            odi.setDepartureDateTime(sdf.format(cal.getTime()));
        } catch (Exception e){       }

        //odi.setDepartureDateTime(sdf.format(bfmSearchAirDto.getStartDate()));

        OriginLocation ol = new OriginLocation();
        ol.setLocationCode(bfmSearchAirDto.getOriginLocation());
        odi.setOriginLocation(ol);

        DestinationLocation dl = new DestinationLocation();
        dl.setLocationCode(bfmSearchAirDto.getDestinationLocation());
        odi.setDestinationLocation(dl);
        OriginDestinationInformation.TPAExtensions odiTpa
                = new OriginDestinationInformation.TPAExtensions();
        SegmentType segType = new SegmentType();
        segType.setCode("O");
        odiTpa.setSegmentType(segType);
        odi.setTPAExtensions(odiTpa);
        result.getOriginDestinationInformation().add(odi);

        //<TravelPreferences>
        AirSearchPrefsType travelPreferences = new AirSearchPrefsType();
        travelPreferences.setValidInterlineTicket(true);
        CabinPrefType cabinPref = new CabinPrefType();
        if (bfmSearchAirDto.getCabinPrefCabin().equals("Y")) cabinPref.setCabin(CabinType.Y);
        else if (bfmSearchAirDto.getCabinPrefCabin().equals("S")) cabinPref.setCabin(CabinType.S);
        else if (bfmSearchAirDto.getCabinPrefCabin().equals("C")) cabinPref.setCabin(CabinType.C);
        else if (bfmSearchAirDto.getCabinPrefCabin().equals("J")) cabinPref.setCabin(CabinType.J);
        else if (bfmSearchAirDto.getCabinPrefCabin().equals("F")) cabinPref.setCabin(CabinType.F);
        else if (bfmSearchAirDto.getCabinPrefCabin().equals("P")) cabinPref.setCabin(CabinType.P);
        else cabinPref.setCabin(CabinType.Y);
        cabinPref.setPreferLevel(PreferLevelType.PREFERRED);
        travelPreferences.getCabinPref().add(cabinPref);
        AirSearchPrefsType.Baggage baggage=new AirSearchPrefsType.Baggage();
        baggage.setRequestType(BaggageRequestType.A);
        baggage.setDescription(true);
        travelPreferences.setBaggage(baggage);
        travelPreferences.getBaggage().setDescription(true);

        result.setTravelPreferences(travelPreferences);
        //<TravelerInfoSummary>
        TravelerInfoSummaryType tiSummaryType = new TravelerInfoSummaryType();
        tiSummaryType.getSeatsRequested().add(new BigInteger("1"));

        TravelerInformationType airTravelerAvail = new TravelerInformationType();
        if (bfmSearchAirDto.getAmountADTPeople() > 0) {
            PassengerTypeQuantityType passengerTypeQuantity = new PassengerTypeQuantityType();
            passengerTypeQuantity.setCode("ADT");
            passengerTypeQuantity.setQuantity(1);
            airTravelerAvail.getPassengerTypeQuantity().add(passengerTypeQuantity);
        }
        if (bfmSearchAirDto.getAmountCNNPeople() > 0) {
            PassengerTypeQuantityType passengerTypeQuantity = new PassengerTypeQuantityType();
            passengerTypeQuantity.setCode("CNN");
            passengerTypeQuantity.setQuantity(bfmSearchAirDto.getAmountCNNPeople());
            airTravelerAvail.getPassengerTypeQuantity().add(passengerTypeQuantity);
        }
        if (bfmSearchAirDto.getAmountINFPeople() > 0) {
            PassengerTypeQuantityType passengerTypeQuantity = new PassengerTypeQuantityType();
            passengerTypeQuantity.setCode("INF");
            passengerTypeQuantity.setQuantity(bfmSearchAirDto.getAmountINFPeople());
            airTravelerAvail.getPassengerTypeQuantity().add(passengerTypeQuantity);
        }

        tiSummaryType.getAirTravelerAvail().add(airTravelerAvail);
        result.setTravelerInfoSummary(tiSummaryType);

        //<TPA_Extension>
        TPAExtensions tpa = new TPAExtensions();
        TransactionType intelliSell = new TransactionType();
        RequestType reqType = new RequestType();
        reqType.setName("50ITINS");
        intelliSell.setRequestType(reqType);
        tpa.setIntelliSellTransaction(intelliSell);
        result.setTPAExtensions(tpa);
        result.setVersion(config.getSoapProperty("BargainFinderMaxRQVersion"));

        return result;
    }
//    private OTAAirLowFareSearchRQ getRequestBody() {
//        OTAAirLowFareSearchRQ result = new OTAAirLowFareSearchRQ();
//
//        //<POS>
//        POSType pos = new POSType();
//        SourceType srcType = new SourceType();
//        LOG.error(config.toString());
//        srcType.setPseudoCityCode(config.getSoapProperty("group"));
//        UniqueIDType uidType = new UniqueIDType();
//        uidType.setType("1");
//        uidType.setID("1");
//        CompanyNameType compNameType = new CompanyNameType();
//        compNameType.setCode("TN");
//        uidType.setCompanyName(compNameType);
//        srcType.setRequestorID(uidType);
//
//        pos.getSource().add(srcType);
//        result.setPOS(pos);
//
//        //<OriginDestinationInformation
//        OriginDestinationInformation odi = new OriginDestinationInformation();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_YEAR, 1);
//        odi.setDepartureDateTime(sdf.format(cal.getTime()));
//
//        OriginLocation ol = new OriginLocation();
//        ol.setLocationCode("JFK");
//        odi.setOriginLocation(ol);
//
//        DestinationLocation dl = new DestinationLocation();
//        dl.setLocationCode("LAX");
//        odi.setDestinationLocation(dl);
//        org.example.contractAir.bargainfindermax.OTAAirLowFareSearchRQ.OriginDestinationInformation.TPAExtensions odiTpa
//                = new org.example.contractAir.bargainfindermax.OTAAirLowFareSearchRQ.OriginDestinationInformation.TPAExtensions();
//        SegmentType segType = new SegmentType();
//        segType.setCode("O");
//        odiTpa.setSegmentType(segType);
//        odi.setTPAExtensions(odiTpa);
//        result.getOriginDestinationInformation().add(odi);
//
//        //<TravelPreferences>
//        AirSearchPrefsType travelPreferences = new AirSearchPrefsType();
//        travelPreferences.setValidInterlineTicket(true);
//        CabinPrefType cabinPref = new CabinPrefType();
//        cabinPref.setCabin(CabinType.Y);
//        cabinPref.setPreferLevel(PreferLevelType.PREFERRED);
//        travelPreferences.getCabinPref().add(cabinPref);
//        result.setTravelPreferences(travelPreferences);
//
//        //<TravelerInfoSummary>
//        TravelerInfoSummaryType tiSummaryType = new TravelerInfoSummaryType();
//        tiSummaryType.getSeatsRequested().add(new BigInteger("1"));
//
//        TravelerInformationType airTravelerAvail = new TravelerInformationType();
//        PassengerTypeQuantityType passengerTypeQuantity = new PassengerTypeQuantityType();
//        passengerTypeQuantity.setCode("ADT");
//        passengerTypeQuantity.setQuantity(1);
//        airTravelerAvail.getPassengerTypeQuantity().add(passengerTypeQuantity);
//
//        tiSummaryType.getAirTravelerAvail().add(airTravelerAvail);
//        result.setTravelerInfoSummary(tiSummaryType);
//
//        //<TPA_Extension>
//        TPAExtensions tpa = new TPAExtensions();
//        TransactionType intelliSell = new TransactionType();
//        RequestType reqType = new RequestType();
//        reqType.setName("50ITINS");
//        intelliSell.setRequestType(reqType);
//        tpa.setIntelliSellTransaction(intelliSell);
//
//        result.setTPAExtensions(tpa);
//        result.setVersion(config.getSoapProperty("BargainFinderMaxRQVersion"));
//
//        return result;
//    }

}

package org.example.request.orchestratedflow;

import com.sabre.api.sacs.errors.ErrorHandlingSchedule;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.configuration.SacsConfiguration;
import org.example.contractAir.bargainfindermax.*;
import org.example.contractAir.createpassengernamerecord.CreatePassengerNameRecordRQ;
import org.example.contractAir.createpassengernamerecord.CreatePassengerNameRecordRS;
import org.example.data.dto.BookingPersonDto;
import org.example.data.dto.CreatePassengerRecordDto;
import org.example.request.common.GenericRequestWrapper;
import org.example.request.pool.SessionPool;
import org.example.workflow.Activity;
import org.example.workflow.SharedContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Controller
@Scope("prototype")
@Data
public class CreatePassengerRecordSoapActivity implements Activity {

    private static final Logger LOG = LogManager.getLogger(BargainFinderMaxSoapActivity.class);
    private static DatatypeFactory df = null;

    static {
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            LOG.error("Exception while obtaining DatatypeFactory instance", dce);
        }
    }

    private final GenericRequestWrapper<CreatePassengerNameRecordRQ, CreatePassengerNameRecordRS> cpn;

    private final ErrorHandlingSchedule errorHandler;

    private final SacsConfiguration config;

    private final SessionPool sessionPool;

    private CreatePassengerRecordDto createPassengerRecordDto;

    @Autowired
    public CreatePassengerRecordSoapActivity(GenericRequestWrapper<CreatePassengerNameRecordRQ, CreatePassengerNameRecordRS> cpn, ErrorHandlingSchedule errorHandler, SacsConfiguration config, SessionPool sessionPool) {
        this.cpn = cpn;
        this.errorHandler = errorHandler;
        this.config = config;
        this.sessionPool = sessionPool;
    }

    @Override
    public void run(SharedContext context) {
        Marshaller marsh;
        try {
            marsh = JAXBContext.newInstance("org.example.contractAir.createpassengernamerecord").createMarshaller();
            StringWriter sw = new StringWriter();
            CreatePassengerNameRecordRQ request = null;
            request = getRequestBody();
            cpn.setRequest(request);
            cpn.setLastInFlow(false);
            marsh.marshal(request, sw);
            context.putResult("CreatePassengerNameRecordRQ", sw.toString());
            CreatePassengerNameRecordRS result = cpn.executeRequest(context);
//            if (result != null && result.getErrors().getError() != null && !result.getErrors().getError().isEmpty()) {
//                context.setFaulty(true);
//                LOG.warn("Error found, adding context to ErrorHandler. ConversationID: " + context.getConversationId());
//                errorHandler.addSystemFailure(context);
//                sessionPool.returnToPool(context.getConversationId());
//                return;
//            }
            sw = new StringWriter();
            marsh.marshal(result, sw);
            context.putResult("CreatePassengerNameRecordRSObj", result);
            context.putResult("CreatePassengerNameRecordRS", sw.toString());
        } catch (JAXBException e) {
            LOG.error("Error while marshalling the response.", e);
        } catch (InterruptedException e) {
            LOG.catching(e);
        }
    }

    public static XMLGregorianCalendar asXMLGregorianCalendar(String string) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
        javax.xml.datatype.XMLGregorianCalendar calendar = null;
        try {
            Date date = format.parse(string);
            calDate.setTime(date);
            javax.xml.datatype.DatatypeFactory factory = javax.xml.datatype.DatatypeFactory.newInstance();
            calendar = factory.newXMLGregorianCalendarDate(
                    calDate.get(java.util.GregorianCalendar.YEAR),
                    calDate.get(java.util.GregorianCalendar.MONTH) + 1,
                    calDate.get(java.util.GregorianCalendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
            //calendar.setTimezone((DatatypeConstants.FIELD_UNDEFINED));
        } catch (Exception dce) {
            //handle or throw
        }
        return calendar;
    }

    //org.springframework.ws.soap.client.SoapFaultClientException: Message validation failed. Errors: [cvc-complex-type.2.4.b: The content of element 'n123:CreatePassengerNameRecordRQ' is not complete. One of '{"http://services.sabre.com/sp/reservation/v2_4":PostProcessing}' is expected.]
    private CreatePassengerNameRecordRQ getRequestBody() {

        CreatePassengerNameRecordRQ result = new CreatePassengerNameRecordRQ();

        //CreatePassengerNameRecordRS.AirPrice.PriceQuote.PricedItinerary.AirItineraryPricingInfo.BaggageProvisions.Associations.FlightNumber flightNumber=new FlightNumberLiterals();
        CreatePassengerNameRecordRQ.AirBook.OriginDestinationInformation.FlightSegment flightSegment = new CreatePassengerNameRecordRQ.AirBook.OriginDestinationInformation.FlightSegment();
        CreatePassengerNameRecordRQ.AirBook.OriginDestinationInformation.FlightSegment.DestinationLocation destinationLocation = new CreatePassengerNameRecordRQ.AirBook.OriginDestinationInformation.FlightSegment.DestinationLocation();
        destinationLocation.setLocationCode(createPassengerRecordDto.getDestinationLocation());
        flightSegment.setDestinationLocation(destinationLocation);

        CreatePassengerNameRecordRQ.AirBook.OriginDestinationInformation.FlightSegment.OriginLocation originLocation = new CreatePassengerNameRecordRQ.AirBook.OriginDestinationInformation.FlightSegment.OriginLocation();
        originLocation.setLocationCode(createPassengerRecordDto.getOriginLocation());
        flightSegment.setOriginLocation(originLocation);

        CreatePassengerNameRecordRQ.AirBook.OriginDestinationInformation.FlightSegment.MarketingAirline marketingAirline = new CreatePassengerNameRecordRQ.AirBook.OriginDestinationInformation.FlightSegment.MarketingAirline();
        marketingAirline.setCode(createPassengerRecordDto.getMarkingAirline());
        marketingAirline.setFlightNumber(createPassengerRecordDto.getFlightNumber());
        flightSegment.setMarketingAirline(marketingAirline);

        flightSegment.setFlightNumber(createPassengerRecordDto.getFlightNumber());
        flightSegment.setDepartureDateTime(createPassengerRecordDto.getDepartureDateTime());
        flightSegment.setNumberInParty("" + createPassengerRecordDto.getBookingInfoDtol().getBookingInfoDto().size());
        flightSegment.setResBookDesigCode(createPassengerRecordDto.getResBookDesigCode());
        flightSegment.setStatus("NN");
        CreatePassengerNameRecordRQ.AirBook air = new CreatePassengerNameRecordRQ.AirBook();
        air.setOriginDestinationInformation(new CreatePassengerNameRecordRQ.AirBook.OriginDestinationInformation());
        air.getOriginDestinationInformation().getFlightSegment().add(flightSegment);
        String[] status = {"HL", "HN", "HX", "LL", "NN", "NO", "PN", "UC", "UN", "US", "UU"};

        CreatePassengerNameRecordRQ.TravelItineraryAddInfo.CustomerInfo.ContactNumbers.ContactNumber contactNumber = new CreatePassengerNameRecordRQ.TravelItineraryAddInfo.CustomerInfo.ContactNumbers.ContactNumber();
        for (String s : status) {
            CreatePassengerNameRecordRQ.AirBook.HaltOnStatus stat = new CreatePassengerNameRecordRQ.AirBook.HaltOnStatus();
            stat.setCode(s);
            air.getHaltOnStatus().add(stat);
        }
        result.setAirBook(air);
        CreatePassengerNameRecordRQ.AirPrice airPrice= new CreatePassengerNameRecordRQ.AirPrice();



        int i = 1;
        result.setTravelItineraryAddInfo(new CreatePassengerNameRecordRQ.TravelItineraryAddInfo());
        result.getTravelItineraryAddInfo().setCustomerInfo(new CreatePassengerNameRecordRQ.TravelItineraryAddInfo.CustomerInfo());
        for (BookingPersonDto bookingPersonDto : createPassengerRecordDto.getBookingInfoDtol().getBookingInfoDto()) {
            CreatePassengerNameRecordRQ.TravelItineraryAddInfo.CustomerInfo.PersonName personName = new CreatePassengerNameRecordRQ.TravelItineraryAddInfo.CustomerInfo.PersonName();
            personName.setGivenName(bookingPersonDto.getName());
            personName.setPassengerType(bookingPersonDto.getPassengerType());
            personName.setSurname(bookingPersonDto.getSurname());
            personName.setNameNumber((i++) + ".1");
            if (bookingPersonDto.getPassengerType().equals("INF")) personName.setInfant(true);
            result.getTravelItineraryAddInfo().getCustomerInfo().getPersonName().add(personName);
        }
        i = 1;
        result.setSpecialReqDetails(new CreatePassengerNameRecordRQ.SpecialReqDetails());
        result.getSpecialReqDetails().setSpecialService(new CreatePassengerNameRecordRQ.SpecialReqDetails.SpecialService());
        result.getSpecialReqDetails().getSpecialService().setSpecialServiceInfo(new CreatePassengerNameRecordRQ.SpecialReqDetails.SpecialService.SpecialServiceInfo());


        for (BookingPersonDto bookingPersonDto : createPassengerRecordDto.getBookingInfoDtol().getBookingInfoDto()) {
            CreatePassengerNameRecordRQ.SpecialReqDetails.SpecialService.SpecialServiceInfo.AdvancePassenger advancePassenger = new CreatePassengerNameRecordRQ.SpecialReqDetails.SpecialService.SpecialServiceInfo.AdvancePassenger();


            CreatePassengerNameRecordRQ.SpecialReqDetails.SpecialService.SpecialServiceInfo.AdvancePassenger.Document document = new CreatePassengerNameRecordRQ.SpecialReqDetails.SpecialService.SpecialServiceInfo.AdvancePassenger.Document();
            CreatePassengerNameRecordRQ.SpecialReqDetails.SpecialService.SpecialServiceInfo.AdvancePassenger.PersonName personName = new CreatePassengerNameRecordRQ.SpecialReqDetails.SpecialService.SpecialServiceInfo.AdvancePassenger.PersonName();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            document.setExpirationDate(asXMLGregorianCalendar(bookingPersonDto.getExpirationDate()));
            personName.setDateOfBirth(asXMLGregorianCalendar(bookingPersonDto.getDateOfBirth()));

            document.setNumber(bookingPersonDto.getPassportNumber());
            document.setType("P");
            document.setIssueCountry("RU");
            document.setNationalityCountry("RU");
            personName.setGender(bookingPersonDto.getGenderOfThePerson());
            personName.setNameNumber((i++) + ".1");
            personName.setGivenName(bookingPersonDto.getName());
            personName.setSurname(bookingPersonDto.getSurname());
            personName.setGivenName(bookingPersonDto.getMiddle());
            advancePassenger.setPersonName(personName);
            advancePassenger.setDocument(document);
            result.getSpecialReqDetails().getSpecialService().getSpecialServiceInfo().getAdvancePassenger().add(advancePassenger);
        }
        result.setVersion(config.getSoapProperty("CreatePassengerNameRecordRQVersion"));
        CreatePassengerNameRecordRQ.PostProcessing postProcessing = new CreatePassengerNameRecordRQ.PostProcessing();
        CreatePassengerNameRecordRQ.PostProcessing.EndTransaction endTransaction = new CreatePassengerNameRecordRQ.PostProcessing.EndTransaction();
        CreatePassengerNameRecordRQ.PostProcessing.EndTransaction.Source source = new CreatePassengerNameRecordRQ.PostProcessing.EndTransaction.Source();
        source.setReceivedFrom("API");
        endTransaction.setSource(source);
        CreatePassengerNameRecordRQ.PostProcessing.WaitForAirlineRecLoc waitForAirlineRecLoc = new CreatePassengerNameRecordRQ.PostProcessing.WaitForAirlineRecLoc();
        waitForAirlineRecLoc.setWaitInterval(500);
        waitForAirlineRecLoc.setNumAttempts(6);
        postProcessing.setEndTransaction(endTransaction);
        postProcessing.setWaitForAirlineRecLoc(waitForAirlineRecLoc);
        result.setPostProcessing(postProcessing);
        return result;
    }
}

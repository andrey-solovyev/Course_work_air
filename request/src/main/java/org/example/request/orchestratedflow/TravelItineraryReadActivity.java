package org.example.request.orchestratedflow;

import org.example.configuration.SacsConfiguration;
import org.example.contractAir.travelitinerary.TravelItineraryReadRQ;
import org.example.contractAir.travelitinerary.TravelItineraryReadRS;
import com.sabre.api.sacs.errors.ErrorHandlingSchedule;
import org.example.data.dto.TravelItineraryReadDto;
import org.example.request.common.GenericRequestWrapper;
import org.example.request.pool.SessionPool;
import org.example.workflow.Activity;
import org.example.workflow.SharedContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Activity class to be used in the workflow. It runs the TravelItineraryRead
 * request.
 */
@Controller
@Scope("prototype")
public class TravelItineraryReadActivity implements Activity {

    private static final Logger LOG = LogManager.getLogger(TravelItineraryReadActivity.class);

    @Autowired
    private GenericRequestWrapper<TravelItineraryReadRQ, TravelItineraryReadRS> tir;

    @Autowired
    private ErrorHandlingSchedule errorHandler;

    @Autowired
    private SacsConfiguration configuration;

    @Autowired
    private SessionPool sessionPool;

    private TravelItineraryReadDto travelItineraryReadDto;

    @Override
    public void run(SharedContext context) {
        Marshaller marsh;
        try {
            marsh = JAXBContext.newInstance("com.sabre.api.sacs.contract.travelitinerary").createMarshaller();
            StringWriter sw = new StringWriter();
            TravelItineraryReadRQ request = getRequestBody(context.getResult("PNR").toString());
            tir.setRequest(request);
            tir.setLastInFlow(true);
            marsh.marshal(request, sw);
            context.putResult("TravelItineraryReadRQ", sw.toString());
            TravelItineraryReadRS result = tir.executeRequest(context);
            if (result.getApplicationResults() != null && result.getApplicationResults().getError() != null
                    && !result.getApplicationResults().getError().isEmpty()) {
                context.setFaulty(true);
                LOG.warn("Error found, adding context to ErrorHandler. ConversationID: " + context.getConversationId());
                errorHandler.addSystemFailure(context);
                sessionPool.returnToPool(context.getConversationId());
                return;
            }
            sw = new StringWriter();
            marsh.marshal(result, sw);
            context.putResult("TravelItineraryReadRSObj", result);
            context.putResult("TravelItineraryReadRS", sw.toString());
        } catch (JAXBException e) {
            LOG.error("Error while marshalling the response.", e);
        } catch (InterruptedException e) {
            LOG.catching(e);
        }
    }

    private TravelItineraryReadRQ getRequestBody(String pnr) {

        TravelItineraryReadRQ body = new TravelItineraryReadRQ();

        body.setVersion(configuration.getSoapProperty("TravelItineraryReadRQVersion"));

        TravelItineraryReadRQ.MessagingDetails details = new TravelItineraryReadRQ.MessagingDetails();
        TravelItineraryReadRQ.MessagingDetails.SubjectAreas subjectAreas = new TravelItineraryReadRQ.MessagingDetails.SubjectAreas();
        subjectAreas.getSubjectArea().add("PNR");
        details.setSubjectAreas(subjectAreas);
        body.setMessagingDetails(details);

        TravelItineraryReadRQ.UniqueID uid = new TravelItineraryReadRQ.UniqueID();
        uid.setID(pnr);
        uid.setID(travelItineraryReadDto.getUniqueID());
        body.setUniqueID(uid);

        return body;
    }

    public void setTravelItineraryReadDto(TravelItineraryReadDto travelItineraryReadDto) {
        this.travelItineraryReadDto = travelItineraryReadDto;
    }
}

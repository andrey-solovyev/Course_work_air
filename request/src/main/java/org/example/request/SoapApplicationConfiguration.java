package org.example.request;


import com.sabre.api.sacs.errors.ErrorHandlerConfiguration;
import org.example.configuration.ConfigurationConfig;
import org.example.contractAir.session.SessionCreateRQ;
import org.example.contractAir.soap.MessageHeader;
import org.example.request.callback.HeaderComposingCallback;
import org.springframework.context.annotation.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main configuration class. Adds callbacks to the Spring context, as well as
 * the marshaller used to marshal/unmarshal security header.
 */
@Configuration
@ComponentScan
@Import({ ConfigurationConfig.class, ErrorHandlerConfiguration.class })
@EnableScheduling
public class SoapApplicationConfiguration {

    @Bean
    @Scope("prototype")
    public HeaderComposingCallback travelItineraryHeaderComposingCallback() {
        return new HeaderComposingCallback("TravelItineraryReadRQ");
    }

    @Bean
    @Scope("prototype")
    public HeaderComposingCallback passengerDetailsHeaderComposingCallback() {
        return new HeaderComposingCallback("PassengerDetailsRQ");
    }

    @Bean
    @Scope("prototype")
    public HeaderComposingCallback bargainFinderMaxHeaderComposingCallback() {
        return new HeaderComposingCallback("BargainFinderMaxRQ");
    }

    @Bean
    @Scope("prototype")
    public HeaderComposingCallback createPassengerNameRecordComposingCallback() {
        return new HeaderComposingCallback("CreatePassengerNameRecordRQ");
    }

    @Bean
    @Scope("prototype")
    public HeaderComposingCallback enhancedAirBookHeaderComposingCallback() {
        return new HeaderComposingCallback("EnhancedAirBookRQ");
    }

    @Bean
    @Scope("prototype")
    public HeaderComposingCallback sessionCloseHeaderComposingCallback() {
        return new HeaderComposingCallback("SessionCloseRQ");
    }

    @Bean
    public Jaxb2Marshaller securityMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        StringBuffer contextPath = new StringBuffer()
                .append(MessageHeader.class.getPackage().getName())
                .append(":")
                .append(SessionCreateRQ.class.getPackage().getName());
        marshaller.setContextPath(contextPath.toString());
        return marshaller;
    }

}

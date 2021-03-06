package org.example.request.callback;

import org.example.configuration.SacsConfiguration;
import org.example.contractAir.soap.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Class generating message header objects with proper action string and
 * PCC taken from the configuration. 
 */
@Controller
public class MessageHeaderFactory {

    @Autowired
    private SacsConfiguration configuration;

    public MessageHeader getMessageHeader(String actionString) {

        MessageHeader header = new MessageHeader();

        header.setAction(actionString);
        header.setCPAId(configuration.getSoapProperty("group"));

        header.setFrom(getFrom());
        header.setTo(getTo());
        
        Service service = new Service();
        service.setValue(actionString);
        header.setService(service);

        return header;
    }

    private From getFrom() {
        From from = new From();
        PartyId fromPartyId = new PartyId();
        fromPartyId.setValue("sample.url.of.sabre.client.com");
        from.getPartyId().add(fromPartyId);
        return from;
    }

    private To getTo() {
        To to = new To();
        PartyId toPartyId = new PartyId();
        toPartyId.setValue("webservices.sabre.com");
        to.getPartyId().add(toPartyId);
        return to;
    }

}

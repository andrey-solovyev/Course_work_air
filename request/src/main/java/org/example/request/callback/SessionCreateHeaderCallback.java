package org.example.request.callback;

import org.example.configuration.SacsConfiguration;
import org.example.contractAir.soap.MessageHeader;
import org.example.contractAir.soap.Security;
import org.example.workflow.SharedContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * SpringWS callback used for adding credentials to the call, that creates a session.
 */
@Controller
public class SessionCreateHeaderCallback implements HeaderCallback {

    private static final String ACTION_STRING = "SessionCreateRQ";

    private MessageHeader header;
    private SharedContext workflowContext;
    private Security security;

    @Autowired
    private MessageHeaderFactory messageHeaderFactory;

    @Autowired
    private SacsConfiguration configuration;

    @Autowired
    private Jaxb2Marshaller marshaller;

    @Override
    public void doWithMessage(WebServiceMessage webServiceMessage) throws IOException, TransformerException {

        header = messageHeaderFactory.getMessageHeader(ACTION_STRING);
        header.setConversationId(workflowContext.getConversationId());

        security = getCredentialsSecurity();

        SoapHeader soapHeaderElement = ((SoapMessage) webServiceMessage).getSoapHeader();

        marshaller.marshal(header, soapHeaderElement.getResult());
        marshaller.marshal(security, soapHeaderElement.getResult());

    }

    public void setWorkflowContext(SharedContext workflowContext) {
        this.workflowContext = workflowContext;
    }

    private Security getCredentialsSecurity() {

        Security security = new Security();
        Security.UsernameToken usernameToken = new Security.UsernameToken();

        
        usernameToken.setDomain(configuration.getSoapProperty("domain"));
        usernameToken.setOrganization(configuration.getSoapProperty("group"));
        usernameToken.setPassword(configuration.getEncodedSoapProperty("clientSecret"));
        usernameToken.setUsername(configuration.getEncodedSoapProperty("userId"));
        security.setUsernameToken(usernameToken);

        return security;
    }

}

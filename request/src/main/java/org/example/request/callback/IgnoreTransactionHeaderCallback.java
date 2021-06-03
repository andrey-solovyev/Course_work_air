package org.example.request.callback;


import org.example.contractAir.soap.MessageHeader;
import org.example.contractAir.soap.Security;
import org.example.workflow.SharedContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Callback class used when the IgnoreTransactionLLS is being called.
 * It's used when the session is about to return to the pool.
 */
@Controller
public class IgnoreTransactionHeaderCallback implements WebServiceMessageCallback {
    
    private MessageHeader header;
    private SharedContext workflowContext;
    private Security session;

    @Autowired
    private MessageHeaderFactory messageHeaderFactory;

    @Autowired
    private Jaxb2Marshaller marshaller;

    @Override
    public void doWithMessage(WebServiceMessage webServiceMessage) throws IOException, TransformerException {

        header = messageHeaderFactory.getMessageHeader("IgnoreTransactionLLSRQ");
        header.setConversationId(workflowContext.getConversationId());

        SoapHeader soapHeaderElement = ((SoapMessage) webServiceMessage).getSoapHeader();

        marshaller.marshal(header, soapHeaderElement.getResult());
        marshaller.marshal(session, soapHeaderElement.getResult());

    }
    
    public void setSessionToIgnore(Security session) {
        this.session = session;
    }

    public void setWorkflowContext(SharedContext workflowContext) {
        this.workflowContext = workflowContext;
    }

}

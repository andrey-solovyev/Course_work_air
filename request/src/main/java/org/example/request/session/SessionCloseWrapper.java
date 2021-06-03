package org.example.request.session;


import org.example.configuration.SacsConfiguration;
import org.example.contractAir.session.SessionCloseRQ;
import org.example.contractAir.session.SessionCloseRS;
import org.example.contractAir.soap.Security;
import org.example.request.callback.SessionCloseHeaderCallback;
import org.example.request.interceptor.LoggingInterceptor;
import org.example.request.interceptor.SessionCloseInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class calling the SessionClose service, which is used to finish the session at application end.
 */
@Controller
public class SessionCloseWrapper extends WebServiceGatewaySupport {

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Autowired
    private SessionCloseInterceptor sessionCloseInterceptor;

    @Autowired
    private SacsConfiguration configuration;

    @Autowired
    private SessionCloseHeaderCallback sessionCloseHeaderComposingCallback;

    @Autowired
    private Jaxb2Marshaller marshaller;

    private List<ClientInterceptor> interceptors = new ArrayList<>();

    @PostConstruct
    private void addInterceptors() {
        interceptors.add(loggingInterceptor);
        interceptors.add(sessionCloseInterceptor);

        this.setInterceptors(interceptors.toArray(new ClientInterceptor[0]));
        setDefaultUri(configuration.getSoapProperty("environment"));
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
    }

    private SessionCloseRQ getRequestBody() {

        SessionCloseRQ requestBody = new SessionCloseRQ();

        SessionCloseRQ.POS pos = new SessionCloseRQ.POS();
        SessionCloseRQ.POS.Source source = new SessionCloseRQ.POS.Source();
        source.setPseudoCityCode(configuration.getSoapProperty("group"));
        pos.setSource(source);
        requestBody.setPOS(pos);

        return requestBody;
    }

    public SessionCloseRS closeSession(Security session) {

        sessionCloseHeaderComposingCallback.setSession(session);
        return (SessionCloseRS) getWebServiceTemplate().marshalSendAndReceive(
                getRequestBody(),
                sessionCloseHeaderComposingCallback
                );
    }

}

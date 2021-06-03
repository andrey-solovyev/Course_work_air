package org.example.request.session;


import org.example.configuration.SacsConfiguration;
import org.example.contractAir.soap.Security;
import org.example.contractAir.transaction.IgnoreTransactionRQ;
import org.example.contractAir.transaction.IgnoreTransactionRS;
import org.example.request.callback.IgnoreTransactionHeaderCallback;
import org.example.request.interceptor.FaultInterceptor;
import org.example.request.interceptor.LoggingInterceptor;
import org.example.workflow.SharedContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper used to run the IgnoreTransacton call, which cleans up the session.
 */
@Controller
public class IgnoreTransactionWrapper extends WebServiceGatewaySupport {

    @Autowired
    private SacsConfiguration configuration;

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Autowired
    private FaultInterceptor faultInterceptor;

    @Autowired
    private IgnoreTransactionHeaderCallback headerCallback;

    private List<ClientInterceptor> interceptors = new ArrayList<>();

    @PostConstruct
    private void addInterceptors() {
        interceptors.add(loggingInterceptor);
        interceptors.add(faultInterceptor);

        this.setInterceptors(interceptors.toArray(new ClientInterceptor[0]));
        setDefaultUri(configuration.getSoapProperty("environment"));
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("org.example.contractAir.transaction");
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

    }

    public IgnoreTransactionRS executeRequest(Security security, SharedContext context) {

        headerCallback.setWorkflowContext(context);
        headerCallback.setSessionToIgnore(security);
        return (IgnoreTransactionRS) getWebServiceTemplate().marshalSendAndReceive(
                getRequestBody(),
                headerCallback
                );

    }

    private IgnoreTransactionRQ getRequestBody() {

        IgnoreTransactionRQ request = new IgnoreTransactionRQ();
        request.setVersion(configuration.getSoapProperty("IgnoreTransactionLLSRQVersion"));
        request.setReturnHostCommand(true);
        return request;
    }

}

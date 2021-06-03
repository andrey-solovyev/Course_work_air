package org.example.request.wrapper;


import org.example.configuration.SacsConfiguration;
import org.example.contractAir.passengerdetails.PassengerDetailsRQ;
import org.example.contractAir.passengerdetails.PassengerDetailsRS;
import org.example.request.callback.HeaderCallback;
import org.example.request.callback.HeaderComposingCallback;
import org.example.request.common.GenericRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import java.util.List;

/**
 * Wrapper class implementing the {@link GenericRequestWrapper}, configuring it to call
 * the PassengerDetails service.
 */
@Controller
@Scope("prototype")
public class PassengerDetailsWrapper extends GenericRequestWrapper<PassengerDetailsRQ, PassengerDetailsRS> {

    
    @Autowired
    private SacsConfiguration configuration;

    @Autowired
    @Qualifier("passengerDetailsHeaderComposingCallback")
    private HeaderComposingCallback headerCallback;



    @Override
    protected List<ClientInterceptor> interceptors() {
        return null;
    }

    @Override
    protected Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("org.example.contractAir.passengerdetails");
        return marshaller;
    }

    @Override
    protected HeaderCallback callback() {
        return headerCallback;
    }

}

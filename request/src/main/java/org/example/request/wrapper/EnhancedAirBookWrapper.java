package org.example.request.wrapper;


import org.example.contractAir.enhancedairbook.EnhancedAirBookRQ;
import org.example.contractAir.enhancedairbook.EnhancedAirBookRS;
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

@Controller
@Scope("prototype")
public class EnhancedAirBookWrapper extends GenericRequestWrapper<EnhancedAirBookRQ, EnhancedAirBookRS> {

    @Autowired
    @Qualifier("enhancedAirBookHeaderComposingCallback")
    private HeaderComposingCallback headerCallback;



    @Override
    protected List<ClientInterceptor> interceptors() {
        return null;
    }

    @Override
    protected Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("org.example.contractAir.enhancedairbook");
        return marshaller;
    }

    @Override
    protected HeaderCallback callback() {
        return headerCallback;
    }


}

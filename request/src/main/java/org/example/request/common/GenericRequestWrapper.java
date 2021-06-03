package org.example.request.common;


import com.sabre.api.sacs.errors.ErrorHandlingSchedule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.configuration.SacsConfiguration;
import org.example.request.callback.HeaderCallback;
import org.example.request.callback.HeaderComposingCallback;
import org.example.request.interceptor.FaultInterceptor;
import org.example.request.interceptor.LoggingInterceptor;
import org.example.request.interceptor.SessionPoolInterceptor;
import org.example.workflow.SharedContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.client.SoapFaultClientException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The class to be implemented to create a SOAP call.
 * @param <RQ> Request class
 * @param <RS> Response class
 */
@Controller
@Scope("prototype")
public abstract class GenericRequestWrapper<RQ, RS> extends WebServiceGatewaySupport {
    
    private static final Logger LOG = LogManager.getLogger("GenericRequestWrapper -> ");
    
    @Autowired
    private SacsConfiguration configuration;
    
    @Autowired
    private ErrorHandlingSchedule errorHandler;

    @Autowired
    private FaultInterceptor faultInterceptor;

    @Autowired
    private SessionPoolInterceptor sessionPoolInterceptor;

    @Autowired
    private LoggingInterceptor loggingInterceptor;
    
    private RQ request;
    
    private boolean lastInFlow;
    
    private BlockingQueue<RS> resultLock = new LinkedBlockingQueue<>();

    public void setRequest(RQ request) {
        this.request = request;
    }

    protected abstract List<ClientInterceptor> interceptors();
    

    protected abstract Jaxb2Marshaller marshaller();
    

    protected abstract HeaderCallback callback();

    public void setLastInFlow(boolean lastInFlow) {
        this.lastInFlow = lastInFlow;
    }
    
    @PostConstruct
    private void init() {
        List<ClientInterceptor> interceptors = interceptors();
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(faultInterceptor);
        interceptors.add(loggingInterceptor);
        this.setInterceptors(interceptors.toArray(new ClientInterceptor[0]));
        this.setDefaultUri(configuration.getSoapProperty("environment"));
        this.setMarshaller(marshaller());
        this.setUnmarshaller(marshaller());
    }

    public RS executeRequest(SharedContext workflowContext) throws InterruptedException {
        new Thread(new SoapCallTask(workflowContext)).start();
        return resultLock.take();
    }
    
    private class SoapCallTask implements Runnable {

        private SharedContext workflowContext;
        
        SoapCallTask(SharedContext workflowContext) {
            this.workflowContext = workflowContext;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            RS result = null;
            

            if (lastInFlow) {
                List<ClientInterceptor> interceptors = new ArrayList<>();
                interceptors.addAll(Arrays.asList(getInterceptors()));
                interceptors.add(sessionPoolInterceptor);
                setInterceptors(interceptors.toArray(new ClientInterceptor[0]));
            }
            callback().setWorkflowContext(workflowContext);
            boolean isFault = false;
            try {
                result = (RS) getWebServiceTemplate().marshalSendAndReceive(
                    request,
                    callback()
                    );
            } catch (SoapFaultClientException e) {
                isFault = true;
                workflowContext.setFaulty(true);
                LOG.catching(e);
            }
            if (!isFault) {
                LOG.info("Request succeeded (y)");
            } else {
                errorHandler.addSystemFailure(workflowContext);
            }
            resultLock.offer(result);
        }
        
    }

}

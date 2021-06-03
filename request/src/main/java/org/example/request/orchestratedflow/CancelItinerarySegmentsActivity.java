package org.example.request.orchestratedflow;

import com.sabre.api.sacs.errors.ErrorHandlingSchedule;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.configuration.SacsConfiguration;
import org.example.request.common.GenericRequestWrapper;
import org.example.contractAir.cancelitinerarysegments.OTACancelRQ;
import org.example.contractAir.cancelitinerarysegments.OTACancelRS;
import org.example.request.pool.SessionPool;
import org.example.workflow.Activity;
import org.example.workflow.SharedContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@Data
public class CancelItinerarySegmentsActivity  implements Activity {
    private static final Logger LOG = LogManager.getLogger(BargainFinderMaxSoapActivity.class);

    private final GenericRequestWrapper<OTACancelRQ, OTACancelRS> cns;

    private final ErrorHandlingSchedule errorHandler;

    private final SacsConfiguration config;

    private final SessionPool sessionPool;

    @Override
    public void run(SharedContext context) {

    }
}

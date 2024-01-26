package com.client.fault.tolerance.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

@RegisterRestClient(baseUri = "http://localhost:8081/api/slow")
public interface ApiToleranceClient {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Timeout(unit = ChronoUnit.SECONDS, value = 2)
  @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1, retryOn = TimeoutException.class)
  @Fallback(fallbackMethod = "defaultFallback")
    @CircuitBreaker(delayUnit = ChronoUnit.SECONDS, requestVolumeThreshold = 4,
                                                  failureRatio = .75, delay = 3,
                                                  successThreshold = 2
  )
  String getDataFromSlowAPI();

  default String defaultFallback() {
    return "It seems that the server is down! We need to define a better response to the final user \n";
  }

}

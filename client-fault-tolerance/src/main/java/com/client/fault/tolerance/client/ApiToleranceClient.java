package com.client.fault.tolerance.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.faulttolerance.*;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

@RegisterRestClient(baseUri = "http://localhost:8081/api/slow")
public interface ApiToleranceClient {

  // Constants for configuration
  int TIMEOUT_VALUE = 2;
  int MAX_RETRIES = 2;
  int RETRY_DELAY = 1;
  int REQUEST_VOLUME_THRESHOLD = 4;
  int CIRCUIT_BREAKER_DELAY = 3;
  int SUCCESS_THRESHOLD = 2;
  double FAILURE_RATIO = .75;

  /**
   * This method retrieves data from a slow API with fault tolerance mechanisms.
   * <p>
   * It has a timeout of TIMEOUT_VALUE seconds, will retry MAX_RETRIES times on a TimeoutException,
   * and uses a circuit breaker pattern with a request volume threshold of REQUEST_VOLUME_THRESHOLD,
   * a failure ratio of FAILURE_RATIO, a delay of CIRCUIT_BREAKER_DELAY, and a success threshold of SUCCESS_THRESHOLD.
   * If all retries fail, it falls back to the defaultFallback method.
   *
   * @return The data from the slow API as a String, or a fallback message if the API is unavailable.
   */
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Timeout(unit = ChronoUnit.SECONDS, value = TIMEOUT_VALUE)
  @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = MAX_RETRIES,
                                         delay = RETRY_DELAY,
                                         retryOn = TimeoutException.class)
  @Fallback(fallbackMethod = "defaultFallback")
  @CircuitBreaker(delayUnit = ChronoUnit.SECONDS, requestVolumeThreshold = REQUEST_VOLUME_THRESHOLD,
                                                  failureRatio = FAILURE_RATIO, delay = CIRCUIT_BREAKER_DELAY,
                                                  successThreshold = SUCCESS_THRESHOLD)
  String retrieveData();

  /**
   * This method provides a default response when the retrieveData method fails to get data from the slow API.
   * @return A default error message as a String.
   */
  default String defaultFallback() {
    // Log the error here
    return "The server is currently unavailable. Please try again later.";
  }
}
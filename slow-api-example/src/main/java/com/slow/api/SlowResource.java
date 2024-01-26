package com.slow.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/slow")
public class SlowResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() throws InterruptedException {
        Thread.sleep(3000L);
        return "Hi there, I'm a slow API \n";
    }
}

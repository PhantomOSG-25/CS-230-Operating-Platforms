package com.gamingroom.gameauth.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

@Path("/api/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {
  @GET
  public Map<String, String> status() {
    return Map.of("service", "gameauth", "status", "ready");
  }
}

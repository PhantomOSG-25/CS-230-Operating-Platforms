package com.gamingroom.gameauth.auth;

import io.dropwizard.auth.Authorizer;
import jakarta.ws.rs.container.ContainerRequestContext;

public class RoleAuthorizer implements Authorizer<GamePrincipal> {
  @Override
  public boolean authorize(
      GamePrincipal principal, String role, ContainerRequestContext requestContext) {
    return principal.roles().contains(role);
  }
}

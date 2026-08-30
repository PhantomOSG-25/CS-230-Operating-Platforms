package com.gamingroom.gameauth.auth;

import java.security.Principal;
import java.util.Set;

public record GamePrincipal(String name, Set<String> roles) implements Principal {
  public GamePrincipal {
    roles = Set.copyOf(roles);
  }

  @Override
  public String getName() {
    return name;
  }
}

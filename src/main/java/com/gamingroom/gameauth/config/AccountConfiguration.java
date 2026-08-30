package com.gamingroom.gameauth.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

public class AccountConfiguration {
  @NotBlank private String username;
  @NotBlank private String password;
  @NotEmpty private Set<String> roles = new HashSet<>();

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<String> getRoles() {
    return Set.copyOf(roles);
  }

  public void setRoles(Set<String> roles) {
    this.roles = new HashSet<>(roles);
  }
}

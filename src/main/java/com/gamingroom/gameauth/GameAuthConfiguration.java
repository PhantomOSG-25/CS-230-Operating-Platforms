package com.gamingroom.gameauth;

import com.gamingroom.gameauth.config.AccountConfiguration;
import com.gamingroom.gameauth.config.SeedUserConfiguration;
import io.dropwizard.core.Configuration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class GameAuthConfiguration extends Configuration {
  @Valid @NotEmpty private List<AccountConfiguration> accounts = new ArrayList<>();
  @Valid private List<SeedUserConfiguration> seedUsers = new ArrayList<>();

  public List<AccountConfiguration> getAccounts() {
    return List.copyOf(accounts);
  }

  public void setAccounts(List<AccountConfiguration> accounts) {
    this.accounts = new ArrayList<>(accounts);
  }

  public List<SeedUserConfiguration> getSeedUsers() {
    return List.copyOf(seedUsers);
  }

  public void setSeedUsers(List<SeedUserConfiguration> seedUsers) {
    this.seedUsers = new ArrayList<>(seedUsers);
  }
}

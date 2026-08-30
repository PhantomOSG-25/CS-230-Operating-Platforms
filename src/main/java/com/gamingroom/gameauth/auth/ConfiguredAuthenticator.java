package com.gamingroom.gameauth.auth;

import com.gamingroom.gameauth.config.AccountConfiguration;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConfiguredAuthenticator implements Authenticator<BasicCredentials, GamePrincipal> {
  private final Map<String, AccountConfiguration> accounts = new HashMap<>();

  public ConfiguredAuthenticator(List<AccountConfiguration> configuredAccounts) {
    configuredAccounts.forEach(account -> accounts.put(account.getUsername(), account));
  }

  @Override
  public Optional<GamePrincipal> authenticate(BasicCredentials credentials) {
    AccountConfiguration account = accounts.get(credentials.getUsername());
    if (account == null || !equal(account.getPassword(), credentials.getPassword()))
      return Optional.empty();
    return Optional.of(new GamePrincipal(account.getUsername(), account.getRoles()));
  }

  private static boolean equal(String expected, String supplied) {
    return MessageDigest.isEqual(
        expected.getBytes(StandardCharsets.UTF_8), supplied.getBytes(StandardCharsets.UTF_8));
  }
}

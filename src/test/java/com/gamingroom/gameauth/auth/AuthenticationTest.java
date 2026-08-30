package com.gamingroom.gameauth.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.gamingroom.gameauth.config.AccountConfiguration;
import io.dropwizard.auth.basic.BasicCredentials;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AuthenticationTest {
  @Test
  void authenticatesConfiguredCredentialsAndPreservesRoles() throws Exception {
    AccountConfiguration account = account("reviewer", "correct", Set.of("USER"));
    var principal =
        new ConfiguredAuthenticator(List.of(account))
            .authenticate(new BasicCredentials("reviewer", "correct"));
    assertThat(principal)
        .get()
        .satisfies(
            user -> {
              assertThat(user.getName()).isEqualTo("reviewer");
              assertThat(user.roles()).containsExactly("USER");
            });
  }

  @Test
  void rejectsUnknownUsersAndWrongPasswords() throws Exception {
    var authenticator =
        new ConfiguredAuthenticator(List.of(account("admin", "correct", Set.of("ADMIN"))));
    assertThat(authenticator.authenticate(new BasicCredentials("admin", "wrong"))).isEmpty();
    assertThat(authenticator.authenticate(new BasicCredentials("unknown", "correct"))).isEmpty();
  }

  @Test
  void authorizesOnlyAssignedRoles() {
    var authorizer = new RoleAuthorizer();
    var principal = new GamePrincipal("reviewer", Set.of("USER"));
    assertThat(authorizer.authorize(principal, "USER", null)).isTrue();
    assertThat(authorizer.authorize(principal, "ADMIN", null)).isFalse();
  }

  private static AccountConfiguration account(String username, String password, Set<String> roles) {
    AccountConfiguration account = new AccountConfiguration();
    account.setUsername(username);
    account.setPassword(password);
    account.setRoles(roles);
    return account;
  }
}

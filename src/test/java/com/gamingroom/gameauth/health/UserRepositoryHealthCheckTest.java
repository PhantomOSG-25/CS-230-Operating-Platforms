package com.gamingroom.gameauth.health;

import static org.assertj.core.api.Assertions.assertThat;

import com.gamingroom.gameauth.repository.InMemoryGameUserRepository;
import org.junit.jupiter.api.Test;

class UserRepositoryHealthCheckTest {
  @Test
  void reportsAnAccessibleRepositoryAsHealthy() throws Exception {
    assertThat(
            new UserRepositoryHealthCheck(new InMemoryGameUserRepository()).execute().isHealthy())
        .isTrue();
  }
}

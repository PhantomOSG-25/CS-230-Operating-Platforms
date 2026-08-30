package com.gamingroom.gameauth.health;

import com.codahale.metrics.health.HealthCheck;
import com.gamingroom.gameauth.repository.GameUserRepository;

public class UserRepositoryHealthCheck extends HealthCheck {
  private final GameUserRepository repository;

  public UserRepositoryHealthCheck(GameUserRepository repository) {
    this.repository = repository;
  }

  @Override
  protected Result check() {
    long count = repository.count();
    return count >= 0
        ? Result.healthy("repository accessible; users=%d", count)
        : Result.unhealthy("invalid repository count");
  }
}

package com.gamingroom.gameauth;

import com.gamingroom.gameauth.auth.ConfiguredAuthenticator;
import com.gamingroom.gameauth.auth.GamePrincipal;
import com.gamingroom.gameauth.auth.RoleAuthorizer;
import com.gamingroom.gameauth.config.SeedUserConfiguration;
import com.gamingroom.gameauth.health.UserRepositoryHealthCheck;
import com.gamingroom.gameauth.model.CreateGameUserRequest;
import com.gamingroom.gameauth.repository.GameUserRepository;
import com.gamingroom.gameauth.repository.InMemoryGameUserRepository;
import com.gamingroom.gameauth.resource.GameUserResource;
import com.gamingroom.gameauth.resource.StatusResource;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class GameAuthApplication extends Application<GameAuthConfiguration> {
  public static void main(String[] args) throws Exception {
    new GameAuthApplication().run(args);
  }

  @Override
  public String getName() {
    return "gameauth-platform-service";
  }

  @Override
  public void initialize(Bootstrap<GameAuthConfiguration> bootstrap) {
    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(
            bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
  }

  @Override
  public void run(GameAuthConfiguration configuration, Environment environment) {
    GameUserRepository repository = new InMemoryGameUserRepository();
    for (SeedUserConfiguration seed : configuration.getSeedUsers()) {
      repository.create(
          new CreateGameUserRequest(seed.getFirstName(), seed.getLastName(), seed.getEmail()));
    }

    environment
        .jersey()
        .register(
            new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<GamePrincipal>()
                    .setAuthenticator(new ConfiguredAuthenticator(configuration.getAccounts()))
                    .setAuthorizer(new RoleAuthorizer())
                    .setRealm("GAMEAUTH")
                    .buildAuthFilter()));
    environment.jersey().register(new AuthValueFactoryProvider.Binder<>(GamePrincipal.class));
    environment.jersey().register(RolesAllowedDynamicFeature.class);
    environment.jersey().register(new StatusResource());
    environment.jersey().register(new GameUserResource(repository));
    environment
        .healthChecks()
        .register("user-repository", new UserRepositoryHealthCheck(repository));
  }
}

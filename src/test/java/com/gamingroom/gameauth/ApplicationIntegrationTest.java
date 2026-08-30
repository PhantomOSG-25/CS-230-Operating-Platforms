package com.gamingroom.gameauth;

import static org.assertj.core.api.Assertions.assertThat;

import com.gamingroom.gameauth.model.CreateGameUserRequest;
import com.gamingroom.gameauth.model.GameUserRecord;
import com.gamingroom.gameauth.model.UpdateGameUserRequest;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class ApplicationIntegrationTest {
  private static final DropwizardAppExtension<GameAuthConfiguration> APP =
      new DropwizardAppExtension<>(
          GameAuthApplication.class, ResourceHelpers.resourceFilePath("test-config.yml"));

  @Test
  void enforcesRolesAndSupportsTheHttpLifecycle() {
    String base = "http://localhost:" + APP.getLocalPort();
    @SuppressWarnings("unchecked")
    Map<String, String> status =
        (Map<String, String>)
            (Map<?, ?>) APP.client().target(base + "/api/status").request().get(Map.class);
    assertThat(status).containsEntry("status", "ready");

    try (Response anonymous = APP.client().target(base + "/api/users").request().get()) {
      assertThat(anonymous.getStatus()).isEqualTo(401);
    }
    try (Response forbidden =
        APP.client()
            .target(base + "/api/users")
            .request()
            .header(HttpHeaders.AUTHORIZATION, basic("reviewer", "test-reviewer-password"))
            .get()) {
      assertThat(forbidden.getStatus()).isEqualTo(403);
    }

    GameUserRecord created;
    try (Response response =
        APP.client()
            .target(base + "/api/users")
            .request()
            .header(HttpHeaders.AUTHORIZATION, basic("admin", "test-admin-password"))
            .post(
                Entity.entity(
                    new CreateGameUserRequest("Jordan", "Lee", "jordan.lee@example.invalid"),
                    MediaType.APPLICATION_JSON_TYPE))) {
      assertThat(response.getStatus()).isEqualTo(201);
      created = response.readEntity(GameUserRecord.class);
    }
    var userTarget = APP.client().target(base + "/api/users/" + created.id());
    try (Response found =
        userTarget
            .request()
            .header(HttpHeaders.AUTHORIZATION, basic("reviewer", "test-reviewer-password"))
            .get()) {
      assertThat(found.getStatus()).isEqualTo(200);
    }
    try (Response updated =
        userTarget
            .request()
            .header(HttpHeaders.AUTHORIZATION, basic("admin", "test-admin-password"))
            .put(
                Entity.entity(
                    new UpdateGameUserRequest("Jordan", "Stone", "jordan.stone@example.invalid"),
                    MediaType.APPLICATION_JSON_TYPE))) {
      assertThat(updated.readEntity(GameUserRecord.class).lastName()).isEqualTo("Stone");
    }
    try (Response deleted =
        userTarget
            .request()
            .header(HttpHeaders.AUTHORIZATION, basic("admin", "test-admin-password"))
            .delete()) {
      assertThat(deleted.getStatus()).isEqualTo(204);
    }
    try (Response missing =
        userTarget
            .request()
            .header(HttpHeaders.AUTHORIZATION, basic("reviewer", "test-reviewer-password"))
            .get()) {
      assertThat(missing.getStatus()).isEqualTo(404);
    }
  }

  private static String basic(String username, String password) {
    return "Basic "
        + Base64.getEncoder()
            .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
  }
}

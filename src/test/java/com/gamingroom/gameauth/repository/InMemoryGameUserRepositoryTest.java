package com.gamingroom.gameauth.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gamingroom.gameauth.model.CreateGameUserRequest;
import com.gamingroom.gameauth.model.UpdateGameUserRequest;
import org.junit.jupiter.api.Test;

class InMemoryGameUserRepositoryTest {
  private final InMemoryGameUserRepository repository = new InMemoryGameUserRepository();

  @Test
  void supportsTheCompleteLifecycleWithStableIds() {
    var first =
        repository.create(new CreateGameUserRequest("Avery", "Morgan", "avery@example.invalid"));
    var second =
        repository.create(new CreateGameUserRequest("Jordan", "Lee", "jordan@example.invalid"));
    assertThat(first.id()).isEqualTo(1);
    assertThat(second.id()).isEqualTo(2);
    assertThat(repository.findAll()).extracting("id").containsExactly(1L, 2L);

    var updated =
        repository.update(
            first.id(), new UpdateGameUserRequest("Avery", "Stone", "avery.stone@example.invalid"));
    assertThat(updated).get().extracting("lastName").isEqualTo("Stone");
    assertThat(repository.delete(first.id())).isTrue();
    assertThat(repository.delete(first.id())).isFalse();
    assertThat(repository.findById(first.id())).isEmpty();
    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  void rejectsDuplicateEmailsWithoutRegardToCase() {
    repository.create(new CreateGameUserRequest("Avery", "Morgan", "avery@example.invalid"));
    assertThatThrownBy(
            () ->
                repository.create(
                    new CreateGameUserRequest("Other", "User", "AVERY@example.invalid")))
        .isInstanceOf(DuplicateEmailException.class);
  }

  @Test
  void returnsEmptyWhenUpdatingAnUnknownUser() {
    assertThat(
            repository.update(99, new UpdateGameUserRequest("No", "User", "none@example.invalid")))
        .isEmpty();
  }
}

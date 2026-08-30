package com.gamingroom.gameauth.repository;

import com.gamingroom.gameauth.model.CreateGameUserRequest;
import com.gamingroom.gameauth.model.GameUserRecord;
import com.gamingroom.gameauth.model.UpdateGameUserRequest;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryGameUserRepository implements GameUserRepository {
  private final ConcurrentHashMap<Long, GameUserRecord> users = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong(1);

  @Override
  public List<GameUserRecord> findAll() {
    return users.values().stream().sorted((a, b) -> Long.compare(a.id(), b.id())).toList();
  }

  @Override
  public Optional<GameUserRecord> findById(long id) {
    return Optional.ofNullable(users.get(id));
  }

  @Override
  public synchronized GameUserRecord create(CreateGameUserRequest request) {
    rejectDuplicate(request.email(), null);
    long id = nextId.getAndIncrement();
    GameUserRecord user =
        new GameUserRecord(id, request.firstName(), request.lastName(), request.email());
    users.put(id, user);
    return user;
  }

  @Override
  public synchronized Optional<GameUserRecord> update(long id, UpdateGameUserRequest request) {
    if (!users.containsKey(id)) return Optional.empty();
    rejectDuplicate(request.email(), id);
    GameUserRecord user =
        new GameUserRecord(id, request.firstName(), request.lastName(), request.email());
    users.put(id, user);
    return Optional.of(user);
  }

  @Override
  public boolean delete(long id) {
    return users.remove(id) != null;
  }

  @Override
  public long count() {
    return users.size();
  }

  private void rejectDuplicate(String email, Long allowedId) {
    String normalized = email.toLowerCase(Locale.ROOT);
    boolean duplicate =
        users.values().stream()
            .anyMatch(
                user ->
                    user.email().toLowerCase(Locale.ROOT).equals(normalized)
                        && !Long.valueOf(user.id()).equals(allowedId));
    if (duplicate) throw new DuplicateEmailException(email);
  }
}

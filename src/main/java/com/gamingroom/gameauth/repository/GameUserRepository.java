package com.gamingroom.gameauth.repository;

import com.gamingroom.gameauth.model.CreateGameUserRequest;
import com.gamingroom.gameauth.model.GameUserRecord;
import com.gamingroom.gameauth.model.UpdateGameUserRequest;
import java.util.List;
import java.util.Optional;

public interface GameUserRepository {
  List<GameUserRecord> findAll();

  Optional<GameUserRecord> findById(long id);

  GameUserRecord create(CreateGameUserRequest request);

  Optional<GameUserRecord> update(long id, UpdateGameUserRequest request);

  boolean delete(long id);

  long count();
}

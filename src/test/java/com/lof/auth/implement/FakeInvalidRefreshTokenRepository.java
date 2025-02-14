package com.lof.auth.implement;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.lof.auth.domain.InvalidRefreshToken;
import com.lof.auth.repository.InvalidRefreshTokenRepository;

public class FakeInvalidRefreshTokenRepository implements InvalidRefreshTokenRepository {

    private final Map<String, InvalidRefreshToken> memory = new HashMap<>();

    @Override
    public <S extends InvalidRefreshToken> S save(S entity) {
        return (S) memory.put(entity.getId(), entity);
    }

    @Override
    public Optional<InvalidRefreshToken> findById(String s) {
        return Optional.ofNullable(memory.get(s));
    }

    /*
    아래 메서들은 아직 사용하지 않음. 사용할 때 구현할 것
     */
    @Override
    public <S extends InvalidRefreshToken> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<InvalidRefreshToken> findAll() {
        return null;
    }

    @Override
    public Iterable<InvalidRefreshToken> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(InvalidRefreshToken entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends InvalidRefreshToken> entities) {

    }

    @Override
    public void deleteAll() {

    }
}

package com.lof.auth.implement;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.lof.auth.domain.ValidRefreshToken;
import com.lof.auth.repository.ValidRefreshTokenRepository;

public class FakeValidRefreshTokenRepository implements ValidRefreshTokenRepository {

    private final Map<Long, ValidRefreshToken> memory = new HashMap<>();

    @Override
    public <S extends ValidRefreshToken> S save(S entity) {
        return (S) memory.put(entity.getId(), entity);
    }

    @Override
    public Optional<ValidRefreshToken> findById(Long aLong) {
        return Optional.ofNullable(memory.get(aLong));
    }

    @Override
    public void deleteById(Long aLong) {
        memory.remove(aLong);
    }

    /*
    아래 메서들은 아직 사용하지 않음. 사용할 때 구현할 것
     */
    @Override
    public <S extends ValidRefreshToken> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<ValidRefreshToken> findAll() {
        return null;
    }

    @Override
    public Iterable<ValidRefreshToken> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(ValidRefreshToken entity) {
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
    }

    @Override
    public void deleteAll(Iterable<? extends ValidRefreshToken> entities) {
    }

    @Override
    public void deleteAll() {
    }
}

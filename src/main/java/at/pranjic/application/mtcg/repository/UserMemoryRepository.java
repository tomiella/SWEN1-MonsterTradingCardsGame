package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.User;

import java.util.*;

public class UserMemoryRepository implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public void update(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getUsername());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}

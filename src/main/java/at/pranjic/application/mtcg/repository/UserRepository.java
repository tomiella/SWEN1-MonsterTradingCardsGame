package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public Optional<User> findByUsername(String username);
    public void save(User user);
    public void update(User user);
    public void delete(User user);
    public List<User> findAll();
}

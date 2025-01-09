package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    void update(User user);
    void delete(Long id);
    void addCardToUser(long userId, String cardId);
}

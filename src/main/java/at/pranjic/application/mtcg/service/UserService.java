package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.dto.UserDTO;
import at.pranjic.application.mtcg.entity.User;
import at.pranjic.application.mtcg.repository.UserMemoryRepository;
import at.pranjic.application.mtcg.repository.UserRepository;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;

import java.util.Optional;

public class UserService {
    private UserRepository userRepository = new UserMemoryRepository();

    public boolean registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public Optional<String> loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            // Generate and return token (token generation not shown here)
            String token = "%s-mtcgToken".formatted(username);
            return Optional.of(token);
        }
        return Optional.empty();
    }

    public Optional<UserDTO> getUser(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return Optional.of(new UserDTO(username, userOpt.get()));
        }
        return Optional.empty();
    }

    public boolean updateUser(String username, UserDTO newUser) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(newUser.getName());
            user.setBio(newUser.getBio());
            user.setImage(newUser.getImage());
            userRepository.update(user);
            return true;
        }
        return false;
    }

    public boolean checkAuth(String username, String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }

        return token.equals("Bearer %s-mtcgToken".formatted(username));
    }

    public boolean isAdmin(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }

        return token.equals("Bearer %s-mtcgToken".formatted("admin"));
    }
}

package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.dto.UserDTO;
import at.pranjic.application.mtcg.entity.User;
import at.pranjic.application.mtcg.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testRegisterUser_Success() {
        User newUser = new User("testUser", "password123", "image", "bio");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        boolean result = userService.registerUser(newUser);

        assertTrue(result);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testRegisterUser_Failure_UserAlreadyExists() {
        User existingUser = new User("existingUser", "password123", "bio", "image");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(existingUser));

        boolean result = userService.registerUser(existingUser);

        assertFalse(result);
        verify(userRepository, never()).save(existingUser);
    }

    @Test
    void testLoginUser_Success() {
        User existingUser = new User("testUser", "password123", "bio", "image");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(existingUser));

        Optional<String> token = userService.loginUser("testUser", "password123");

        assertTrue(token.isPresent());
        assertEquals("testUser-mtcgToken", token.get());
    }

    @Test
    void testLoginUser_Failure_IncorrectPassword() {
        User existingUser = new User("testUser", "password123", "bio", "image");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(existingUser));

        Optional<String> token = userService.loginUser("testUser", "wrongPassword");

        assertFalse(token.isPresent());
    }

    @Test
    void testLoginUser_Failure_UserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        Optional<String> token = userService.loginUser("nonExistentUser", "password");

        assertFalse(token.isPresent());
    }

    @Test
    void testGetUser_Success() {
        User existingUser = new User("testUser", "password123", "bio", "image");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(existingUser));

        Optional<UserDTO> userDTO = userService.getUser("testUser");

        assertTrue(userDTO.isPresent());
        assertEquals("testUser", userDTO.get().getUsername());
    }

    @Test
    void testGetUser_Failure_UserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        Optional<UserDTO> userDTO = userService.getUser("nonExistentUser");

        assertFalse(userDTO.isPresent());
    }

    @Test
    void testUpdateUser_Success() {
        User existingUser = new User("testUser", "password123", "old bio", "old image");
        UserDTO updatedUserDTO = new UserDTO("testUser", "new bio", "new image");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(existingUser));

        boolean result = userService.updateUser("testUser", updatedUserDTO);

        assertTrue(result);
        assertEquals("new bio", existingUser.getBio());
        assertEquals("new image", existingUser.getImage());
        verify(userRepository, times(1)).update(existingUser);
    }

    @Test
    void testUpdateUser_Failure_UserNotFound() {
        UserDTO updatedUserDTO = new UserDTO("testUser", "new bio", "new image");

        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        boolean result = userService.updateUser("nonExistentUser", updatedUserDTO);

        assertFalse(result);
        verify(userRepository, never()).update(any());
    }

    @Test
    void testCheckAuth_ValidToken() {
        String token = "Bearer testUser-mtcgToken";
        assertTrue(UserService.checkAuth("testUser", token));
    }

    @Test
    void testCheckAuth_InvalidToken() {
        String token = "InvalidToken";
        assertFalse(UserService.checkAuth("testUser", token));
    }

    @Test
    void testCheckAuth_EmptyToken() {
        assertFalse(UserService.checkAuth("testUser", null));
    }

    @Test
    void testIsAdmin_ValidAdminToken() {
        String token = "Bearer admin-mtcgToken";
        assertTrue(userService.isAdmin(token));
    }

    @Test
    void testIsAdmin_InvalidToken() {
        String token = "Bearer user-mtcgToken";
        assertFalse(userService.isAdmin(token));
    }

    @Test
    void testIsAdmin_EmptyToken() {
        assertFalse(userService.isAdmin(null));
    }
}

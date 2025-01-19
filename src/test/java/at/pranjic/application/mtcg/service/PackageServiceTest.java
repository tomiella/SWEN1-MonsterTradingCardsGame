package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.entity.CardInfo;
import at.pranjic.application.mtcg.entity.Package;
import at.pranjic.application.mtcg.entity.User;
import at.pranjic.application.mtcg.exceptions.NoPackagesAvailableException;
import at.pranjic.application.mtcg.exceptions.NotEnoughMoneyException;
import at.pranjic.application.mtcg.repository.PackageRepository;
import at.pranjic.application.mtcg.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PackageServiceTest {
    private PackageRepository packageRepository;
    private UserRepository userRepository;
    private PackageService packageService;

    @BeforeEach
    void setUp() {
        packageRepository = mock(PackageRepository.class);
        userRepository = mock(UserRepository.class);
        packageService = new PackageService(packageRepository, userRepository);
    }

    @Test
    void testAddPackage_Success() {
        Package pkg = new Package(1);
        List<String> cardIds = List.of("card1", "card2", "card3", "card4", "card5");

        packageService.addPackage(pkg, cardIds);

        verify(packageRepository, times(1)).save(pkg, cardIds);
    }

    @Test
    void testAddPackage_Failure_InvalidCardCount() {
        Package pkg = new Package(1);
        List<String> cardIds = List.of("card1", "card2");  // Only 2 cards

        Exception exception = assertThrows(IllegalArgumentException.class, () -> packageService.addPackage(pkg, cardIds));

        assertEquals("A package must contain exactly 5 cards.", exception.getMessage());
        verify(packageRepository, never()).save(any(), any());
    }

    @Test
    void testAcquirePackage_Success() {
        User user = new User(1, "testUser", "password");
        Package pkg = new Package(1);
        Card card1 = new Card();
        card1.setId("1");
        card1.setInfo(CardInfo.DRAGON);
        card1.setDamage(50);

        Card card2 = new Card();
        card2.setId("2");
        card2.setInfo(CardInfo.FIRE_GOBLIN);
        card2.setDamage(40);
        List<Card> cards = List.of(card1, card2);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(packageRepository.findAll()).thenReturn(List.of(pkg));
        when(packageRepository.getCardsInPackage(pkg.getId())).thenReturn(cards);

        packageService.acquirePackage("testUser");

        assertEquals(15, user.getCoins());
        verify(userRepository, times(1)).update(user);
        verify(userRepository, times(1)).addCardToUser(user.getId(), "1");
        verify(userRepository, times(1)).addCardToUser(user.getId(), "2");
        verify(packageRepository, times(1)).delete(pkg.getId());
    }

    @Test
    void testAcquirePackage_Failure_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> packageService.acquirePackage("unknownUser"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testAcquirePackage_Failure_NotEnoughMoney() {
        User user = new User(1, "testUser", "password");
        user.setCoins(3);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        Exception exception = assertThrows(NotEnoughMoneyException.class, () -> packageService.acquirePackage("testUser"));

        assertEquals("Not enough money", exception.getMessage());
    }

    @Test
    void testAcquirePackage_Failure_NoPackagesAvailable() {
        User user = new User(1, "testUser", "password");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(packageRepository.findAll()).thenReturn(List.of());

        Exception exception = assertThrows(NoPackagesAvailableException.class, () -> packageService.acquirePackage("testUser"));

        assertEquals("No packages available", exception.getMessage());
    }

    @Test
    void testGetPackageById_Success() {
        Package pkg = new Package(1);

        when(packageRepository.findById(1)).thenReturn(Optional.of(pkg));

        Optional<Package> result = packageService.getPackageById(1);

        assertTrue(result.isPresent());
        assertEquals(pkg, result.get());
    }

    @Test
    void testGetPackageById_NotFound() {
        when(packageRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Package> result = packageService.getPackageById(1);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllPackages() {
        List<Package> packages = List.of(new Package(1), new Package(2));

        when(packageRepository.findAll()).thenReturn(packages);

        List<Package> result = packageService.getAllPackages();

        assertEquals(2, result.size());
    }

    @Test
    void testDeletePackage_Success() {
        packageService.deletePackage(1);

        verify(packageRepository, times(1)).delete(1);
    }
}

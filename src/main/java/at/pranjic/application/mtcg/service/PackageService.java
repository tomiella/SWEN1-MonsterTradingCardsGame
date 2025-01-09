package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.repository.PackageRepository;
import at.pranjic.application.mtcg.entity.Package;
import at.pranjic.application.mtcg.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class PackageService {
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;

    public PackageService(PackageRepository packageRepository, UserRepository userRepository) {
        this.packageRepository = packageRepository;
        this.userRepository = userRepository;
    }

    public void addPackage(Package pkg, List<String> cardIds) {
        if (cardIds == null || cardIds.size() != 5) {
            throw new IllegalArgumentException("A package must contain exactly 5 cards.");
        }
        packageRepository.save(pkg, cardIds);
    }

    public void acquirePackage(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getCoins() < 5) {
            throw new IllegalArgumentException("Not enough coins to acquire a package.");
        }

        Package pkg = packageRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No packages available for acquisition"));

        user.setCoins(user.getCoins() - 5);
        userRepository.update(user);

        List<Card> cards = packageRepository.getCardsInPackage(pkg.getId());

        cards.forEach(card -> userRepository.addCardToUser(user.getId(), card.getId()));

        packageRepository.delete(pkg.getId());
    }


    public Optional<Package> getPackageById(int packageId) {
        return packageRepository.findById(packageId);
    }

    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    public void deletePackage(int packageId) {
        packageRepository.delete(packageId);
    }

}

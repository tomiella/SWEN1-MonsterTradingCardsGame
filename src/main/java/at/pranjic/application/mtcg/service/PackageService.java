package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.repository.PackageRepository;
import at.pranjic.application.mtcg.entity.Package;

import java.util.List;
import java.util.Optional;

public class PackageService {
    private final PackageRepository packageRepository;

    public PackageService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public void addPackage(Package pkg, List<String> cardIds) {
        if (cardIds == null || cardIds.size() != 5) {
            throw new IllegalArgumentException("A package must contain exactly 5 cards.");
        }
        packageRepository.save(pkg, cardIds);
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

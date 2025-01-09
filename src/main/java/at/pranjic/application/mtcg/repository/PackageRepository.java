package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.Package;

import java.util.List;
import java.util.Optional;

public interface PackageRepository {
    void save(Package pkg, List<String> cardIds);
    Optional<Package> findById(int packageId);
    List<Package> findAll();
    void delete(int packageId);
}

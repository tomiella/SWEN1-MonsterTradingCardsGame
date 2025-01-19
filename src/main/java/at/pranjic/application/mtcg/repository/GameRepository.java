package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.Battle;
import java.util.List;
import java.util.Optional;

public interface GameRepository {
    void createBattle(Battle battle);
    Optional<Battle> getBattleById(int battleId);
    List<Battle> getAllBattles();
    List<Battle> getBattlesByUserId(long userId);
}

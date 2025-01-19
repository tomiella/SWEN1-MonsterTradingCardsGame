package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.entity.Battle;
import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.entity.CardInfo;
import at.pranjic.application.mtcg.entity.User;
import at.pranjic.application.mtcg.repository.CardRepository;
import at.pranjic.application.mtcg.repository.DeckRepository;
import at.pranjic.application.mtcg.repository.GameRepository;
import at.pranjic.application.mtcg.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class GameService {
    private final UserRepository userRepository;
    private final DeckRepository deckRepository;
    private final GameRepository gameRepository;

    private User player1;
    private User player2;
    private final Object queue = new Object();
    private StringBuilder log = new StringBuilder();
    private LocalDateTime startTime = LocalDateTime.now();
    private final Random random = new Random();
    private boolean running = false;


    public GameService(UserRepository userRepository, DeckRepository deckRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
        this.gameRepository = gameRepository;
    }

    public void reset() {
        player1 = null;
        player2 = null;
        running = false;
        log = new StringBuilder();
        startTime = LocalDateTime.now();
    }

    public String startBattle(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            try {
                if ((player1 != null && Objects.equals(user.getId(), player1.getId())) || (player2 != null && Objects.equals(user.getId(), player2.getId()))) {
                    throw new RuntimeException("User already started");
                }

                if (player1 == null) {
                    player1 = user;
                } else if (player2 == null) {
                    player2 = user;
                }

                System.out.println("User started");
                if (player2 == null) {
                    synchronized (queue) {
                        while (player2 == null) {
                            System.out.println("Waiting for queue");
                            queue.wait();
                        }
                    }
                }

                this.running = true;

                if (player2.getId() == user.getId()) {
                    synchronized (queue) {
                        while (running) {
                            System.out.println("Waiting for battle finish");
                            queue.wait();
                        }
                        System.out.println("Battle finished");
                        return log.toString();
                    }
                }

                log.append("Player %s and Player %s battle started%n".formatted(player1.getUsername(), player2.getUsername()));

                this.executeBattle();
                this.running = false;

                synchronized (queue) {
                    queue.notifyAll();
                }

                return log.toString();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("User not valid");
    }

    public void executeBattle() {
        List<Card> deck1 = this.deckRepository.getDeckByUserId(player1.getId());
        List<Card> deck2 = this.deckRepository.getDeckByUserId(player2.getId());

        for (int round = 0; round < 100; round++) {
            if (deck1.isEmpty() || deck2.isEmpty()) {
                log.append("All round played%n");
                break;
            }

            Card card1 = deck1.get(random.nextInt(deck1.size()));
            Card card2 = deck2.get(random.nextInt(deck2.size()));

            User winner = this.getWinner(card1, card2);

            if (winner == null) {
                log.append("Round is a draw%n");
                continue;
            }

            log.append("Round %s won by player: %s%n".formatted(round, winner.getUsername()));
            if (Objects.equals(winner.getId(), player1.getId())) {
                deck2.remove(card2);
                deck1.add(card2);
            } else if (Objects.equals(winner.getId(), player2.getId())) {
                deck1.remove(card1);
                deck2.add(card1);
            }
        }

        if (deck2.isEmpty()) {
            log.append("Player %s won!%n".formatted(player1.getUsername()));
            updateStats(player1);
        }
        if (deck1.isEmpty()) {
            log.append("Player %s won!%n".formatted(player2.getUsername()));
            updateStats(player2);
        }
    }

    private void updateStats(User winner) {
        Battle battle = new Battle(0, player1.getId(), player2.getId(), winner.getId(), log.toString(), startTime);
        gameRepository.createBattle(battle);

        player1.setGames_played(player1.getGames_played() + 1);
        player2.setGames_played(player2.getGames_played() + 1);

        if (winner.getId() == player1.getId()) {
            player1.setElo(player1.getElo() + 3);
            player2.setElo(player2.getElo() - 5);
            player1.setWins(player1.getWins() + 1);
            player2.setLosses(player2.getLosses() + 1);
        } else if (winner.getId() == player2.getId()) {
            player1.setElo(player1.getElo() - 5);
            player2.setElo(player2.getElo() + 3);
            player2.setWins(player2.getWins() + 1);
            player1.setLosses(player1.getLosses() + 1);
        }

        userRepository.update(player1);
        userRepository.update(player2);
    }

    public User getWinner(Card card1, Card card2) {
        double damage1 = card1.getDamage();
        if (damageExceptions(card1, card2)) {
            return player1;
        }

        double damage2 = card2.getDamage();
        if (damageExceptions(card2, card1)) {
            return player2;
        }

        if (card1.getCardType() == "spell") {
            damage1 = this.spellDamage(card1, card2.getElementType());
        }

        if (card2.getCardType() == "spell") {
            damage2 = this.spellDamage(card2, card1.getElementType());
        }

        if (damage1 > damage2) {
            return player1;
        } else if (damage1 < damage2) {
            return player2;
        }

        return null;
    }

    private double spellDamage(Card card, String card2Element) {
        if (card.getElementType() == card2Element) {
            return card.getDamage();
        }

        if (card.getElementType() == "water" && card2Element == "fire") {
            return card.getDamage() * 2;
        }
        if (card.getElementType() == "water" && card2Element == "regular") {
            return (double) card.getDamage() / 2;
        }

        if (card.getElementType() == "fire" && card2Element == "normal") {
            return card.getDamage() * 2;
        }
        if (card.getElementType() == "fire" && card2Element == "water") {
            return (double) card.getDamage() / 2;
        }

        if (card.getElementType() == "regular" && card2Element == "water") {
            return card.getDamage() * 2;
        }
        if (card.getElementType() == "regular" && card2Element == "fire") {
            return (double) card.getDamage() / 2;
        }

        return 0;
    }

    private boolean damageExceptions(Card card1, Card card2) {
        if (card1.getName().contains("Goblin") && card2.getInfo() == CardInfo.DRAGON) {
            return false;
        }
        if (card1.getName() == "Ork" && card2.getInfo() == CardInfo.WIZARD) {
            return false;
        }
        if (card1.getCardType() == "spell" && card2.getInfo() == CardInfo.KNIGHT) {
            return true;
        }
        if (card1.getCardType() == "spell" && card2.getInfo() == CardInfo.KRAKEN) {
            return false;
        }
        if (card1.getInfo() == CardInfo.DRAGON && card2.getInfo() == CardInfo.FIRE_ELF) {
            return false;
        }

        return false;
    }
}

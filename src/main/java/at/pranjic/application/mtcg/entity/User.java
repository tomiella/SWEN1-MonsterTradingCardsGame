package at.pranjic.application.mtcg.entity;

import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String password;
    // cards, decks, stats, ...


    public User(String username, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

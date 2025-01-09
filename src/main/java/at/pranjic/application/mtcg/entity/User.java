package at.pranjic.application.mtcg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonIgnore
    private Long id;

    @JsonProperty("Username")
    private String username;

    @JsonProperty("Password")
    private String password;

    @JsonIgnore
    private int coins = 20;

    @JsonIgnore
    private int elo = 100;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Bio")
    private String bio;

    @JsonProperty("Image")
    private String image;


    public User() {
    }

    public User(String username, String password) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getElo() {
        return elo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}

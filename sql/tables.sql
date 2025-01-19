DO
$$
    DECLARE
        stmt text;
    BEGIN
        -- Drop all objects in the schema
        EXECUTE (SELECT string_agg('DROP SCHEMA public CASCADE;', ''));
        -- Recreate the public schema
        EXECUTE 'CREATE SCHEMA public;';
    END;
$$;

-- Users table
CREATE TABLE IF NOT EXISTS users
(
    id           SERIAL PRIMARY KEY,
    username     VARCHAR(50) UNIQUE NOT NULL,
    password     VARCHAR(255)       NOT NULL,
    name         VARCHAR(255),
    bio          VARCHAR(255),
    image        VARCHAR(255),
    coins        INT DEFAULT 20,
    elo          INT DEFAULT 100,
    games_played INT DEFAULT 0,
    wins         INT DEFAULT 0,
    losses       INT DEFAULT 0
);

-- Cards table
CREATE TABLE IF NOT EXISTS cards
(
    id           VARCHAR(255) PRIMARY KEY,
    name         VARCHAR(100)                                                     NOT NULL,
    damage       INT                                                              NOT NULL,
    element_type VARCHAR(10) CHECK (element_type IN ('fire', 'water', 'regular')) NOT NULL,
    card_type    VARCHAR(10) CHECK (card_type IN ('spell', 'monster'))            NOT NULL
);

-- User cards table (ownership of cards)
CREATE TABLE IF NOT EXISTS user_cards
(
    id         SERIAL PRIMARY KEY,
    user_id    INT REFERENCES users (id) ON DELETE CASCADE,
    card_id    VARCHAR(255) REFERENCES cards (id) ON DELETE CASCADE,
    is_in_deck BOOLEAN DEFAULT FALSE,
    is_locked  BOOLEAN DEFAULT FALSE
);

-- Packages table
CREATE TABLE IF NOT EXISTS packages
(
    id         SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Package cards table (cards within a package)
CREATE TABLE IF NOT EXISTS package_cards
(
    package_id INT REFERENCES packages (id) ON DELETE CASCADE,
    card_id    VARCHAR(255) REFERENCES cards (id) ON DELETE CASCADE,
    PRIMARY KEY (package_id, card_id)
);

-- Trades table
CREATE TABLE IF NOT EXISTS tradings
(
    id                     SERIAL PRIMARY KEY,                          -- Unique identifier for the trading deal
    offered_card_id        INT REFERENCES user_cards (id) ON DELETE CASCADE, -- Card offered for trade
    requested_card_type    VARCHAR(10) CHECK (requested_card_type IN ('spell', 'monster')), -- Required type of received card
    requested_min_damage   INT DEFAULT 0,                               -- Minimum damage of the card
    user_id                INT REFERENCES users (id) ON DELETE CASCADE  -- User who created the trade
);

-- Battles table
CREATE TABLE IF NOT EXISTS battles
(
    id         SERIAL PRIMARY KEY,
    player1_id INT REFERENCES users (id) ON DELETE CASCADE,
    player2_id INT REFERENCES users (id) ON DELETE CASCADE,
    winner_id  INT REFERENCES users (id),
    log        TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

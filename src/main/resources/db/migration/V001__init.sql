CREATE TABLE players (
    id BIGINT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME2 NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at DATETIME2 NOT NULL DEFAULT CURRENT_TIMESTAMP()
);


CREATE TABLE player_profiles (
    player_id BIGINT PRIMARY KEY,
    country VARCHAR(50) NOT NULL,
    age INT NULL,
    bio VARCHAR(255) NULL,
    updated_at DATETIME2 NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (player_id) REFERENCES players(id)
);


CREATE TABLE missions (
    id BIGINT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    completed BIT NOT NULL DEFAULT 0,
    progress INT NOT NULL DEFAULT 0,
    required_progress INT NOT NULL DEFAULT 100,
    updated_at DATETIME2 NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (player_id) REFERENCES players(id)
);


CREATE TABLE mission_rewards (
    id BIGINT PRIMARY KEY,
    mission_id BIGINT NOT NULL,
    reward_type VARCHAR(50) NOT NULL,
    amount INT NOT NULL,
    FOREIGN KEY (mission_id) REFERENCES missions(id)
);


CREATE TABLE tags (
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE mission_tags (
    mission_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (mission_id, tag_id),
    FOREIGN KEY (mission_id) REFERENCES missions(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id)
);
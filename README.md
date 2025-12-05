# Mini Test Project – Player Missions Sync Service

Assignment to test **Java**, **Hibernate/JPA**, **SQL**, **Redis**, and **Azure Functions**.  
Purpose of this assesment is to implement simple service to laod relational data from SQL and synchronizes it to Redis through an HTTP-triggered Azure Function.

---

## Service Description

- A **SQL database** with two tables: `players` and `missions`
- A **one-to-many** relation: one **Player** has many **Missions**
- Redis is used as a **cache layer** for other systems

---


## Project structure

```
src/main/java
  ├─ config/
  ├─ entity/
  ├─ repository/
  ├─ service/
  ├─ controller/
  ├─ dto/
  ├─ redis/
application.yml
```

## SQL Schema

Use the following database schema:

```sql
-- Players table
CREATE TABLE players (
    id BIGINT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE()
);

-----------------------------------------------------------------------

CREATE TABLE player_profiles (
    player_id BIGINT PRIMARY KEY,
    country VARCHAR(50) NOT NULL,
    age INT NULL,
    bio VARCHAR(255) NULL,
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (player_id) REFERENCES players(id)
);

-----------------------------------------------------------------------

CREATE TABLE missions (
    id BIGINT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    completed BIT NOT NULL DEFAULT 0,
    progress INT NOT NULL DEFAULT 0,
    required_progress INT NOT NULL DEFAULT 100,
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (player_id) REFERENCES players(id)
);

-----------------------------------------------------------------------

CREATE TABLE mission_rewards (
    id BIGINT PRIMARY KEY,
    mission_id BIGINT NOT NULL,
    reward_type VARCHAR(50) NOT NULL,
    amount INT NOT NULL,
    FOREIGN KEY (mission_id) REFERENCES missions(id)
);

-----------------------------------------------------------------------

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

```


## Requirements
1. Implement model, service, repository DB structure with JPA queries to retrieve data needed.
2. Avoid N + 1 queries by using JOIN FETCH.

## Azure Function (HTTP-triggered)
1. Load Player + Missions using Hibernate/JPA
2. Serialize the data into Redis
3. Return the synchronized object as JSON
4. Correct use of DI
5. Handle errors

## Azure Function Requirements
- HTTP Method: GET /sync/players/{playerId}

1. Validate input
2. Load data from SQL using a service layer
3. Map Hibernate entities → DTO
4. Store the data into Redis under the key:
    - player:{id}:missions
5. Set a 10-minute TTL
6. Return a JSON response

7. Handle error responses:
    - 400 → invalid player ID
    - 404 → player not found
    - 500 → unexpected failures

## Redis Requirements
1. Store data as Redis hash with key format:
    - player:{id}:missions
2. Cache Eviction
    - Only sync when data changed (store last_sync_at in redis and compare)

## Bonus 1
1. Pagination Support
    - Create pagination support for the large mission lists using SQL + JPA pagination to avoid heap memoryh out of space

## Bonus 2
- PUT Http azure function
1. Update SQL data
2. Invalidate Redis cache
3. Sync redis watermark

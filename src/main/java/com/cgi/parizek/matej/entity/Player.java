package com.cgi.parizek.matej.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player extends ABaseEntity{
    @Id
    protected Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String status = "ACTIVE";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @OneToOne(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PlayerProfile profile;
}

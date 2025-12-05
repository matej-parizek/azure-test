package com.cgi.parizek.matej.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerProfile extends ABaseEntity{
    @Id
    @Column(name = "player_id")
    private Long playerId;

    @Column(nullable = false)
    private String country;

    @Column
    private Integer age;

    @Column
    private String bio;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "player_id")
    private Player player;
}

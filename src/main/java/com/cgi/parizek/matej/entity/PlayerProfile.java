package com.cgi.parizek.matej.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerProfile extends ABaseEntity {
    @Id
    @Column(name = "player_id")
    private Long playerId;

    @OneToOne
    @MapsId
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(nullable = false, length = 50)
    private String country;

    @Column
    private Integer age;

    @Column(length = 255)
    private String bio;
}

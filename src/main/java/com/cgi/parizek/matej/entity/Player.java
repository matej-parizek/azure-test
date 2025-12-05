package com.cgi.parizek.matej.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mission> missions = new ArrayList<>();

    @OneToOne(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    private PlayerProfile profile;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

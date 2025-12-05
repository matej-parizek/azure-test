package com.cgi.parizek.matej.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "mission_rewards")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissionReward {
    @Id
    private Long id;

    @Column(nullable = false)
    private String rewardType;

    @Column(nullable = false)
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

}

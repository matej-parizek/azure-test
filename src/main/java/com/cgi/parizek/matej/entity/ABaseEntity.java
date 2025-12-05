package com.cgi.parizek.matej.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;


@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class ABaseEntity {
    @Column(name = "update_at", nullable = false)
    protected LocalDateTime updateAt;

    @PrePersist
    void onCreate(){
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate(){
        updateAt = LocalDateTime.now();
    }
}

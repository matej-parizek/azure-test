package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.entity.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMissionService {
    Page<Mission> retrieveByPlayerId(Long playerId, Pageable pageable);
}

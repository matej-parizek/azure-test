package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.entity.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMissionService {
    Page<Mission> retrieveByPlayerId(Long playerId, Pageable pageable);
    List<Mission> retrieveByPlayerId(Long playerId);

}

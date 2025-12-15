package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.entity.Mission;
import com.cgi.parizek.matej.repository.IMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionService implements IMissionService {
    private final IMissionRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<Mission> retrieveByPlayerId(Long playerId, Pageable pageable) {
        return repository.findByPlayerId(playerId, pageable);
    }

}

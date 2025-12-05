package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.config.PagingProperties;
import com.cgi.parizek.matej.entity.Mission;
import com.cgi.parizek.matej.repository.IMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService implements IMissionService {
    private final IMissionRepository repository;
    private final PagingProperties pagingProperties;

    @Override
    public Page<Mission> retrieveByPlayerId(Long playerId, Pageable pageable) {
        return repository.findByPlayerId(playerId, pageable);
    }

    @Override
    public List<Mission> retrieveByPlayerId(Long playerId){
        Page<Mission> page;
        List<Mission> allMissionsDTO = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, pagingProperties.getSize());
        while ((page = this.retrieveByPlayerId(playerId, pageable)).hasNext()) {
            allMissionsDTO.addAll(page.getContent());
            pageable = page.nextPageable();
        }
        return allMissionsDTO;
    }

}

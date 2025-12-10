package com.cgi.parizek.matej.redis;

import com.cgi.parizek.matej.dto.MissionDto;
import com.cgi.parizek.matej.dto.PlayerDto;

import java.util.List;

public interface IPlayerCache extends ICacheService<PlayerDto> {

    void saveMissions(String key, List<MissionDto> value, Integer page);

    List<MissionDto> getMissions(String key, Integer page);
}

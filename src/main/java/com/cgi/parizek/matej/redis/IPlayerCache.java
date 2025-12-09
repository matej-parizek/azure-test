package com.cgi.parizek.matej.redis;

import com.cgi.parizek.matej.entity.Mission;
import com.cgi.parizek.matej.entity.Player;

import java.util.List;

public interface IPlayerCache extends ICacheService<Player> {

    void saveMissions(String key, List<Mission> value, Integer page);

    List<Mission> getMissions(String key, Integer page);
}

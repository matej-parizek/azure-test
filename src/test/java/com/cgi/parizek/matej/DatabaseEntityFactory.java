package com.cgi.parizek.matej;

import com.cgi.parizek.matej.entity.Mission;
import com.cgi.parizek.matej.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class DatabaseEntityFactory {
    public Player player(Integer numberOfMission){

        var profile = EntityFactory.playerProfile(null).build();
        var player = EntityFactory.player(null)
                .profile(profile)
                .build();
        var missions = IntStream.range(0,numberOfMission).mapToObj((ignored)->mission(player)).toList();
        player.setMissions(missions);
        profile.setPlayer(player);
        return player;
    }
    public Player player(){
        return player(1);
    }

    public Mission mission(Player player){
        var missionTag = EntityFactory.tag(null).build();
        var missionReward = EntityFactory.missionReward(null).build();
        var mission = EntityFactory.mission(null)
                .tags(Set.of(missionTag))
                .rewards(List.of(missionReward))
                .player(player)
                .build();
        missionReward.setMission(mission);
        missionTag.setMissions(Set.of(mission));
        return mission;
    }

}

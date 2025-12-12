package com.cgi.parizek.matej.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MissionRewardMapperTest {
    @Test
    @DisplayName("Mission reward mapper test")
    void reward_mapper() {
        var reward = com.cgi.parizek.matej.EntityFactory.missionReward(1L).build();
        var dto = MissionRewardMapper.map(reward);

        assertEquals(dto.getId(), reward.getId());
        assertEquals(dto.getRewardType(), reward.getRewardType());
        assertEquals(dto.getAmount(), reward.getAmount());
    }

    @Test
    @DisplayName("Mission reward mapper null test")
    void reward_mapper_null() {
        var dto = MissionRewardMapper.map(null);
        assertNull(dto);
    }
}
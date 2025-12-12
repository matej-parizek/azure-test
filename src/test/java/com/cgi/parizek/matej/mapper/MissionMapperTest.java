package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.EntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MissionMapperTest {


    @Test
    @DisplayName("Map mission with null collections, should be empty collections in DTO")
    void map_mission_null_collections_to_empty_collections() {
        var mission = EntityFactory.mission(1L).build();

        var dto = MissionMapper.map(mission);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo(mission.getName());
        assertThat(dto.getDescription()).isEqualTo(mission.getDescription());
        assertThat(dto.getType()).isEqualTo(mission.getType());
        assertThat(dto.getCompleted()).isEqualTo(mission.isCompleted());
        assertThat(dto.getProgress()).isEqualTo(mission.getProgress());
        assertThat(dto.getRequiredProgress()).isEqualTo(mission.getRequiredProgress());
        assertThat(dto.getRewards()).isNotNull().isEmpty();
        assertThat(dto.getTags()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Map mission with rewards and tags delegates to nested mappers")
    void map_mission_with_collections() {
        var rewardEntity = EntityFactory.missionReward(10L).build();
        var tagEntity = EntityFactory.tag(20L).build();

        var mission = EntityFactory.mission(1L)
                .rewards(List.of(rewardEntity))
                .tags(Set.of(tagEntity))
                .build();

        var rewardDto = EntityFactory.missionRewardDto(10L).build();
        var tagDto = EntityFactory.tagDto(20L).build();

        try (var mockStatic = Mockito.mockStatic(MissionRewardMapper.class);
             var mocked = Mockito.mockStatic(TagMapper.class)) {

            mockStatic.when(() -> MissionRewardMapper.map(rewardEntity)).thenReturn(rewardDto);
            mocked.when(() -> TagMapper.map(tagEntity)).thenReturn(tagDto);

            var dto = MissionMapper.map(mission);

            assertThat(dto).isNotNull();
            assertThat(dto.getRewards()).hasSize(1).containsExactly(rewardDto);
            assertThat(dto.getTags()).hasSize(1).containsExactly(tagDto);
            assertThat(dto.getId()).isEqualTo(mission.getId());
            assertThat(dto.getName()).isEqualTo(mission.getName());
        }
    }

    @Test
    @DisplayName("Map null mission should return null")
    void map_null_mission() {
        var dto = MissionMapper.map(null);
        assertThat(dto).isNull();
    }
}
package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.EntityFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TagMapperTest {

    @Test
    @DisplayName("Tag mapper test")
    public void tag_mapper_success() {
        var tag = EntityFactory.tag(1L).build();
        var dto = TagMapper.map(tag);

        Assertions.assertEquals(dto.getId(), tag.getId());
        Assertions.assertEquals(dto.getName(), tag.getName());
    }

    @Test
    @DisplayName("Tag mapper null test")
    public void tag_mapper_null() {
        var dto = TagMapper.map(null);
        Assertions.assertNull(dto);
    }
}
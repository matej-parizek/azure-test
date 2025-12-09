package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.dto.TagDto;
import com.cgi.parizek.matej.entity.Tag;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TagMapper {

    public TagDto map(Tag tag) {
        if (tag == null) {
            return null;
        }
        return TagDto.builder()
                .name(tag.getName())
                .id(tag.getId())
                .build();
    }
}

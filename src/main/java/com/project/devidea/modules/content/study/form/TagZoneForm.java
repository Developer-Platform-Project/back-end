package com.project.devidea.modules.content.study.form;

import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.zone.Zone;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class TagZoneForm {
    Set<String>  tags;
    String location;
    public TagZoneForm(Set<Tag> tags, Zone location) {
        tags.stream().forEach(tag -> {
            this.tags.add(tag.getFirstName());
        });
        this.location=location.toString();
    }
}

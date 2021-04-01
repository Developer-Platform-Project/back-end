package com.project.devidea.modules.content.mentoring.form;

import com.project.devidea.api.Request;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.zone.Zone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMenteeRequest extends Request {

    private String description;
    private Set<Zone> zones;
    private Set<Tag> tags;

    private boolean open;
    private boolean free;
}
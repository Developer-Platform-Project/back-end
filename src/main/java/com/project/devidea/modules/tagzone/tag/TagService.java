package com.project.devidea.modules.tagzone.tag;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("TagService")
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    @PostConstruct
    public void initTagData() throws IOException {
        if (tagRepository.count() == 0) {
            ClassPathResource resource = new ClassPathResource("tag_kr.csv");
            InputStream inputStream = resource.getInputStream();
            File file = File.createTempFile("tag_kr", ".csv");
            FileUtils.copyInputStreamToFile(inputStream, file);
            Files.readAllLines(file.toPath(), StandardCharsets.UTF_8).stream()
                    .forEach(line -> {
                        String[] split = line.split(",");
                        Tag tag = Tag.builder()
                                .firstName(split[1])
                                .secondName(split[2].equals("null")?null:split[2])
                                .thirdName(split[3].equals("null")?null:split[3])
                                .build();
                        if (!split[0].equals("parent")){
                            Tag tagParent = tagRepository.findByFirstName(split[0]);
                            tagParent.addChild(tag);
                        }
                        tagRepository.save(tag);
                    });
        }
    }

    @Transactional(readOnly = true)
    public TagsResponseDto findAll() {
        List<Tag> tags = tagRepository.findAll();

        TagsResponseDto dto = new TagsResponseDto();
        tags.forEach(tag -> {
            if (tag.getParent() == null) {
                dto.getParent().add(tag.getFirstName());
            } else {
                dto.getChildren().computeIfAbsent(tag.getParent().toString(), k -> new ArrayList<>());
                dto.getChildren().get(tag.getParent().getFirstName()).add(tag.getFirstName());
            }
        });

        return dto;
    }

    public List<Tag> findInterests(List<String> interests) {
        return tagRepository.findByFirstNameIn(interests);
    }
}

package com.codacy.commitviewer.infra.github.mapper;

import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.infra.github.dtos.GitHubCommitDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GitHubApiMapper {

    @Mapping(target = "message", source = "commit.message")
    @Mapping(target = "author", source = "commit.author.name")
    @Mapping(target = "date", source = "commit.author.date", qualifiedByName = "toOffsetDateTime")
    Commit to(GitHubCommitDto gitHubCommitDto);
    List<Commit> to(List<GitHubCommitDto> gitHubCommitDto);

    @Named("toOffsetDateTime")
    default OffsetDateTime toOffsetDateTime(String date){
        return OffsetDateTime.parse(date);
    }

}

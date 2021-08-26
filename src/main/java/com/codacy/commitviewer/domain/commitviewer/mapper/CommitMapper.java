package com.codacy.commitviewer.domain.commitviewer.mapper;

import com.codacy.commitviewer.api.dtos.CommitDto;
import com.codacy.commitviewer.domain.commitviewer.entity.Commit;
import com.codacy.commitviewer.infra.github.dtos.GitHubCommitDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommitMapper {

    @Mapping(target = "date", source = "date", qualifiedByName = "fromOffsetDateTime")
    CommitDto from(Commit gitHubCommitDto);
    List<CommitDto> from(List<Commit> gitHubCommitDto);

    @Named("fromOffsetDateTime")
    default String fromOffsetDateTime(OffsetDateTime offsetDateTime){
        return offsetDateTime.format(DateTimeFormatter.BASIC_ISO_DATE);
    }
}

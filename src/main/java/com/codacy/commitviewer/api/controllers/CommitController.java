package com.codacy.commitviewer.api.controllers;

import com.codacy.commitviewer.api.dtos.CommitDto;
import com.codacy.commitviewer.domain.commitviewer.service.CommitViewerAggregator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RequestMapping("/commits")
@RestController
@AllArgsConstructor
@Slf4j
public class CommitController {

    private final CommitViewerAggregator commitViewerAggregator;

    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    List<CommitDto> getCommitsLocal(@RequestParam("url") @NotNull final String url,
                                    @RequestParam("per_page") @NotNull @Max(100) final Integer per_page,
                                    @RequestParam(value = "page", required = false) @Min(1) final Integer page,
                                    @RequestParam(value = "force_local", required = false) final Boolean forceLocal){
        log.trace("url: {} per_page:{} page:{} forceLocal:{}", url, per_page, page, forceLocal);
        boolean local = !Objects.isNull(forceLocal) && forceLocal;
        int pageInt = Optional.ofNullable(page).orElse(1);
        return  commitViewerAggregator.getCommits(url, per_page, pageInt, local);
    }
}

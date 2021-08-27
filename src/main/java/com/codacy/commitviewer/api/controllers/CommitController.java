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

import java.util.List;


@RequestMapping("/commits")
@RestController
@AllArgsConstructor
@Slf4j
public class CommitController {

    private final CommitViewerAggregator commitViewerAggregator;

    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    List<CommitDto> getCommitsLocal(@RequestParam("url") final String url,
                                    @RequestParam("limit") final Integer limit,
                                    @RequestParam("offset") final Integer offset){
        log.debug("url: {} limit:{} offset:{}", url, limit, offset);
        return  commitViewerAggregator.getCommits(url, limit, offset);
    }
}

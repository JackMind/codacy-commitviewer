package com.codacy.commitviewer.domain.git.services;

import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import com.codacy.commitviewer.domain.git.exceptions.UnableToCreateLocalRepoException;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

@Component
@Slf4j
@AllArgsConstructor
public class LocalRepoManagerService {

    private final GitCommands gitCommands;

    @Cacheable(value = "localRepos", cacheManager = "localRepoCacheManager", keyGenerator = "localRepoKeyGenerator")
    public String createLocalRepoDirectory(final GitParsedUrl gitParsedUrl){
        String gitHubUrl = gitParsedUrl.getUrl();
        log.info("Create local repo directory for {}", gitHubUrl);
        try{
            String tempRepoPrefix = gitParsedUrl.getOwner()+"_"+gitParsedUrl.getRepo();
            log.debug("temp repo dir prefix: {}", tempRepoPrefix);

            Path tempRepo = Files.createTempDirectory(tempRepoPrefix);
            log.debug("tempRepo {}", tempRepo);

            String tempRepoPath = tempRepo.toFile().getAbsolutePath();
            log.debug("local repo directory created at {}", tempRepoPath);

            log.info("Cloning remote repo {} to temp repo dir {}", gitHubUrl, tempRepoPath);
            gitCommands.cloneRepo(gitHubUrl, tempRepoPath);
            log.info("Git clone completed");
            return tempRepoPath;
        } catch (IOException e) {
            throw new UnableToCreateLocalRepoException(e.getMessage());
        }
    }

    @Configuration
    @Slf4j
    @EnableCaching
    public static class LocalRepoCacheConfig {

        @Bean
        @Named("localRepoCacheManager")
        public CacheManager localRepoCacheManager(){
            CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
            caffeineCacheManager.setCaffeine(
                    Caffeine.newBuilder()
                    .initialCapacity(10)
                    .expireAfterAccess(Duration.ofMinutes(5))
                    .removalListener(
                            (key, tempRepoPath, removalCause) -> {
                                try {
                                    Files.delete(Paths.get((String) tempRepoPath));
                                    LocalRepoManagerService.log.info("Local repo {} deleted ", key);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    )
            );
            return caffeineCacheManager;
        }

        @Bean
        @Named("localRepoKeyGenerator")
        public KeyGenerator localRepoKeyGenerator(){
            return (o, method, params) -> ((GitParsedUrl) params[0]).getUrl();
        }

    }

}

package com.codacy.commitviewer.domain.git.services;

import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import com.codacy.commitviewer.domain.git.exceptions.UnableToCreateLocalRepoException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
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

    @Cacheable(value = LocalRepoCacheConfig.CACHE_NAME, cacheManager = "localRepoCacheManager",
            keyGenerator = "localRepoKeyGenerator")
    public String createLocalRepoDirectory(final GitParsedUrl gitParsedUrl, final int limit){
        String gitHubUrl = gitParsedUrl.getUrl();
        log.info("Create local repo directory for {}", gitHubUrl);
        try{
            String tempRepoPrefix = "com.codacy.commitviewer.localRepos."
                    +gitParsedUrl.getOwner()+"_"+gitParsedUrl.getRepo();
            log.debug("temp repo dir prefix: {}", tempRepoPrefix);

            Path tempRepo = Files.createTempDirectory(tempRepoPrefix);
            log.debug("tempRepo {}", tempRepo);

            String tempRepoPath = tempRepo.toFile().getAbsolutePath();
            log.debug("local repo directory created at {}", tempRepoPath);

            gitCommands.cloneRemoteToDirWithDepth(gitHubUrl, tempRepoPath, limit);

            return tempRepoPath;
        } catch (IOException e) {
            throw new UnableToCreateLocalRepoException(e.getMessage());
        }
    }

    public static void forceDeleteDir(Path path){
        try {
            FileUtils.forceDelete(path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Configuration
    @Slf4j
    @EnableCaching
    public static class LocalRepoCacheConfig {

        public static final String CACHE_NAME = "localRepos";

        @Autowired
        private CacheManager cacheManager;

        @PostConstruct
        void cleanUp(){
            LocalRepoManagerService.log.info("Setting up clean up shutdown hook for cache {}", CACHE_NAME);
            Runtime.getRuntime().addShutdownHook(
                    new Thread(() -> {
                        Cache<Object, Object> cache = ((CaffeineCache)cacheManager.getCache(CACHE_NAME)).getNativeCache();
                        LocalRepoManagerService.log.info("Shutdown hook clean up for cache {} has {} entries", CACHE_NAME, cache.estimatedSize());
                        cache.asMap()
                                .forEach((key, value) -> {
                                    LocalRepoManagerService.log.info("Deleting local repo: {}", value);
                                    forceDeleteDir(Paths.get((String) value));
                                });
                    })
            );

        }

        @Bean
        @Named("localRepoCacheManager")
        public CacheManager localRepoCacheManager(){
            CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
            caffeineCacheManager.setCaffeine(
                    Caffeine.newBuilder()
                    .initialCapacity(10)
                    .evictionListener(
                            (key, value, removalCause) -> {
                                LocalRepoManagerService.log.info("Executing Eviction Listener key: {} value: {}", key, value);
                                forceDeleteDir(Paths.get((String) value));
                                LocalRepoManagerService.log.info("Local repo {} deleted ", key);
                            }
                    )
                    .expireAfterWrite(Duration.ofSeconds(5))
                    .expireAfterAccess(Duration.ofSeconds(10))
                    .recordStats()

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

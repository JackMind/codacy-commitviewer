package com.codacy.commitviewer.domain.git.services;

import com.codacy.commitviewer.domain.commitviewer.entity.GitParsedUrl;
import com.codacy.commitviewer.domain.git.entity.LocalRepo;
import com.codacy.commitviewer.domain.git.exceptions.UnableToCreateLocalRepoException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoField;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
@RequiredArgsConstructor
public class LocalRepoManagerService {

    private static final String CACHE_NAME = "localRepos";

    private final GitCommands gitCommands;
    private final CacheManager cacheManager;

    @Value("${app.cache.localRepos.deleteAfterMinutes:10}")
    private Integer deleteAfterMinutes;

    /**
     * Create local repo directory and saves the created directory path into a cache.
     * The cache is based on the url of the requested github url repo.
     * After the directory is created the clone of the remote repo to the local directory
     * is executed with the depth flag.
     *
     * @param gitParsedUrl the git parsed url
     * @return the string
     */
    @Cacheable(value = CACHE_NAME, cacheManager = "localRepoCacheManager",
            keyGenerator = "localRepoKeyGenerator")
    public LocalRepo createLocalRepoDirectory(final GitParsedUrl gitParsedUrl){
        String gitHubUrl = gitParsedUrl.getUrl();
        log.info("Create local repo directory for {}", gitHubUrl);
        try{
            String tempRepoPrefix = "com.codacy.commitviewer.localRepos."
                    +gitParsedUrl.getOwner()+"_"+gitParsedUrl.getRepo();
            log.trace("temp repo dir prefix: {}", tempRepoPrefix);

            Path tempRepo = Files.createTempDirectory(tempRepoPrefix);
            log.trace("tempRepo {}", tempRepo);

            String tempRepoPath = tempRepo.toFile().getAbsolutePath();
            log.info("local repo directory created at {}", tempRepoPath);

            gitCommands.cloneRemoteToDirWithDepth(gitHubUrl, tempRepoPath, 1);

            return new LocalRepo(tempRepoPath, OffsetDateTime.now());

        } catch (IOException e) {
            throw new UnableToCreateLocalRepoException(e.getMessage());
        }
    }


    /**
     * Force delete directory.
     *
     * @param path the path
     */
    public static void forceDeleteDir(Path path){
        try {
            FileUtils.forceDelete(path.toFile());
            log.debug("Directory {} deleted.", path.toAbsolutePath().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up a callback that will be executed by the JVM on shutdown that
     * deletes all created directories on cache.
     */
    @PostConstruct
    void cleanUp(){
        LocalRepoManagerService.log.info("Setting up clean up shutdown hook for cache {}", CACHE_NAME);
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    ConcurrentMap<Object, Object> cache = ((ConcurrentMapCache)cacheManager.getCache(CACHE_NAME)).getNativeCache();
                    LocalRepoManagerService.log.info("Shutdown hook clean up for cache {} has {} entries", CACHE_NAME, cache.size()
                    );
                    cache.forEach((key, value) -> {
                                LocalRepo repo = (LocalRepo) value;
                                LocalRepoManagerService.log.info("Deleting local repo: {}", repo.getPathAsString());
                                forceDeleteDir(repo.getPath());
                            });
                })
        );

    }

    @Scheduled(fixedRateString = "${app.cache.localRepos.scheduler.cleanUpMillis:5000}")
    public void deleteLocalDirectory(){
        ConcurrentMap<Object, Object> cache = ((ConcurrentMapCache)cacheManager.getCache(CACHE_NAME)).getNativeCache();
        log.debug("{} size: {}", CACHE_NAME, cache.size());
        cache.forEach((key, value) -> {
            LocalRepo repo = (LocalRepo) value;
            //TODO rasantos do more TimeUnits
            long time = OffsetDateTime.now().getLong(ChronoField.INSTANT_SECONDS) - repo.getCreated().getLong(ChronoField.INSTANT_SECONDS);
            long minutes = TimeUnit.SECONDS.toMinutes(time);
            log.trace("time: {}, minutes: {}", time, minutes);
            if(minutes >= deleteAfterMinutes){
                cacheManager.getCache(CACHE_NAME).evict(key);
                forceDeleteDir(repo.getPath());
                log.info("Deleted local repo: {}", repo.getPathAsString());
                log.debug("createdAt: {}", repo.getCreated());
            }
        });

    }

    @Configuration
    @Slf4j
    @EnableCaching
    @EnableScheduling
    public static class LocalRepoCacheConfig {

        /**
         * Local repo cache manager cache manager.
         *
         * @return the cache manager
         */
        @Bean
        @Named("localRepoCacheManager")
        public CacheManager localRepoCacheManager(){
            return new ConcurrentMapCacheManager();
        }

        /**
         * Local repo key generator key generator.
         *
         * @return the key generator
         */
        @Bean
        @Named("localRepoKeyGenerator")
        public KeyGenerator localRepoKeyGenerator(){
            return (o, method, params) -> ((GitParsedUrl) params[0]).getUrl();
        }

    }

}

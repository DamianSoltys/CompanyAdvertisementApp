package local.project.Inzynierka.servicelayer.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCache voivodeshipCache = buildCache("voivodeship", 30, 16);
        CaffeineCache promotionItemTypes = buildCache("PItypes", 60, 3);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(voivodeshipCache, promotionItemTypes));
        return manager;
    }

    private CaffeineCache buildCache(String name, int minutesToExpire, int maximumSize) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterAccess(minutesToExpire, TimeUnit.MINUTES)
                .maximumSize(maximumSize)
                .build());
    }
}

package core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.dv8tion.jda.api.requests.RestAction;
import java.time.Duration;

//TODO: Does it work?
public class QuickUpdater {

    private static final QuickUpdater ourInstance = new QuickUpdater();

    public static QuickUpdater getInstance() {
        return ourInstance;
    }

    private QuickUpdater() {
    }

    private final Cache<String, Long> idCache = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(20))
            .build();

    public synchronized <T> RestAction<T> update(String type, Object key, RestAction<T> restAction) {
        String stringKey = type + ":" + key;
        long id = System.nanoTime();

        restAction.setCheck(() -> {
                    Long idCheck = idCache.getIfPresent(stringKey);
                    return idCheck == null || idCheck == id;
                });

        idCache.put(stringKey, id);
        return restAction;
    }

}

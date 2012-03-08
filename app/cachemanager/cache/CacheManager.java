package cachemanager.cache;

import java.io.Serializable;

import play.Logger;
import play.cache.Cache;
import play.mvc.Scope;
import cachemanager.cache.CacheKey.CacheScope;

public final class CacheManager {

    private CacheManager() {

    }

    private static String constructKey(CacheKey cacheKey, Object id) {

        String key = cacheKey.key();
        if (id != null) {
            key += "_" + id.toString();
        }
        if (CacheScope.SESSION.equals(cacheKey.scope())) {
            key += "_" + Scope.Session.current().getId();
        }
        return key;
    }

    public static <P extends Object, R extends Serializable> R get(CacheKey cacheKey, P id) {

        final String key = constructKey(cacheKey, id);
        R value = (R) Cache.get(key);
        if (value != null) {
            Logger.debug("Cache hit: %s", key);
            return value;
        }
        Logger.debug("Cache miss: %s", key);
        final CacheAdapter adapter = cacheKey.adapter();
        value = adapter.get(id);
        Cache.set(key, value, adapter.expire);
        return value;
    }

    public static <R extends Serializable> R get(CacheKey cacheKey) {

        return get(cacheKey, null);
    }

    public static void delete(CacheKey cacheKey, Object id) {

        String key = constructKey(cacheKey, id);
        Cache.delete(key);
        Logger.debug("%s removed from cache", key);
    }
}

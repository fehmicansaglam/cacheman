package cache;

import cachemanager.cache.CacheAdapter;
import cachemanager.cache.CacheKey;

public enum SessionCacheKey implements CacheKey {

    ;

    private CacheAdapter adapter;

    private SessionCacheKey(CacheAdapter adapter) {

        this.adapter = adapter;
    }

    @Override
    public CacheScope scope() {

        return CacheScope.SESSION;
    }

    @Override
    public String key() {

        return this.name();
    }

    @Override
    public CacheAdapter getAdapter() {

        return this.adapter;
    }
}
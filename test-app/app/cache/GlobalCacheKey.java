package cache;

import models.User;
import cachemanager.cache.CacheAdapter;
import cachemanager.cache.CacheKey;
import cachemanager.cache.NameSpaceCacheAdapter;
import cachemanager.common.Function;

public enum GlobalCacheKey implements CacheKey {

    USER_BY_ID, USER, USER_COUNT, NS_USER_BY_ID;

    private CacheAdapter adapter;

    @Override
    public CacheScope scope() {
        return CacheScope.GLOBAL;
    }

    @Override
    public String key() {
        return this.name();
    }

    @Override
    public CacheAdapter getAdapter() {
        if (this.adapter != null) {
            return this.adapter;
        }

        switch (this) {
        case USER_BY_ID:
            return (this.adapter = new CacheAdapter(User.class, "findById", null, Object.class));

        case NS_USER_BY_ID:
            return (this.adapter = new NameSpaceCacheAdapter("user", User.class, "findById", null,
                    Object.class));

        case USER:
            return (this.adapter = new CacheAdapter(new Function<String, User>() {

                @Override
                public User apply(final String username) {

                    return User.findByUsername(username);
                }
            }, "8h"));

        case USER_COUNT:
            return (this.adapter = new CacheAdapter(User.class, "count", "5mn"));

        default:
            throw new IllegalArgumentException("Unknown cache key");
        }
    }
}
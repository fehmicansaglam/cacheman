package cachemanager.cache;

import cachemanager.common.Function;

public enum SampleCacheKey implements CacheKey {

    SAMPLE(new CacheAdapter(new Function<String, String>() {

        @Override
        public String apply(final String input) {

            return input;
        }
    }, "8h"));

    private CacheAdapter adapter;

    private SampleCacheKey(final CacheAdapter adapter) {

        this.adapter = adapter;
    }

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

        return this.adapter;
    }
}
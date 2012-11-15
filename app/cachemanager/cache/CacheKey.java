package cachemanager.cache;

public interface CacheKey {

    public enum CacheScope {
        SESSION, GLOBAL
    }

    public CacheScope scope();

    public String key();

    public CacheAdapter getAdapter();
}
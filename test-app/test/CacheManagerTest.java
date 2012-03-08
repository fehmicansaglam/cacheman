import models.User;

import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.test.UnitTest;
import cache.GlobalCacheKey;
import cachemanager.cache.CacheManager;

public class CacheManagerTest extends UnitTest {

    @Before
    public void before() {

        User.deleteAll();
    }

    @Test
    public void staticMethodTest() {

        new User("testUser1", "").save();
        new User("testUser2", "").save();
        new User("testUser3", "").save();

        // Cache miss
        long start = System.nanoTime();
        Long userCount = CacheManager.get(GlobalCacheKey.USER_COUNT);
        long end = System.nanoTime();
        Logger.info("[staticMethodTest]: Cache miss in %d ns", end - start);
        assertEquals(Long.valueOf(3), userCount);

        // Cache hit
        start = System.nanoTime();
        userCount = CacheManager.get(GlobalCacheKey.USER_COUNT);
        end = System.nanoTime();
        Logger.info("[staticMethodTest]: Cache hit in %dns", end - start);
        assertEquals(Long.valueOf(3), userCount);
    }

    @Test
    public void functionTest() {

        final String testUsername = "testUser4";
        CacheManager.delete(GlobalCacheKey.USER, testUsername);

        User testUser = new User(testUsername, "").save();

        // Cache miss
        long start = System.nanoTime();
        User cachedUser = CacheManager.get(GlobalCacheKey.USER, testUsername);
        long end = System.nanoTime();
        Logger.info("[functionTest]: Cache miss in %dns", end - start);
        assertEquals(testUser, cachedUser);

        // Cache hit
        start = System.nanoTime();
        cachedUser = CacheManager.get(GlobalCacheKey.USER, testUsername);
        end = System.nanoTime();
        Logger.info("[functionTest]: Cache hit in %dns", end - start);
        assertEquals(testUser, cachedUser);

        CacheManager.delete(GlobalCacheKey.USER, testUsername);

        // Cache miss
        start = System.nanoTime();
        cachedUser = CacheManager.get(GlobalCacheKey.USER, testUsername);
        end = System.nanoTime();
        Logger.info("[functionTest]: Cache miss in %dns", end - start);
        assertEquals(testUser, cachedUser);
    }

}

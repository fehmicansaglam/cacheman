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
        Logger.info("[staticMethodTest]: Cache miss in %dns", end - start);
        assertEquals(Long.valueOf(3), userCount);

        // Cache hit
        start = System.nanoTime();
        userCount = CacheManager.get(GlobalCacheKey.USER_COUNT);
        end = System.nanoTime();
        Logger.info("[staticMethodTest]: Cache hit in %dns", end - start);
        assertEquals(Long.valueOf(3), userCount);
    }

    @Test
    public void staticMethodWithParameterTest() {

        User user = new User("testUser1", "").save();
        Long id = user.id;

        // Cache miss
        long start = System.nanoTime();
        user = CacheManager.get(GlobalCacheKey.USER_BY_ID, id);
        long end = System.nanoTime();
        Logger.info("[staticMethodWithParameterTest]: Cache miss in %dns", end - start);
        assertEquals(id, user.id);

        // Cache hit
        start = System.nanoTime();
        user = CacheManager.get(GlobalCacheKey.USER_BY_ID, id);
        end = System.nanoTime();
        Logger.info("[staticMethodWithParameterTest]: Cache hit in %dns", end - start);
        assertEquals(id, user.id);
    }

    @Test
    public void nameSpaceTest() {

        User user1 = new User("testUser1", "").save();
        User user2 = new User("testUser2", "").save();
        Long id1 = user1.id;
        Long id2 = user2.id;

        // Cache miss
        long start = System.nanoTime();
        user1 = CacheManager.get(GlobalCacheKey.NS_USER_BY_ID, id1);
        user2 = CacheManager.get(GlobalCacheKey.NS_USER_BY_ID, id2);
        long end = System.nanoTime();
        Logger.info("[nameSpaceTest]: Cache miss in %dns", end - start);
        assertEquals(id1, user1.id);
        assertEquals(id2, user2.id);

        // Cache hit
        start = System.nanoTime();
        user1 = CacheManager.get(GlobalCacheKey.NS_USER_BY_ID, id1);
        user2 = CacheManager.get(GlobalCacheKey.NS_USER_BY_ID, id2);
        end = System.nanoTime();
        Logger.info("[nameSpaceTest]: Cache hit in %dns", end - start);
        assertEquals(id1, user1.id);
        assertEquals(id2, user2.id);

        CacheManager.invalidateNameSpace(GlobalCacheKey.NS_USER_BY_ID);

        // Cache miss
        start = System.nanoTime();
        user1 = CacheManager.get(GlobalCacheKey.NS_USER_BY_ID, id1);
        user2 = CacheManager.get(GlobalCacheKey.NS_USER_BY_ID, id2);
        end = System.nanoTime();
        Logger.info("[nameSpaceTest]: Cache miss in %dns", end - start);
        assertEquals(id1, user1.id);
        assertEquals(id2, user2.id);
    }

    @Test
    public void functionTest() {

        final String testUsername = "testUser4";
        CacheManager.delete(GlobalCacheKey.USER, testUsername);

        final User testUser = new User(testUsername, "").save();

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

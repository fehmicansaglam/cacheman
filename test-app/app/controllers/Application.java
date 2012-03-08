package controllers;

import play.Logger;
import play.mvc.Controller;
import cache.GlobalCacheKey;
import cachemanager.cache.CacheManager;

public class Application extends Controller {

    public static void index() {

        Long userCount = CacheManager.get(GlobalCacheKey.USER_COUNT);
        Logger.info("User Count: %d", userCount);
        render();
    }

}
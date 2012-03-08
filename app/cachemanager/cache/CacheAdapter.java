package cachemanager.cache;

import java.io.Serializable;
import java.lang.reflect.Method;

import play.db.jpa.GenericModel;
import play.exceptions.UnexpectedException;
import cachemanager.common.Function;

public class CacheAdapter {

    private final Function function;

    private final Method method;

    public final String expire;

    public CacheAdapter(Function<? extends Object, ? extends Serializable> function, String expire) {

        this.function = function;
        this.method = null;
        this.expire = expire;
    }

    public CacheAdapter(Class<? extends GenericModel> clazz, String method, String expire) {

        try {
            this.method = clazz.getDeclaredMethod(method);
            this.function = null;
            this.expire = expire;
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }
    }

    public <P extends Object, R extends Serializable> R get(P input) {

        if (this.method != null) {
            try {
                return (R) this.method.invoke(null);
            } catch (Exception e) {
                throw new UnexpectedException(e);
            }
        } else if (this.function != null) {
            return (R) this.function.apply(input);
        } else {
            throw new UnexpectedException("Don't know how to get the value");
        }
    }
}

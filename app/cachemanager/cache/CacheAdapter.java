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

    public CacheAdapter(final Function<? extends Object, ? extends Serializable> function, final String expire) {

        this.function = function;
        this.method = null;
        this.expire = expire;
    }

    public CacheAdapter(final Class<? extends GenericModel> clazz, final String method, final String expire) {

        try {
            this.method = clazz.getDeclaredMethod(method);
            this.function = null;
            this.expire = expire;
        } catch (final Exception e) {
            throw new UnexpectedException(e);
        }
    }

    public <P extends Object, R extends Serializable> R get(final P input) {

        if (this.method != null) {
            try {
                return (R) this.method.invoke(null);
            } catch (final Exception e) {
                throw new UnexpectedException(e);
            }
        } else if (this.function != null) {
            return (R) this.function.apply(input);
        } else {
            throw new UnexpectedException("Don't know how to get the value");
        }
    }
}

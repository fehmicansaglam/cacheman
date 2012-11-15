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

    private final Class<?>[] parameterTypes;

    public CacheAdapter(final Function<? extends Object, ? extends Serializable> function,
            final String expire) {

        this.function = function;
        this.method = null;
        this.expire = expire;
        this.parameterTypes = null;
    }

    public CacheAdapter(final Class<? extends GenericModel> clazz, final String method,
            final String expire, final Class<?>... parameterTypes) {

        try {
            this.method = clazz.getDeclaredMethod(method, parameterTypes);
            this.method.setAccessible(true);
            this.function = null;
            this.expire = expire;
            this.parameterTypes = parameterTypes;
        } catch (final Exception e) {
            throw new UnexpectedException(e);
        }
    }

    public <P extends Object, R extends Serializable> R get(final P input) {

        if (this.method != null) {
            try {
                if (this.parameterTypes == null || this.parameterTypes.length == 0) {
                    return (R) this.method.invoke(null);
                } else {
                    return (R) this.method.invoke(null, input);
                }
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

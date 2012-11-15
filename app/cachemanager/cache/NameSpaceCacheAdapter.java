package cachemanager.cache;

import java.io.Serializable;

import play.db.jpa.GenericModel;
import play.libs.Codec;
import cachemanager.common.Function;

public class NameSpaceCacheAdapter extends CacheAdapter {

    public final String nameSpace;

    public NameSpaceCacheAdapter(final String nameSpace, final Class<? extends GenericModel> clazz,
            final String method, final String expire, final Class<?>... parameterTypes) {
        super(clazz, method, expire, parameterTypes);
        this.nameSpace = Codec.encodeBASE64(nameSpace);
    }

    public NameSpaceCacheAdapter(final String nameSpace,
            final Function<? extends Object, ? extends Serializable> function, final String expire) {
        super(function, expire);
        this.nameSpace = Codec.encodeBASE64(nameSpace);
    }

}

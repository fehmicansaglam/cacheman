package cachemanager.common;

public interface Function<P, R> {

    R apply(P input);

    @Override
    boolean equals(Object object);
}
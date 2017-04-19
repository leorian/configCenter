package com.luotian.json;

public interface ClassLocator {
    public Class locate(ObjectBinder context, Path currentPath) throws ClassNotFoundException;
}

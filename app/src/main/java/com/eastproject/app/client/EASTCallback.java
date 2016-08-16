package com.eastproject.app.client;

/**
 * Created by TMD on 2016/8/8.
 */
public interface EASTCallback<T> {
    void done(T obj, EASTException e);
}

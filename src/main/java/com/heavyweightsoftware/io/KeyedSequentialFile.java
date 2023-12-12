package com.heavyweightsoftware.io;

public interface KeyedSequentialFile {
    boolean delete(String key);

    void deleteFile();

    void insert(String key, String data);

    String[] update(String key, String data);

    String[] get(String key);
}

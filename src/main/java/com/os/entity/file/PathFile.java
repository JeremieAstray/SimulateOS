package com.os.entity.file;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Jeremie on 2014/9/23.
 */
public class PathFile extends FileBase {

    private Map<String,FileBase> files = new TreeMap<>();

    public Map<String, FileBase> getFiles() {
        return files;
    }

    public void setFiles(Map<String, FileBase> files) {
        this.files = files;
    }
}

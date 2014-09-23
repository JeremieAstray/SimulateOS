package com.os.entity.file;

import java.security.Timestamp;

/**
 * Created by Jeremie on 2014/9/23.
 */
public class File extends FileBase {

    private Timestamp createTime;
    private Timestamp changeTime;
    private int size;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

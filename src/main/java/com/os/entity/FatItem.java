package com.os.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Jeremie on 2014/11/20.
 */
public class FatItem {
    private IntegerProperty diskNumber1 ;
    private IntegerProperty diskNumber2;

    public FatItem(int diskNumber1, int diskNumber2) {
        this.diskNumber1 = new SimpleIntegerProperty(diskNumber1);
        this.diskNumber2 = new SimpleIntegerProperty(diskNumber2);
    }

    public int getDiskNumber1() {
        return diskNumber1.get();
    }

    public IntegerProperty diskNumber1Property() {
        return diskNumber1;
    }

    public void setDiskNumber1(int diskNumber1) {
        this.diskNumber1.set(diskNumber1);
    }

    public int getDiskNumber2() {
        return diskNumber2.get();
    }

    public IntegerProperty diskNumber2Property() {
        return diskNumber2;
    }

    public void setDiskNumber2(int diskNumber2) {
        this.diskNumber2.set(diskNumber2);
    }
}

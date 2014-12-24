package com.os.entity;

import javafx.scene.image.Image;

/**
 * Created by Jeremie on 2014/12/7.
 */
public class FileTreeItem {
    private String name;
    private boolean attribute;

    public FileTreeItem(String name,boolean attribute) {
        this.name = name;
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAttribute() {
        return attribute;
    }

    public void setAttribute(boolean attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return name ;
    }
}

package com.os.entity;

import javafx.scene.image.Image;

/**
 * Created by Jeremie on 2014/12/7.
 */
public class FileTreeItem {
    public Image image;
    public String name;

    public FileTreeItem(Image image, String name) {
        this.image = image;
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

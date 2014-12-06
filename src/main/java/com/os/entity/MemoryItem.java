package com.os.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Jeremie on 2014/12/6.
 */
public class MemoryItem {
    private StringProperty memory;
    public MemoryItem(String memory){
        this.memory = new SimpleStringProperty(memory);
    }

    public String getMemory() {
        return memory.get();
    }

    public StringProperty memoryProperty() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory.set(memory);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package operate;

/**
 *
 * @author Administrator
 */
public class CatalogueItem {
    
    private String name;
    private String type;
    private int[] attribute;
    private int initialDiskNumber;
    private int length;

    public CatalogueItem() {

    }

    public CatalogueItem(String name, String type, int[] attribute, int initialDiskNumber, int length) {
        this.name = name;  //注意这里的名字是用‘$’号来表示空字符的，所以和用户输入的名字交互时记得替换回空字符
        this.type = type;  //这里的类型的格式为".xxx"
        this.attribute = attribute;
        this.initialDiskNumber = initialDiskNumber;
        this.length = length;  //暂定为盘块数
    }

    /**
     * ***************************************************************************
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInitialDiskNumber() {
        return initialDiskNumber;
    }

    public void setInitialDiskNumber(int initialDiskNumber) {
        this.initialDiskNumber = initialDiskNumber;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int[] getAttribute() {
        return attribute;
    }

    public void setAttribute(int[] attribute) {
        this.attribute = attribute;
    }

}

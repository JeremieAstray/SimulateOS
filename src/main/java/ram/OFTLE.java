/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ram;

/**
 *
 * @author Administrator
 */
public class OFTLE {
    
    public static final int READ_MODE = 0;
    public static final int WRITE_MODE = 1;
    
    private String absoultRoute; //文件绝对路径名
    private int[] attribute; //文件的属性，因为是操作层的属性，所以用数组来记录
    private int firstDiskNumber; //文件起始盘块号
    private int fileLength; //文件长度，文件占用的字节数
    private int flag;  //操作类型，用“0”表示以读操作方式打开文件，用“1”表示以写操作方式打开文件
    private Pointer read;  //读文件的位置，文件打开时dnum 为文件起始盘块号，bnum 为“0”
    private Pointer write; //写文件的位置，文件刚建立时dnum 为文件起始盘块号，bnum 为“0 ，打开文件时dnum 和bnum 为文件的末尾位置

    public OFTLE(String absoultRoute, int[] attribute, int firstDiskNumber, int fileLength, int flag, Pointer read, Pointer write) {
        this.absoultRoute = absoultRoute;
        this.attribute = attribute;
        this.firstDiskNumber = firstDiskNumber;
        this.fileLength = fileLength;
        this.flag = flag;
        this.read = read;
        this.write = write;
    }

    /**
     * ***************************************************************************
     */
    public String getAbsoultRoute() {
        return absoultRoute;
    }

    public void setAbsoultRoute(String absoultRoute) {
        this.absoultRoute = absoultRoute;
    }

    public int getFirstDiskNumber() {
        return firstDiskNumber;
    }

    public void setFirstDiskNumber(int firstDiskNumber) {
        this.firstDiskNumber = firstDiskNumber;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Pointer getRead() {
        return read;
    }

    public void setRead(Pointer read) {
        this.read = read;
    }

    public Pointer getWrite() {
        return write;
    }

    public void setWrite(Pointer write) {
        this.write = write;
    }

    public int[] getAttribute() {
        return attribute;
    }

    public void setAttribute(int[] attribute) {
        this.attribute = attribute;
    }
}

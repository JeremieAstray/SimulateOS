package com.os.ram;

import java.util.ArrayList;

/**
 *
 * @author FeiFan Liang
 */
public class RAMManager {

    private int[] fat = new int[128];
    private FATManager fatManager = new FATManager();
    private ArrayList<OFTLE> oftleList;
    private ArrayList<Character> ramSpaceForRead;
    private ArrayList<Character> ramSpaceForWrite;
    private char[] buffer1;  //缓冲暂时这样先
    private char[] buffer2;  //缓冲暂时这样先
    
    public RAMManager() {

    }

    public void initRam() {
        this.setFat(this.getFatManager().returnTheFAT());
        this.setOftleList(new ArrayList<>());
        this.setRamSpaceForRead(new ArrayList<>());
        this.setRamSpaceForWrite(new ArrayList<>());
        this.setBuffer1(new char[64]);
        this.setBuffer2(new char[64]);
    }

    /**
     * @return the fat
     */
    public int[] getFat() {
        return fat;
    }

    /**
     * @param fat the fat to set
     */
    public void setFat(int[] fat) {
        this.fat = fat;
    }

    /**
     * @return the fatManager
     */
    public FATManager getFatManager() {
        return fatManager;
    }

    /**
     * @param fatManager the fatManager to set
     */
    public void setFatManager(FATManager fatManager) {
        this.fatManager = fatManager;
    }

    /**
     * @return the oftleList
     */
    public ArrayList<OFTLE> getOftleList() {
        return oftleList;
    }

    /**
     * @param oftleList the oftleList to set
     */
    public void setOftleList(ArrayList<OFTLE> oftleList) {
        this.oftleList = oftleList;
    }

    /**
     * @return the ramSpaceForRead
     */
    public ArrayList<Character> getRamSpaceForRead() {
        return ramSpaceForRead;
    }

    /**
     * @param ramSpaceForRead the ramSpaceForRead to set
     */
    public void setRamSpaceForRead(ArrayList<Character> ramSpaceForRead) {
        this.ramSpaceForRead = ramSpaceForRead;
    }

    /**
     * @return the ramSpaceForWrite
     */
    public ArrayList<Character> getRamSpaceForWrite() {
        return ramSpaceForWrite;
    }

    /**
     * @param ramSpaceForWrite the ramSpaceForWrite to set
     */
    public void setRamSpaceForWrite(ArrayList<Character> ramSpaceForWrite) {
        this.ramSpaceForWrite = ramSpaceForWrite;
    }

    /**
     * @return the buffer1
     */
    public char[] getBuffer1() {
        return buffer1;
    }

    /**
     * @param buffer1 the buffer1 to set
     */
    public void setBuffer1(char[] buffer1) {
        this.buffer1 = buffer1;
    }

    /**
     * @return the buffer2
     */
    public char[] getBuffer2() {
        return buffer2;
    }

    /**
     * @param buffer2 the buffer2 to set
     */
    public void setBuffer2(char[] buffer2) {
        this.buffer2 = buffer2;
    }

}

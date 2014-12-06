package com.os.ram;

import java.util.ArrayList;

/**
 *
 * @author FeiFan Liang
 */
public class RAMManager {

    private int[] fat = new int[128];
    private ArrayList<OFTLE> oftleList;
    private ArrayList<Character> ramSpaceForRead;
    private ArrayList<Character> ramSpaceForWrite;
    private char[] buffer1;  //缓冲暂时这样先
    private char[] buffer2;  //缓冲暂时这样先

    private FATManager fatManager = new FATManager();

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

    public int[] getFat() {
        return fat;
    }

    public void setFat(int[] fat) {
        this.fat = fat;
    }

    public FATManager getFatManager() {
        return fatManager;
    }

    public void setFatManager(FATManager fatManager) {
        this.fatManager = fatManager;
    }

    public ArrayList<OFTLE> getOftleList() {
        return oftleList;
    }

    public void setOftleList(ArrayList<OFTLE> oftleList) {
        this.oftleList = oftleList;
    }

    public ArrayList<Character> getRamSpaceForRead() {
        return ramSpaceForRead;
    }

    public void setRamSpaceForRead(ArrayList<Character> ramSpaceForRead) {
        this.ramSpaceForRead = ramSpaceForRead;
    }

    public ArrayList<Character> getRamSpaceForWrite() {
        return ramSpaceForWrite;
    }

    public void setRamSpaceForWrite(ArrayList<Character> ramSpaceForWrite) {
        this.ramSpaceForWrite = ramSpaceForWrite;
    }

    public char[] getBuffer1() {
        return buffer1;
    }

    public void setBuffer1(char[] buffer1) {
        this.buffer1 = buffer1;
    }

    public char[] getBuffer2() {
        return buffer2;
    }

    public void setBuffer2(char[] buffer2) {
        this.buffer2 = buffer2;
    }

}

package com.os.ram;

/**
 *
 * @author FeiFan Liang
 */
public class Pointer {

    private int d_num;
    private int b_num;
    public static final int MAX_B_NUM = 64;

    public Pointer(int d_num, int b_num) {
        this.d_num = d_num;
        this.b_num = b_num;
    }

    /**
     * ***************************************************************************
     */
    public int getD_num() {
        return d_num;
    }

    public void setD_num(int d_num) {
        this.d_num = d_num;
    }

    public int getB_num() {
        return b_num;
    }

    public void setB_num(int b_num) {
        this.b_num = b_num;
    }
}

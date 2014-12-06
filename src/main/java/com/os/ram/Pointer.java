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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pointer pointer = (Pointer) o;

        if (b_num != pointer.b_num) return false;
        if (d_num != pointer.d_num) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = d_num;
        result = 31 * result + b_num;
        return result;
    }

    public void setB_num(int b_num) {
        this.b_num = b_num;
    }
}

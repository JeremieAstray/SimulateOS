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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ram;

import disk.DiskManager;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class FATManager {

    public static final int END_FLAG = 255;

    public FATManager() {

    }

    public int[] returnTheFAT() {
        int[] fat = new int[128];
        try {
            DiskManager diskManager = new DiskManager();

            ArrayList<Integer> fat1 = diskManager.readRealDisk(0);
            ArrayList<Integer> fat2 = diskManager.readRealDisk(1);

            int i;
            for (i = 0; i < fat1.size(); i++) {
                fat[i] = fat1.get(i);
            }
            for (int j = 0; j < fat2.size(); i++, j++) {
                fat[i] = fat2.get(j);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FATManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fat;
    }

    public void saveFATToDisk(int[] fat) {
        try {
            ArrayList<Integer> fat1 = new ArrayList<>();
            ArrayList<Integer> fat2 = new ArrayList<>();

            int i;
            for (i = 0; i < fat.length / 2; i++) {
                fat1.add(fat[i]);
            }
            for (; i < fat.length; i++) {
                fat2.add(fat[i]);
            }

            DiskManager diskManager = new DiskManager();
            diskManager.writeRealDisk(0, fat1);
            diskManager.writeRealDisk(1, fat2);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FATManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int updateFATForMallocANewDisk(int[] fat) {
        int d_n = 3;
        boolean isDiskFull = true;
        for (int i = 3; i < fat.length; i++) {
            if (fat[i] == 0) {
                d_n = i;
                isDiskFull = false;
                break;
            }
        }
        if (!isDiskFull) {
            fat[d_n] = FATManager.END_FLAG;
            return d_n;
        } else {
            return -1;
        }
    }

    public void printFAT(int[] fat) {
        System.out.print("项：  ");
        for (int i = 0; i < fat.length; i++) {
            System.out.printf("%-5d", i);
        }
        System.out.println("");
        System.out.print("信息：");
        for (int i = 0; i < fat.length; i++) {
            System.out.printf("%-5d", fat[i]);
        }
        System.out.println("");
    }

}

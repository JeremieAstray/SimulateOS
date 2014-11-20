package com.os.ram;

import com.os.disk.DiskManager;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author FeiFan Liang
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
            ex.printStackTrace();
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
            ex.printStackTrace();
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

}

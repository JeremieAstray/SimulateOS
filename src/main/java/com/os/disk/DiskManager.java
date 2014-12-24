package com.os.disk;

import com.os.operate.CatalogueItem;
import com.os.ram.FATManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author FeiFan Liang
 */
public class DiskManager {

    DiskCodeInterpreter diskCodeInterpreter = new DiskCodeInterpreter();
    public static final int ORIGINAL_DISK_NUMBER = 2;
    public static final int C_DISK_NUMBER = 3;
    public static final int D_DISK_NUMBER = 4;
    public static final int E_DISK_NUMBER = 5;
    public static final int F_DISK_NUMBER = 6;
    public static final int G_DISK_NUMBER = 7;
    public static final int MAX_DISK_SIZE = 64;

    public DiskManager() {

    }

    public ArrayList<Integer> readRealDisk(int diskNumber) throws FileNotFoundException {
        File disk = new File(diskNumber + ".txt");
        ArrayList<Integer> diskOriginInformation = new ArrayList<>();

        try (Scanner input = new Scanner(disk)) {
            while (input.hasNext()) {
                diskOriginInformation.add(input.nextInt());
            }
            input.close();
        }

        return diskOriginInformation;
    }

    public void writeRealDisk(int diskNumber, ArrayList<Integer> diskOriginInformation) throws FileNotFoundException {
        try (PrintWriter output = new PrintWriter(new File(diskNumber + ".txt"))) {
            for (int i = 0; i < diskOriginInformation.size(); i++) {
                output.println(diskOriginInformation.get(i));
            }
            output.close();
        }
    }

    public void clearRealDisk(int diskNumber) {
        ArrayList<Integer> diskOriginInformation = new ArrayList<>();
        try {
            this.writeRealDisk(diskNumber, diskOriginInformation);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<CatalogueItem> getCatalogueItemFormatInformationFromDisk(int diskNumber) {
        ArrayList<Integer> diskOriginInformation = null;
        try {
            diskOriginInformation = this.readRealDisk(diskNumber);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return this.diskCodeInterpreter.explainDiskInformationForCatalogueFormat(diskOriginInformation);
    }

    public boolean saveCatalogueItemFormatInformationToDiskForAddCatalogueItem(int diskNumber, ArrayList<CatalogueItem> catalogueItemList, int[] fat) {
        FATManager fatManager = new FATManager();

        ArrayList<Integer> diskOriginInformation = this.diskCodeInterpreter.turnCatalogueFormatInformationToDiskInformation(catalogueItemList);
        ArrayList<Integer> tempDiskOriginInformation = new ArrayList<>();
        int temp = diskNumber, index = 0;
        int d_length = diskOriginInformation.size();

        while (fat[temp] != FATManager.END_FLAG) {
            d_length -= 64;
            index += 64;  //注意一个盘块里面有64个字节
            temp = fat[temp];
        }

        d_length -= 64;
        for (int i = index; i < diskOriginInformation.size() && i < index + 64; i++) {  //本盘结束情形
            tempDiskOriginInformation.add(diskOriginInformation.get(i));
        }
        try {
            this.writeRealDisk(temp, tempDiskOriginInformation);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        /* 增加目录项时申请一个新盘情形的存储方式 */
        tempDiskOriginInformation = new ArrayList<>();
        if (d_length > 0) {
            int t_temp = temp;
            if ((temp = fatManager.updateFATForMallocANewDisk(fat)) == -1) {  //磁盘已满，没法增加新的目录项
                return false;
            } else {
                for (int i = diskOriginInformation.size() - 8; i < diskOriginInformation.size(); i++) {
                    tempDiskOriginInformation.add(diskOriginInformation.get(i));
                }
                /* 最多是超一个目录项，8个字节 */

                try {
                    this.writeRealDisk(temp, tempDiskOriginInformation);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                fat[t_temp] = temp;  //连接信息必需记得更新

                return true;
            }
        }

        return true;
    }

    public void saveCatalogueItemFormatInformationToDiskForDeleteCatalogueItem(int diskNumber, ArrayList<CatalogueItem> catalogueItemList, int[] fat) {
        ArrayList<Integer> diskOriginInformation = this.diskCodeInterpreter.turnCatalogueFormatInformationToDiskInformation(catalogueItemList);
        ArrayList<Integer> tempDiskOriginInformation = new ArrayList<>();
        int temp = diskNumber, index = 0, count = 0, last_temp = diskNumber;

        while (index < diskOriginInformation.size()) {
            tempDiskOriginInformation.add(diskOriginInformation.get(index));
            count++;
            index++;

            if (count == 64) {
                count = 0;
                try {
                    this.writeRealDisk(temp, tempDiskOriginInformation);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                tempDiskOriginInformation = new ArrayList<>();
                last_temp = temp;
                temp = fat[temp];
            }
        }
        if (count != 0) {
            try {
                this.writeRealDisk(temp, tempDiskOriginInformation);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            last_temp = temp;
            temp = fat[temp];
        }
        
        int t_temp;
        while (temp != FATManager.END_FLAG) {  //删掉目录项的时候释放磁盘的情形
            t_temp = fat[temp];
            fat[temp] = 0;
            this.clearRealDisk(temp);
            temp = t_temp;
        }
        fat[last_temp] = FATManager.END_FLAG;  //连接信息必需记得更新
    }
    
    public void saveCatalogueItemFormatInformationToDisk(int diskNumber, ArrayList<CatalogueItem> catalogueItemList, int[] fat){
        ArrayList<Integer> diskOriginInformation = this.diskCodeInterpreter.turnCatalogueFormatInformationToDiskInformation(catalogueItemList);
        ArrayList<Integer> tempDiskOriginInformation = new ArrayList<>();
        
        int temp = diskNumber, index = 0, count = 0;
        while (index < diskOriginInformation.size()) {
            tempDiskOriginInformation.add(diskOriginInformation.get(index));
            count++;
            index++;

            if (count == 64) {
                count = 0;
                try {
                    this.writeRealDisk(temp, tempDiskOriginInformation);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                tempDiskOriginInformation = new ArrayList<>();
                temp = fat[temp];
            }
        }
        if (count != 0) {
            try {
                this.writeRealDisk(temp, tempDiskOriginInformation);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            temp = fat[temp];
        }
    }
    
    public ArrayList<Character> getFileFormatInformationFromDisk(int diskNumber) {
        ArrayList<Integer> diskOriginInformation = null;
        try {
            diskOriginInformation = this.readRealDisk(diskNumber);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        ArrayList<Character> fileInformation = this.diskCodeInterpreter.explainDiskInformationForFileFormat(diskOriginInformation);
        return fileInformation;
    }

    public void saveFileFormatInformationToDisk(int diskNumber, ArrayList<Character> fileInformation) {
        ArrayList<Integer> diskOriginInformation = this.diskCodeInterpreter.turnFileFormatInformationToDiskInformation(fileInformation);
        try {
            this.writeRealDisk(diskNumber, diskOriginInformation);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public int getUsingSizeOfCurrentDisk(int diskNumber) {
        ArrayList<Integer> diskOriginInformation = null;
        try {
            diskOriginInformation = this.readRealDisk(diskNumber);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return diskOriginInformation.size();
    }
}

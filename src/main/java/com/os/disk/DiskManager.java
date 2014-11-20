package com.os.disk;

import com.os.operate.CatalogueItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author FeiFan Liang
 */
public class DiskManager {

    DiskCodeInterpreter diskCodeInterpreter = new DiskCodeInterpreter();
    public static final int ROOT_DISK_NUMBER = 2;
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

    public void saveCatalogueItemFormatInformationToDisk(int diskNumber, ArrayList<CatalogueItem> catalogueItemList) {
        ArrayList<Integer> diskOriginInformation = this.diskCodeInterpreter.turnCatalogueFormatInformationToDiskInformation(catalogueItemList);
        try {
            this.writeRealDisk(diskNumber, diskOriginInformation);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
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

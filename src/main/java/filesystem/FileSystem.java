/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystem;

import disk.DiskManager;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import operate.CatalogueItem;
import operate.DirectoryOpreator;
import operate.FileOperator;
import ram.FATManager;
import ram.OFTLE;
import ram.RAMManager;

/**
 *
 * @author Administrator
 */
public class FileSystem {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here

        /* 初始化主存，建立操作器 */
        RAMManager ramManager = new RAMManager();
        ramManager.initRam();
        DirectoryOpreator directoryOpreator = new DirectoryOpreator();
        FileOperator fileOperator = new FileOperator();
        DiskManager diskManager = new DiskManager();
        FATManager fatManager = new FATManager();
        String something;
        int[] attribute = {0, 0, 1, 0, 0, 0, 0, 0};
//
//        /* 初始化磁盘信息 */
//        for (int i = 0; i < 128; i++) {
//            java.io.File file = new java.io.File(i + ".txt");
//            java.io.PrintWriter output = new java.io.PrintWriter(file);
//            output.close();
//        }
//
//        int[] testFat = new int[128];
//        testFat[0] = 1;
//        testFat[1] = FATManager.END_FLAG;
//        testFat[2] = FATManager.END_FLAG;
//        for (int i = 3; i < testFat.length; i++) {
//            testFat[i] = 0;
//        }
//        fatManager.saveFATToDisk(testFat);
//
//        /* 在根目录创建四个目录 */
//        DirectoryOpreator DirectoryOpreator = new DirectoryOpreator();
//        for (int i = 0; i < 4; i++) {
//            DirectoryOpreator.makeDirectory("/" + i, ramManager.getFat());  //每操作一次fat一定要save一次
//        }
//
//        /* 在根目录创建三个文件 */
//        for (int i = 0; i < 3; i++) {
//            if (fileOperator.creatFile("/" + i + ".txt", attribute, ramManager.getFat(), ramManager.getOftleList())) {
//                fileOperator.closeFile("/" + i + ".txt", ramManager.getOftleList());
//            }
//        }
//        if (fileOperator.creatFile("/3.txt", attribute, ramManager.getFat(), ramManager.getOftleList())) {
//            fileOperator.closeFile("/3.txt", ramManager.getOftleList());
//        }
//
//        /* 写入一些东西进文件 */
//        something = "123456789\nabcdefghijk\nABCDEFGHIJK\nHello, this is my first time to write something to file, please pray me, help, help!!";
//        for (int i = 0; i < something.length(); i++) {
//            ramManager.getRamSpaceForWrite().add(something.charAt(i));
//        }
//        int index = 0, b1_index = 0;
//        while (index < ramManager.getRamSpaceForWrite().size()) {
//            if (b1_index == ramManager.getBuffer1().length) {
//                fileOperator.writeFile("/2.txt", ramManager.getBuffer1(), b1_index, ramManager.getFat(), ramManager.getOftleList());
//                b1_index = 0;
//            } else {
//                ramManager.getBuffer1()[b1_index] = ramManager.getRamSpaceForWrite().get(index);
//                b1_index++;
//                index++;
//            }
//        }
//        if (b1_index > 0) {  //缓冲区内还有内容，还要再写一遍
//            fileOperator.writeFile("/2.txt", ramManager.getBuffer1(), b1_index, ramManager.getFat(), ramManager.getOftleList());
//        }
//        ramManager.setRamSpaceForWrite(new ArrayList<>());
//        something = "\nOK, the first time, we have succeed, now let's append something new.";
//        for (int i = 0; i < something.length(); i++) {
//            ramManager.getRamSpaceForWrite().add(something.charAt(i));
//        }
//        index = 0;
//        b1_index = 0;
//        while (index < ramManager.getRamSpaceForWrite().size()) {
//            if (b1_index == ramManager.getBuffer1().length) {
//                fileOperator.writeFile("/2.txt", ramManager.getBuffer1(), b1_index, ramManager.getFat(), ramManager.getOftleList());
//                b1_index = 0;
//            } else {
//                ramManager.getBuffer1()[b1_index] = ramManager.getRamSpaceForWrite().get(index);
//                b1_index++;
//                index++;
//            }
//        }
//        if (b1_index > 0) {  //缓冲区内还有内容，还要再写一遍
//            fileOperator.writeFile("/2.txt", ramManager.getBuffer1(), b1_index, ramManager.getFat(), ramManager.getOftleList());
//        }
//        fileOperator.closeFile("/2.txt", ramManager.getOftleList());  //记得每一次写入东西都必需关掉文件
//        
//        /* 读文件测试 */
//        DirectoryOpreator DirectoryOpreator = new DirectoryOpreator();
//        FileOperator fileOperator = new FileOperator();
//        fileOperator.readFile("/2.txt", 64, ramManager.getRamSpaceForRead(), ramManager.getFat(), ramManager.getOftleList());
//        System.out.println(ramManager.getRamSpaceForRead());
//        fileOperator.readFile("/2.txt", 64, ramManager.getRamSpaceForRead(), ramManager.getFat(), ramManager.getOftleList());
//        System.out.println(ramManager.getRamSpaceForRead());
//        fileOperator.readFile("/2.txt", 64, ramManager.getRamSpaceForRead(), ramManager.getFat(), ramManager.getOftleList());
//        System.out.println(ramManager.getRamSpaceForRead());
//        fileOperator.printFileInformation("/2.txt", ramManager.getFat(), ramManager.getOftleList());
//        fileOperator.closeFile("/2.txt", ramManager.getOftleList());
//        fileOperator.printFileInformation("/2.txt", ramManager.getFat(), ramManager.getOftleList());
//        DirectoryOpreator.printDirectory("/");
//        
//          /* 综合测试 */
//        DirectoryOpreator DirectoryOpreator = new DirectoryOpreator();
//        FileOperator fileOperator = new FileOperator();
//        directoryOpreator.makeDirectory("/0/0", ramManager.getFat());
//        for (int i = 0; i < 8; i++) {
//            if (fileOperator.creatFile("/0/0" + "/" + i + ".txt", attribute, ramManager.getFat(), ramManager.getOftleList())) {
//                fileOperator.closeFile("/0/0" + "/" + i + ".txt", ramManager.getOftleList());
//            }
//        }
//
//        for (int k = 0; k < 8; k++) {
//            something = "this is the " + k + ".txt.\n" + "123456789\nabcdefghijk\nABCDEFGHIJK\nHello, this is my first time to write something to file, please pray me, help, help!!";
//            ramManager.setRamSpaceForWrite(new ArrayList<>());
//            for (int i = 0; i < something.length(); i++) {
//                ramManager.getRamSpaceForWrite().add(something.charAt(i));
//            }
//            index = 0;
//            b1_index = 0;
//            while (index < ramManager.getRamSpaceForWrite().size()) {
//                if (b1_index == ramManager.getBuffer1().length) {
//                    fileOperator.writeFile("/0/0" + "/" + k + ".txt", ramManager.getBuffer1(), b1_index, ramManager.getFat(), ramManager.getOftleList());
//                    b1_index = 0;
//                } else {
//                    ramManager.getBuffer1()[b1_index] = ramManager.getRamSpaceForWrite().get(index);
//                    b1_index++;
//                    index++;
//                }
//            }
//            if (b1_index > 0) {  //缓冲区内还有内容，还要再写一遍
//                fileOperator.writeFile("/0/0" + "/" + k + ".txt", ramManager.getBuffer1(), b1_index, ramManager.getFat(), ramManager.getOftleList());
//            }
//            fileOperator.closeFile("/0/0" + "/" + k + ".txt", ramManager.getOftleList());
//        }

//        for (int k = 0; k < 8; k++) {
//            ramManager.setRamSpaceForRead(new ArrayList<>());
//            fileOperator.readFile("/0/0" + "/" + k + ".txt", 64, ramManager.getRamSpaceForRead(), ramManager.getFat(), ramManager.getOftleList());
//            System.out.println(ramManager.getRamSpaceForRead());
//            ramManager.setRamSpaceForRead(new ArrayList<>());
//            fileOperator.readFile("/0/0" + "/" + k + ".txt", 64, ramManager.getRamSpaceForRead(), ramManager.getFat(), ramManager.getOftleList());
//            System.out.println(ramManager.getRamSpaceForRead());
//            ramManager.setRamSpaceForRead(new ArrayList<>());
//            fileOperator.readFile("/0/0" + "/" + k + ".txt", 64, ramManager.getRamSpaceForRead(), ramManager.getFat(), ramManager.getOftleList());
//            System.out.println(ramManager.getRamSpaceForRead());
//            fileOperator.closeFile("/0/0" + "/" + k + ".txt", ramManager.getOftleList());
//        }
//
//        for (int k = 0; k < 8; k++) {
//            fileOperator.printFileInformation("/0/0" + "/" + k + ".txt", ramManager.getFat(), ramManager.getOftleList());
//        }
//
//        DirectoryOpreator.printDirectory("/0");
//        System.out.println("华丽的分割线+华丽的分割线+华丽的分割线+华丽的分割线");
//        DirectoryOpreator.printDirectory("/0/0");
//        
//        /* 测试文件属性修改 */
//        if (fileOperator.change("/2.txt", FileOperator.READ_ONLY_MODE, ramManager.getOftleList())) {
//            System.out.println("修改为只读模式成功！");
//        }
//        directoryOpreator.printDirectory("/");
//        if (fileOperator.change("/2.txt", FileOperator.COMMON_MODE, ramManager.getOftleList())) {
//            System.out.println("修改为普通模式成功！");
//        }
//        fileOperator.openFile("/2.txt", OFTLE.WRITE_MODE, ramManager.getFat(), ramManager.getOftleList());
//        String something = "1234\n";
//        for (int i = 0; i < something.length(); i++) {
//            ramManager.getRamSpaceForWrite().add(something.charAt(i));
//        }
//        if (fileOperator.writeFile("/2.txt", ramManager.getBuffer1(), 5, ramManager.getFat(), ramManager.getOftleList())) {
//            fileOperator.closeFile("/2.txt", ramManager.getOftleList());  //记得每一次写入东西都必需关掉文件
//        }
//        
//        /* 测试删除文件 */
//        fileOperator.deleteFile("/2.txt", ramManager.getFat(), ramManager.getOftleList());
//        DirectoryOpreator.printDirectory("/");
//        fileOperator.printFileInformation("/2.txt", ramManager.getFat(), ramManager.getOftleList());
//        for (int i = 1; i < 3; i++) {
//            directoryOpreator.makeDirectory("/0/" + i, ramManager.getFat());  //每操作一次fat一定要save一次
//        }
//        int[] attribute = {0, 0, 1, 0, 0, 0, 0, 0};
//        for (int i = 0; i < 3; i++) {
//            if (fileOperator.creatFile("/0/" + i + ".txt", attribute, ramManager.getFat(), ramManager.getOftleList())) {
//                fileOperator.closeFile("/0/" + i + ".txt", ramManager.getOftleList());
//            }
//        }
//      
//        /* 测试删除任意目录 */
//        fatManager.printFAT(ramManager.getFat());
//        directoryOpreator.removeDirectory("/0", ramManager.getFat(), ramManager.getOftleList());
//
//        directoryOpreator.printDirectory("/");
//        directoryOpreator.printDirectory("/0");
//        directoryOpreator.printDirectory("/0/0");
//
//        fatManager.printFAT(ramManager.getFatManager().returnTheFAT());
    }

}

package com.os.operate;

import com.os.disk.DiskManager;
import java.util.ArrayList;
import com.os.ram.FATManager;
import com.os.ram.OFTLE;
import com.os.ram.OFTLEManager;
import com.os.ram.Pointer;

/**
 *
 * @author FeiFan Liang
 */
public class FileOperator {

    public static final int COMMON_MODE = 0;
    public static final int READ_ONLY_MODE = 1;

    public String creatFile(String absouletRoute, int[] attribute, int[] fat, ArrayList<OFTLE> oftleList) {
        DirectoryOpreator directoryOpreator = new DirectoryOpreator();
        FATManager fatManager = new FATManager();
        DiskManager diskManager = new DiskManager();

        String fatherDirectory, fileNameType;
        fatherDirectory = absouletRoute.substring(0, absouletRoute.lastIndexOf('/'));
        fileNameType = absouletRoute.substring(absouletRoute.lastIndexOf('/') + 1);
        /* 父目录-子目录搜索法 */

        int diskNumber = directoryOpreator.searchDiskNumberOfStoringCatalogueInformation(fatherDirectory, fat);
        if (diskNumber == -1) {
//            System.out.println("父目录不存在，无法创建新文件");
//            return false;
            return "父目录不存在，无法创建新文件！";
        }

        //ArrayList<CatalogueItem> catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
        ArrayList<CatalogueItem> catalogueItemList = directoryOpreator.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, fat);
//        if (catalogueItemList.size() == 8) {
//            System.out.println("该目录已有八个项，无法在此新建文件！");
//            return false;
//        }

        for (CatalogueItem catalogueItemList1 : catalogueItemList) {
            String tempS = "";
            if (catalogueItemList1.getAttribute()[3] == 1) {
                continue;
            } else {
                for (int k = 0; k < catalogueItemList1.getName().length(); k++) {
                    if (catalogueItemList1.getName().charAt(k) != '$') {
                        tempS += catalogueItemList1.getName().charAt(k);
                    }
                }
                tempS += catalogueItemList1.getType();  //文件的类型格式已经带上点‘.’（ASCII码为46）
            }

//            System.out.println("fileNameType = " + fileNameType + "tempS = " + tempS);
            if (fileNameType.equals(tempS)) {
//                System.out.println("文件名重复，无法在此新建文件！");
//                return false;
                return "文件名重复，无法在此新建文件！";
            }
        }

        if (attribute[0] == 1) {
//            System.out.println("该文件为只读文件，不能建立！");
//            return false;
            return "该文件为只读文件，不能建立！";
        }

        /* 这里的文件类型也是带'.'的 */
        int fileFirstAddress = fatManager.updateFATForMallocANewDisk(fat);
        if (fileFirstAddress == -1) {
//            System.out.println("磁盘已满，无法新建文件");
//            return false;
            return "磁盘已满，无法新建文件！";
        }

        String tempFileName = "";
        int k;
        for (k = 0; k < fileNameType.substring(0, fileNameType.indexOf('.')).length(); k++) {
            tempFileName += fileNameType.substring(0, fileNameType.indexOf('.')).charAt(k);
        }
        while (tempFileName.length() < 3) {
            tempFileName += '$';  //用被是为空字符的特殊字符补够长度
        }

        CatalogueItem item = new CatalogueItem(tempFileName,
                fileNameType.substring(fileNameType.indexOf('.')), attribute, fileFirstAddress, 1);
        catalogueItemList.add(item);

        if (!diskManager.saveCatalogueItemFormatInformationToDiskForAddCatalogueItem(diskNumber, catalogueItemList, fat)) {   //写进磁盘
//            System.out.println("磁盘已满，无法新建文件");
//            fat[fileFirstAddress] = 0;  //记得把给新文件分配的磁盘也归零
//            return false;
            return "磁盘已满，无法新建文件！";
        }

        //填写文件打开表，暂定这样填
        OFTLE oftle = new OFTLE(absouletRoute, attribute,
                fileFirstAddress, 1, 1, new Pointer(fileFirstAddress, 0), new Pointer(fileFirstAddress, 0));
        oftleList.add(oftle);

        fatManager.saveFATToDisk(fat);
//        return true;
        return "";
    }

    public String deleteFile(String absouletRoute, int[] fat, ArrayList<OFTLE> oftleList) {
        OFTLEManager oftleManager = new OFTLEManager();
        DiskManager diskManager = new DiskManager();
        DirectoryOpreator directoryOpreator = new DirectoryOpreator();
        FATManager fatManager = new FATManager();

        String fatherDirectory, fileNameType;
        fatherDirectory = absouletRoute.substring(0, absouletRoute.lastIndexOf('/'));
        fileNameType = absouletRoute.substring(absouletRoute.lastIndexOf('/') + 1);
        /* 父目录-子目录搜索法 */

        int diskNumber;
        if ((diskNumber = directoryOpreator.searchDiskNumberOfStoringCatalogueInformation(fatherDirectory, fat)) == -1) {
//            System.out.println("该文件不存在，无法对其进行删除！");  //父目录不存在
//            return false;
            return "该文件不存在，无法对其进行删除！";
        } else {
            //ArrayList<CatalogueItem> catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
            ArrayList<CatalogueItem> catalogueItemList = directoryOpreator.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, fat);
            boolean isFileExist = false;

            CatalogueItem catalogueItem = null;
            for (CatalogueItem catalogueItemList1 : catalogueItemList) {
                String t_fileCompleteName = "";
                for (int i = 0; i < catalogueItemList1.getName().length(); i++) {
                    if (catalogueItemList1.getName().charAt(i) != '$') {
                        t_fileCompleteName += catalogueItemList1.getName().charAt(i);
                    }
                }
                t_fileCompleteName += catalogueItemList1.getType();
                if (t_fileCompleteName.equals(fileNameType)) {
                    catalogueItem = catalogueItemList1;
                    isFileExist = true;
                    break;
                }
            }
            if (!isFileExist) {
//                System.out.println("该文件不存在，无法对其进行删除！");  //目录下该文件不存在
//                return false;
                return "该文件不存在，无法对其进行删除！";
            }

            if (oftleManager.returnOFTLEIndex(oftleList, absouletRoute) != -1) {
//                System.out.println("文件已被打开，无法对其进行删除！");
//                return false;
                return "文件已被打开，无法对其进行删除！";
            } else {
                int temp = catalogueItem.getInitialDiskNumber(), t_temp = fat[temp];
                while (temp != FATManager.END_FLAG) {  //归还空间有两步，一步是改FAT表，一步是清空磁盘
                    fat[temp] = 0;
                    diskManager.clearRealDisk(temp);
                    temp = t_temp;
                    if (t_temp != FATManager.END_FLAG) {
                        t_temp = fat[t_temp];
                    }
                }

                catalogueItemList.remove(catalogueItem);
                diskManager.saveCatalogueItemFormatInformationToDiskForDeleteCatalogueItem(diskNumber, catalogueItemList, fat);
                /* 删除对应的目录项 */
            }
        }
        fatManager.saveFATToDisk(fat);
//        return true;
        return "";
    }

    public String openFile(String absouletRoute, int flag, int[] fat, ArrayList<OFTLE> oftleList) {
        DirectoryOpreator directoryOpreator = new DirectoryOpreator();
        DiskManager diskManager = new DiskManager();
        FATManager fatManager = new FATManager();
        OFTLEManager oftleManager = new OFTLEManager();

        String fatherDirectory, fileNameType;
        fatherDirectory = absouletRoute.substring(0, absouletRoute.lastIndexOf('/'));
        fileNameType = absouletRoute.substring(absouletRoute.lastIndexOf('/') + 1);
        /* 父目录-子目录搜索法 */

        int diskNumber = directoryOpreator.searchDiskNumberOfStoringCatalogueInformation(fatherDirectory, fat);
        if (diskNumber == -1) {
//            System.out.println("该文件不存在，无法打开文件");  //是该文件的父目录不存在，所以说该文件不存在（这时传过来的文件名是个绝对路径文件名）
//            return false;
            return "该文件不存在，无法打开文件！";
        }

        //ArrayList<CatalogueItem> catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
        ArrayList<CatalogueItem> catalogueItemList = directoryOpreator.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, fat);

        boolean isFileExist = false;
        int fileNumber = 0;
        for (CatalogueItem catalogueItemList1 : catalogueItemList) {
            String tempS = "";
            if (catalogueItemList1.getAttribute()[3] == 1) {
                continue;
            } else {
                for (int k = 0; k < catalogueItemList1.getName().length(); k++) {
                    if (catalogueItemList1.getName().charAt(k) != '$') {
                        tempS += catalogueItemList1.getName().charAt(k);
                    }
                }
                tempS += catalogueItemList1.getType();
            }

            if (fileNameType.equals(tempS)) {
                isFileExist = true;
                fileNumber = catalogueItemList.indexOf(catalogueItemList1);
                break;
            }
        }

        if (!isFileExist) {
//            System.out.println("该文件不存在，无法打开文件");  //真正的该文件在本目录下面不存在
//            return false;
            return "该文件不存在，无法打开文件！";
        }

        System.out.println(catalogueItemList.get(fileNumber).getAttribute());

        if (flag == 1 && catalogueItemList.get(fileNumber).getAttribute()[0] == 1) {
//            System.out.println("打开失败，不可以用写的方式打开只读文件");
//            return false;
            return "打开失败，不可以用写的方式打开只读文件！";
        }

        CatalogueItem item = catalogueItemList.get(fileNumber);
        Pointer readPointer = new Pointer(item.getInitialDiskNumber(), 0);  //建立读指针

        int temp = item.getInitialDiskNumber();  //用来记录写指针的盘块号
        while (fat[temp] != FATManager.END_FLAG) {
            temp = fat[temp];
        }
        int lastAddressInDisk = (diskManager.getUsingSizeOfCurrentDisk(temp) == 0) ? 0 : diskManager.getUsingSizeOfCurrentDisk(temp) - 1;
        /* 写指针的块内地址，也就是文件末尾（最后一个存储磁盘的块内地址），上一次文件结束符的位置 */

        Pointer writePointer = new Pointer(temp, lastAddressInDisk);
        /* 建立打开文件表前的数据准备 */

        OFTLE oftle = new OFTLE(absouletRoute, item.getAttribute(),
                item.getInitialDiskNumber(), item.getLength(), flag, readPointer, writePointer);
        if (!oftleManager.isOFTLEExist(oftleList, oftle)) {  //打开表里面就添加进去
            oftleList.add(oftle);
        } else {
            return "文件已被打开!";
        }

        fatManager.saveFATToDisk(fat);
//        return true;
        return "";
    }

    public String readFile(String absouletRoute, int readLength, ArrayList<Character> ramSpace, int[] fat, ArrayList<OFTLE> oftleList) {
        OFTLEManager oftleManager = new OFTLEManager();
        DiskManager diskManager = new DiskManager();
        FATManager fatManager = new FATManager();

        int oftleNumber;
        if ((oftleNumber = oftleManager.returnOFTLEIndex(oftleList, absouletRoute)) != -1) {
            OFTLE tempOFTLET = oftleList.get(oftleNumber);
            if (tempOFTLET.getFlag() == 1) {
//                System.out.println("文件已用写方式打开，不能读此文件");
//                return false;  //交给另一个层面去处理
                return "文件已用写方式打开，不能读此文件！";
            } else {
                int temp_d_num = tempOFTLET.getRead().getD_num(), temp_b_num = tempOFTLET.getRead().getB_num();
                int readIndex = 0, index = temp_b_num;
                ArrayList<Character> fileInformationPerDisk = diskManager.getFileFormatInformationFromDisk(temp_d_num);

                if (fileInformationPerDisk.get(index) != '#') {  //只允许读一次文件结束符
                    while (readIndex < readLength) {  //读的长度结束了就必需停止
                        if (fileInformationPerDisk.get(index) == '#') {  //读到文件结束符，请结束
                            //ramSpace.add(fileInformationPerDisk.get(index));
                            temp_b_num = index;
                            readIndex++;
                            break;
                        } else {
                            ramSpace.add(fileInformationPerDisk.get(index));
                            readIndex++;
                            if (index + 1 == fileInformationPerDisk.size()) {
                                index = 0;
                                temp_b_num = index;
                                temp_d_num = fat[temp_d_num];
                                fileInformationPerDisk = diskManager.getFileFormatInformationFromDisk(temp_d_num);
                            } else {
                                index++;
                                temp_b_num = index;
                            }
                        }
                    }
                }

                tempOFTLET.getRead().setD_num(temp_d_num);
                tempOFTLET.getRead().setB_num(temp_b_num);
                /* 新读指针的位置相当关键 */

            }
        } else {  //其实只是多了打开文件这一项罢了
            if (!openFile(absouletRoute, 0, fat, oftleList).isEmpty()) {
//                System.out.println("打开文件失败，无法读取！");
//                return false;
                return "打开文件失败，无法读取！";
            } else {
                OFTLE tempOFTLET = oftleList.get(oftleList.size() - 1);
                /* 打开文件表的最后一项正是新打开的文件项 */

                int temp_d_num = tempOFTLET.getRead().getD_num(), temp_b_num = tempOFTLET.getRead().getB_num();
                int readIndex = 0, index = temp_b_num;
                ArrayList<Character> fileInformationPerDisk = diskManager.getFileFormatInformationFromDisk(temp_d_num);

                if (fileInformationPerDisk.get(index) != '#') {  //只允许读一次文件结束符
                    while (readIndex < readLength) {
                        if (fileInformationPerDisk.get(index) == '#') {
                            //ramSpace.add(fileInformationPerDisk.get(index));
                            temp_b_num = index;
                            readIndex++;
                            break;
                        } else {
                            ramSpace.add(fileInformationPerDisk.get(index));
                            readIndex++;
                            if (index + 1 == fileInformationPerDisk.size()) {
                                index = 0;
                                temp_b_num = index;
                                temp_d_num = fat[temp_d_num];
                                fileInformationPerDisk = diskManager.getFileFormatInformationFromDisk(temp_d_num);
                            } else {
                                index++;
                                temp_b_num = index;
                            }
                        }
                    }
                }

                tempOFTLET.getRead().setD_num(temp_d_num);
                tempOFTLET.getRead().setB_num(temp_b_num);
                /* 新读指针的位置相当关键 */
            }
        }

        fatManager.saveFATToDisk(fat);
//        return true;
        return "";
    }

    public String writeFile(String absouletRoute, char[] buffer, int writeLength, int[] fat, ArrayList<OFTLE> oftleList) {
        /* 抽象为直接从缓冲向文件内写数据，在关闭文件时追加文件结束符 */
        OFTLEManager oftleManager = new OFTLEManager();
        DiskManager diskManager = new DiskManager();
        FATManager fatManager = new FATManager();

        int oftleNumber;
        if ((oftleNumber = oftleManager.returnOFTLEIndex(oftleList, absouletRoute)) != -1) {
            System.out.println("yashi");
            OFTLE tempOFTLET = oftleList.get(oftleNumber);
            if (tempOFTLET.getFlag() == 0) {
//                System.out.println("文件已用读方式打开，不能写此文件");
//                return false;  //交给另一个层面去处理
                return "文件已用读方式打开，不能写此文件！";
            } else {  //这里只考虑从文件末尾添加的情况  
                int temp_d_num = tempOFTLET.getWrite().getD_num(), temp_b_num = tempOFTLET.getWrite().getB_num();
                int bufferIndex = 0, addDiskNumber = 0;
                ArrayList<Character> fileInformationPerDisk = diskManager.getFileFormatInformationFromDisk(temp_d_num);

                while (bufferIndex < writeLength) {
                    while (temp_b_num < fileInformationPerDisk.size() && bufferIndex < writeLength) {
                        fileInformationPerDisk.set(temp_b_num, buffer[bufferIndex]);
                        bufferIndex++;
                        temp_b_num++;
                    }
                    while (temp_b_num < DiskManager.MAX_DISK_SIZE && bufferIndex < writeLength) {
                        fileInformationPerDisk.add(buffer[bufferIndex]);
                        bufferIndex++;
                        temp_b_num++;
                    }
                    if (temp_b_num == DiskManager.MAX_DISK_SIZE) {  //本盘被写光了才申请新盘
                        if (fat[temp_d_num] != FATManager.END_FLAG) {  //如果写指针在文件中间，就顺理成章往后读
                            temp_d_num = fat[temp_d_num];
                        } else {
                            int t_temp_d_num;
                            if ((t_temp_d_num = fatManager.updateFATForMallocANewDisk(fat)) == -1) {
//                                System.out.println("磁盘已满，无法把内容完全写完！");

                                temp_b_num--;

                                tempOFTLET.getWrite().setD_num(temp_d_num);
                                tempOFTLET.getWrite().setB_num(temp_b_num);
                                /* 这个写指针位一定要准确记录，因为它将用于追加文件结束符 */
                                diskManager.saveFileFormatInformationToDisk(temp_d_num, fileInformationPerDisk);
                                /* 写文件操作最后三部曲 */

//                                return true;
                                return "";
                            } else {  //不然就申请新的空间
                                fat[temp_d_num] = t_temp_d_num;  //及时更新FAT表
                                diskManager.saveFileFormatInformationToDisk(temp_d_num, fileInformationPerDisk);  //及时将某个盘块存进磁盘
                                temp_d_num = fat[temp_d_num];
                                addDiskNumber++;
                            }
                        }

                        fileInformationPerDisk = diskManager.getFileFormatInformationFromDisk(temp_d_num);
                        temp_b_num = 0;
                    }
                }

                tempOFTLET.getWrite().setD_num(temp_d_num);
                tempOFTLET.getWrite().setB_num(temp_b_num);
                /* 这个写指针位一定要准确记录，因为它将用于追加文件结束符 */
                diskManager.saveFileFormatInformationToDisk(temp_d_num, fileInformationPerDisk);
                /* 写文件操作最后三部曲 */

//                System.out.println("addDiskNumber = " + addDiskNumber);
                tempOFTLET.setFileLength(tempOFTLET.getFileLength() + addDiskNumber);
//                System.out.println("OFTLETFileLength = " + tempOFTLET.getFileLength());
                /* 记得修改增加的长度 */
            }
        } else {
            String temp = openFile(absouletRoute, 1, fat, oftleList);
            if (!temp.isEmpty()) {
//                System.out.println("打开文件失败，无法写入！");
//                return false;
                switch (temp) {
                    case "该文件不存在，无法打开文件！":
                        return "文件不存在，无法写入！";
                    case "打开失败，不可以用写的方式打开只读文件！":
                        return "该文件为只读文件，无法写入！";
                }
                return "打开文件失败，无法写入！";
            } else {
                OFTLE tempOFTLET = oftleList.get(oftleList.size() - 1);
                int temp_d_num = tempOFTLET.getWrite().getD_num(), temp_b_num = tempOFTLET.getWrite().getB_num();
                int bufferIndex = 0, addDiskNumber = 0;
                ArrayList<Character> fileInformationPerDisk = diskManager.getFileFormatInformationFromDisk(temp_d_num);
//                System.out.println("temp_b_num = " + temp_b_num + " fileInformationPerDisk.size = " + fileInformationPerDisk.size());

                while (bufferIndex < writeLength) {
                    while (temp_b_num < fileInformationPerDisk.size() && bufferIndex < writeLength) {
//                        System.out.println("temp_b_num = " + temp_b_num + " bufferIndex = " + bufferIndex);
                        fileInformationPerDisk.set(temp_b_num, buffer[bufferIndex]);
                        bufferIndex++;
                        temp_b_num++;
                    }
                    while (temp_b_num < DiskManager.MAX_DISK_SIZE && bufferIndex < writeLength) {
                        fileInformationPerDisk.add(buffer[bufferIndex]);
                        bufferIndex++;
                        temp_b_num++;
//                        System.out.println("temp_b_num = " + temp_b_num);
                    }
                    if (temp_b_num == DiskManager.MAX_DISK_SIZE) {
                        if (fat[temp_d_num] != FATManager.END_FLAG) {  //如果写指针在文件中间，就顺理成章往后读
                            temp_d_num = fat[temp_d_num];
                        } else {
                            int t_temp_d_num;
                            if ((t_temp_d_num = fatManager.updateFATForMallocANewDisk(fat)) == -1) {
//                                System.out.println("磁盘已满，无法把内容完全写完！");

                                temp_b_num--;

                                tempOFTLET.getWrite().setD_num(temp_d_num);
                                tempOFTLET.getWrite().setB_num(temp_b_num);
                                /* 这个写指针位一定要准确记录，因为它将用于追加文件结束符 */
                                diskManager.saveFileFormatInformationToDisk(temp_d_num, fileInformationPerDisk);
                                /* 写文件操作最后三部曲 */

//                                return true;
                                return "";
                            } else {  //不然就申请新的空间
                                fat[temp_d_num] = t_temp_d_num;  //及时更新FAT表
                                diskManager.saveFileFormatInformationToDisk(temp_d_num, fileInformationPerDisk);  //及时将某个盘块存进磁盘
                                temp_d_num = fat[temp_d_num];
                                addDiskNumber++;
                            }
                        }

                        fileInformationPerDisk = diskManager.getFileFormatInformationFromDisk(temp_d_num);
                        temp_b_num = 0;
                    }
                }

                tempOFTLET.getWrite().setD_num(temp_d_num);
                tempOFTLET.getWrite().setB_num(temp_b_num);
                /* 这个写指针位一定要准确记录，因为它将用于追加文件结束符 */
                diskManager.saveFileFormatInformationToDisk(temp_d_num, fileInformationPerDisk);
                /* 写文件操作最后三部曲 */

//                System.out.println("addDiskNumber = " + addDiskNumber);
                tempOFTLET.setFileLength(tempOFTLET.getFileLength() + addDiskNumber);
//                System.out.println("OFTLETFileLength = " + tempOFTLET.getFileLength());
                /* 记得修改增加的长度 */
            }
        }

        fatManager.saveFATToDisk(fat);
//        return true;
        return "";
    }

    public String closeFile(String absouletRoute, int[] fat, ArrayList<OFTLE> oftleList) {
        OFTLEManager oftleManager = new OFTLEManager();
        DiskManager diskManager = new DiskManager();
        DirectoryOpreator directoryOpreator = new DirectoryOpreator();

        int oftleNumber;
        if ((oftleNumber = oftleManager.returnOFTLEIndex(oftleList, absouletRoute)) == -1) {
//            System.out.println("该文件没被打开，不用关闭！");
//            return false;
            return "该文件没被打开，不用关闭！";
        } else {
            OFTLE tempOFTLET = oftleList.get(oftleNumber);

            if (tempOFTLET.getFlag() == 1) {
                int diskNumber = directoryOpreator.searchDiskNumberOfStoringCatalogueInformation(absouletRoute.substring(0, absouletRoute.lastIndexOf('/')), fat);
                /* 文件的目录登记项在父目录，所以万分小心 */

                //ArrayList<CatalogueItem> catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
                ArrayList<CatalogueItem> catalogueItemList = directoryOpreator.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, fat);

                for (CatalogueItem catalogueItemList1 : catalogueItemList) {
                    String t_fileCompleteName = "";
                    for (int i = 0; i < catalogueItemList1.getName().length(); i++) {
                        if (catalogueItemList1.getName().charAt(i) != '$') {
                            t_fileCompleteName += catalogueItemList1.getName().charAt(i);
                        }
                    }
                    t_fileCompleteName += catalogueItemList1.getType();
                    if (t_fileCompleteName.equals(tempOFTLET.getAbsoultRoute().substring(absouletRoute.lastIndexOf('/') + 1))) {
//                    System.out.println("OFTLETFileLength = " + tempOFTLET.getFileLength());
                        catalogueItemList1.setLength(tempOFTLET.getFileLength());
                        /* 一般只会变动长度 */

                        ArrayList<Character> fileInformationPerDisk = diskManager.getFileFormatInformationFromDisk(tempOFTLET.getWrite().getD_num());
                        if (fileInformationPerDisk.size() == DiskManager.MAX_DISK_SIZE) {
                            fileInformationPerDisk.set(fileInformationPerDisk.size() - 1, '#');
                        } else {
                            fileInformationPerDisk.add('#');
                        }
                        diskManager.saveFileFormatInformationToDisk(tempOFTLET.getWrite().getD_num(), fileInformationPerDisk);
                        /* 追加文件结束符 */

                        oftleManager.deleteOFTLE(oftleList, tempOFTLET);
                        break;
                    }
                }
                diskManager.saveCatalogueItemFormatInformationToDisk(diskNumber, catalogueItemList, fat);
                /* 修改后的目录项信息记得保存呀 */
            } else {
                oftleManager.deleteOFTLE(oftleList, tempOFTLET);  //如果是以读方式打开就不用修改任何东西
            }
        }
//        return true;
        return "";
    }

    public boolean printFileInformation(String absouletRoute, int[] fat, ArrayList<OFTLE> oftleList) {
        OFTLEManager oftleManager = new OFTLEManager();
        DiskManager diskManager = new DiskManager();
        DirectoryOpreator directoryOpreator = new DirectoryOpreator();

        String fatherDirectory, fileNameType;
        fatherDirectory = absouletRoute.substring(0, absouletRoute.lastIndexOf('/'));
        fileNameType = absouletRoute.substring(absouletRoute.lastIndexOf('/') + 1);
        /* 父目录-子目录搜索法 */

        int diskNumber;
        if ((diskNumber = directoryOpreator.searchDiskNumberOfStoringCatalogueInformation(fatherDirectory, fat)) == -1) {
            System.out.println("该文件不存在！");  //父目录不存在
            return false;
        } else {
            //ArrayList<CatalogueItem> catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
            ArrayList<CatalogueItem> catalogueItemList = directoryOpreator.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, fat);
            boolean isFileExist = false;

            CatalogueItem catalogueItem = null;
            for (CatalogueItem catalogueItemList1 : catalogueItemList) {
                String t_fileCompleteName = "";
                for (int i = 0; i < catalogueItemList1.getName().length(); i++) {
                    if (catalogueItemList1.getName().charAt(i) != '$') {
                        t_fileCompleteName += catalogueItemList1.getName().charAt(i);
                    }
                }
                t_fileCompleteName += catalogueItemList1.getType();
                if (t_fileCompleteName.equals(fileNameType)) {
                    catalogueItem = catalogueItemList1;
                    isFileExist = true;
                    break;
                }
            }
            if (!isFileExist) {
                System.out.println("该文件不存在！");  //目录下该文件不存在
                return false;
            }

            if (oftleManager.returnOFTLEIndex(oftleList, absouletRoute) != -1) {
                System.out.println("文件已被打开，无法查看！");
                return false;
            } else {
//                OFTLE tempOFTLET = oftleList.get(oftleNumber);
                int temp = catalogueItem.getInitialDiskNumber();
                ArrayList<Character> fileInformationPerDisk;

                do {
                    fileInformationPerDisk = diskManager.getFileFormatInformationFromDisk(temp);
                    for (Character fileInformationPerDisk1 : fileInformationPerDisk) {
                        if (fileInformationPerDisk1 != '#') {
                            System.out.print(fileInformationPerDisk1);
                        }
                    }
                    System.out.println("");
                    System.out.println("******************************************************");
                    temp = fat[temp];
                } while (temp != FATManager.END_FLAG);
            }
        }
        return true;
    }

    public String change(String absouletRoute, int mode, int[] fat, ArrayList<OFTLE> oftleList) {
        OFTLEManager oftleManager = new OFTLEManager();
        DiskManager diskManager = new DiskManager();
        DirectoryOpreator directoryOpreator = new DirectoryOpreator();

        String fatherDirectory, fileNameType;
        fatherDirectory = absouletRoute.substring(0, absouletRoute.lastIndexOf('/'));
        fileNameType = absouletRoute.substring(absouletRoute.lastIndexOf('/') + 1);
        /* 父目录-子目录搜索法 */

        int diskNumber;
        if ((diskNumber = directoryOpreator.searchDiskNumberOfStoringCatalogueInformation(fatherDirectory, fat)) == -1) {
//            System.out.println("该文件不存在，无法修改其属性！");  //父目录不存在
//            return false;
            return "该文件不存在，无法修改其属性！";
        } else {
            //ArrayList<CatalogueItem> catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
            ArrayList<CatalogueItem> catalogueItemList = directoryOpreator.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, fat);
            boolean isFileExist = false;

            CatalogueItem catalogueItem = null;
            for (CatalogueItem catalogueItemList1 : catalogueItemList) {
                String t_fileCompleteName = "";
                for (int i = 0; i < catalogueItemList1.getName().length(); i++) {
                    if (catalogueItemList1.getName().charAt(i) != '$') {
                        t_fileCompleteName += catalogueItemList1.getName().charAt(i);
                    }
                }
                t_fileCompleteName += catalogueItemList1.getType();
                if (t_fileCompleteName.equals(fileNameType)) {
                    catalogueItem = catalogueItemList1;
                    isFileExist = true;
                    break;
                }
            }
            if (!isFileExist) {
//                System.out.println("该文件不存在，无法修改其属性！");  //目录下该文件不存在
//                return false;
                return "该文件不存在，无法修改其属性！";
            }

            if (oftleManager.returnOFTLEIndex(oftleList, absouletRoute) != -1) {
//                System.out.println("文件已被打开，无法修改其属性！");
//                return false;
                return "文件已被打开，无法修改其属性！";
            } else {
                if (mode == 0) {
                    int[] newAttribute = {0, 0, 1, 0, 0, 0, 0, 0};
                    catalogueItem.setAttribute(newAttribute);
                    diskManager.saveCatalogueItemFormatInformationToDisk(diskNumber, catalogueItemList, fat);
                } else if (mode == 1) {
                    int[] newAttribute = {1, 0, 0, 0, 0, 0, 0, 0};
                    catalogueItem.setAttribute(newAttribute);
                    diskManager.saveCatalogueItemFormatInformationToDisk(diskNumber, catalogueItemList, fat);
                }
            }
        }
//        return true;
        return "";
    }
}

package com.os.operate;

import com.os.disk.DiskManager;
import java.util.ArrayList;
import java.util.Arrays;
import com.os.ram.FATManager;
import com.os.ram.OFTLE;
import com.os.ram.OFTLEManager;

/**
 *
 * @author FeiFan Liang
 */
public class DirectoryOpreator {

    public ArrayList<CatalogueItem> getCompleteCatalogueItemFormatInformationFromDisk(int diskNumber, int[] fat) {
        DiskManager diskManager = new DiskManager();

        int temp = diskNumber;
        ArrayList<CatalogueItem> catalogueItemList = new ArrayList<>();
        ArrayList<CatalogueItem> tempCatalogueItemList;

        do {
            tempCatalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(temp);
            for (CatalogueItem tempCatalogueItemList1 : tempCatalogueItemList) {
                catalogueItemList.add(tempCatalogueItemList1);
            }
            temp = fat[temp];
        } while (temp != FATManager.END_FLAG);

        return catalogueItemList;
    }

    public int searchDiskNumberOfStoringCatalogueInformation(String absouletRoute, int[] fat) {
        // 路径来源暂定 //
        
        if (absouletRoute.isEmpty()) {  //元目录的盘块号
            return DiskManager.ORIGINAL_DISK_NUMBER;  //暂定增加功能//
        }
        /* 返回2号磁盘的格式化内容 */

        String[] s = absouletRoute.split("/");  //将路径名分隔
        ArrayList<CatalogueItem> catalogueItemList = null;
//        for (int i = 0; i < s.length; i++) {
//            System.out.println(s[i]);
//        }

        int diskNumber = -1;  //根目录都不匹配就返回-1喽
        switch (s[0].toUpperCase()) {
            case "C:":
                //catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(DiskManager.C_DISK_NUMBER);
                catalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(DiskManager.C_DISK_NUMBER, fat);
                diskNumber = DiskManager.C_DISK_NUMBER;
                break;
            case "D:":
                //catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(DiskManager.D_DISK_NUMBER);
                catalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(DiskManager.D_DISK_NUMBER, fat);
                diskNumber = DiskManager.D_DISK_NUMBER;
                break;
            case "E:":
                //catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(DiskManager.E_DISK_NUMBER);
                catalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(DiskManager.E_DISK_NUMBER, fat);
                diskNumber = DiskManager.E_DISK_NUMBER;
                break;
            case "F:":
                //catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(DiskManager.F_DISK_NUMBER);
                catalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(DiskManager.F_DISK_NUMBER, fat);
                diskNumber = DiskManager.F_DISK_NUMBER;
                break;
            case "G:":
                //catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(DiskManager.G_DISK_NUMBER);
                catalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(DiskManager.G_DISK_NUMBER, fat);
                diskNumber = DiskManager.G_DISK_NUMBER;
                break;
        }
        /* 以目录项格式读取某个根目录的内容（C盘、D盘、E盘、F盘、G盘，3号、4号、5号、6号磁盘的内容）*/

        for (int i = 1; i < s.length; i++) {
            boolean isNeedNextDiskInformation = false;

            /* 这些目录项都是在父目录上面找出来的目录项 */
            for (CatalogueItem catalogueItemList1 : catalogueItemList) {
                int nextDiskNumber = catalogueItemList1.getInitialDiskNumber();
//                System.out.println("实际名字");
//                for (int k = 0; k < catalogueItemList1.getName().length(); k++) {
//                    if (catalogueItemList1.getName().charAt(k) != '$') {  //‘$’视为空
//                        System.out.print(catalogueItemList1.getName().charAt(k));
//                    }
//                }
//                System.out.println("");
                String tempS = "";
                if (catalogueItemList1.getAttribute()[3] == 1) {
                    for (int k = 0; k < catalogueItemList1.getName().length(); k++) {
                        if (catalogueItemList1.getName().charAt(k) != '$') {  //必需特别地去处理这个‘$’符号
                            tempS += catalogueItemList1.getName().charAt(k);
                        }
                    }
                } else {
                    for (int k = 0; k < catalogueItemList1.getName().length(); k++) {
                        if (catalogueItemList1.getName().charAt(k) != '$') {
                            tempS += catalogueItemList1.getName().charAt(k);
                        }
                    }
                    tempS += catalogueItemList1.getType();  //文件的类型格式已经带上点'.'（ASCII码为46）
                }

//                System.out.println("tempS = "+tempS);
                if (s[i].equals(tempS) && i == s.length - 1) {  //典型的查找成功，用户输入的路径名到了终点，并且用户输入的路径名上对应的最后一个名字是目录项上存储的真实名字
                    return nextDiskNumber;
                    /* 真实路径:C:/xx/xx/x.txt   用户输入路径:C:/xx/xx/x.txt */
                } else if (s[i].equals(tempS) && i != s.length - 1) {   //用户路径还没结束的所有情况
                    if (catalogueItemList1.getAttribute()[3] == 1) {  //说明当前登记项为目录登记项
                        //ArrayList<CatalogueItem> tempCatalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(nextDiskNumber);
                        ArrayList<CatalogueItem> tempCatalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(nextDiskNumber, fat);
                        /* 
                         预先将目录中的内容读出来，看看该目录是否为空 
                         这一着保证了扫描实际路径，也就是实际目录的时候一定不为空（除了根目录为空以外）
                         */
                        //可能不需要预读//

                        if (tempCatalogueItemList.isEmpty()) { //该目录为空，也就意味着实际路径已结束，但用户路径还没结束
                            return -1;
                            /* 真实路径:C:/xx/xx   用户输入路径:C:/xx/xx/x.txt */
                        }
                    } else {  //如果该登记项为文件登记项，则说明实际路径一定结束了
                        return -1;
                        /* 真实路径:C:/xx/xx.txt   用户输入路径:C:/xx/xx.txt/x.txt */
                    }

                    //catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(nextDiskNumber);
                    catalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(nextDiskNumber, fat);
                    isNeedNextDiskInformation = true;
                    break;
                    /* 上述两种情况都不是，说明实际路径和用户路径均未结束，往后走吧 */
                }
                /* 名字不对应,那就选择目录项表里面的下一个目录项进行判断 */
            }

            if (!isNeedNextDiskInformation) {
                return -1;
                /* 
                 这里有两种情况
                 一种是该层目录的所有目录项名字都和用户输入路径中的名字不对应，宣告搜索失败
                 另一种是（该目录为空目录，无法继续进行下去了）：  真实路径:C:   用户输入路径:C:/xx/xx.txt/x.txt 
                 */
            }
        }

        /* 
         剩下的情况就是特殊情况了,这种情况就是用户输入路径为某一个根路径，直接返回根路径盘号即可
         真实路径:C:   用户输入路径:C:   或者  真实路径:C:/xx/x.c   用户输入路径:C:
         或者  用户输入路径为cjlghlgh这种非法路径
         */
        return diskNumber;
    }

    public String makeDirectory(String absouletRoute, int[] fat) {
        FATManager fatManager = new FATManager();
        DiskManager diskManager = new DiskManager();

        if (absouletRoute.lastIndexOf('/') == -1) {   //稍有改动，因为根目录变成了C:  D:  E:  等等,已把元目录包含在内
//            System.out.println("你没资格创建根目录！");
//            return false;
            return "你没资格创建根目录！";
        }

        String fatherDirectory, subDirectory;
//        if (absouletRoute.lastIndexOf('/') == 0) {
//            fatherDirectory = "/";
//        } else {
        fatherDirectory = absouletRoute.substring(0, absouletRoute.lastIndexOf('/'));
//        }
        subDirectory = absouletRoute.substring(absouletRoute.lastIndexOf("/") + 1);

        int diskNumber;  //记录父目录的存储磁盘号
        if ((diskNumber = this.searchDiskNumberOfStoringCatalogueInformation(fatherDirectory, fat)) == -1) {
//            System.out.println("父目录不存在，无法创建当前目录！");  //父目录不存在
//            return false;
            return "父目录不存在，无法创建当前目录！";
        } else {
            //ArrayList<CatalogueItem> catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
            ArrayList<CatalogueItem> catalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, fat);

//            if (catalogueItemList.size() == 8) {
//                System.out.println("已有八个目录项，无法继续创建！");
//                return false;
//            } else {
            for (CatalogueItem catalogueItemList1 : catalogueItemList) {
                String tempS = "";
                if (catalogueItemList1.getAttribute()[3] == 1) {
                    for (int k = 0; k < catalogueItemList1.getName().length(); k++) {
                        if (catalogueItemList1.getName().charAt(k) != '$') {
                            tempS += catalogueItemList1.getName().charAt(k);
                        }
                    }
                    System.out.println(subDirectory+" "+tempS);
                    if (subDirectory.equals(tempS)) {
//                        System.out.println("目录名重复，无法在此新建目录！");
//                        return false;
                        return "目录名重复，无法在此新建目录！";
                    }
                }
            }

            int d_n;
            if ((d_n = fatManager.updateFATForMallocANewDisk(fat)) == -1) {   //为新目录申请磁盘已经隐含在其中
//                System.out.println("磁盘已满，没法新建目录！");
//                return false;
                return "磁盘已满，没法新建目录！";
            }

            int[] t_attribute = {0, 0, 0, 1, 0, 0, 0, 0};
            while (subDirectory.length() < 3) {
                subDirectory += '$';  //用被是为空字符的特殊字符补够长度
            }
            CatalogueItem catalogueItem = new CatalogueItem(subDirectory, " ", t_attribute, d_n, 0);  //反正类型对于目录的登记项没什么用，就用' '呗（什么时候心血来潮也可以改回'.'呗）

            catalogueItemList.add(catalogueItem);
            if (!diskManager.saveCatalogueItemFormatInformationToDiskForAddCatalogueItem(diskNumber, catalogueItemList, fat)) {  //如果成功，不必进行任何操作，因为目录项写回磁盘的操作均已经隐含在save里面了
//                System.out.println("磁盘已满，没法新建目录！");
//                fat[d_n] = 0;  //如果失败，记得把给新目录分配的磁盘也重新归零
//                return false;
//            }
                return "磁盘已满，没法新建目录！";
            }

            fatManager.saveFATToDisk(fat);
//            return true;
            return "";
        }
    }

    public boolean printDirectory(String absouletRoute, int[] fat) {
        int diskNumber;
        if ((diskNumber = this.searchDiskNumberOfStoringCatalogueInformation(absouletRoute, fat)) == -1) {
            System.out.println("无法显示，该目录不存在！");
            return false;
        } else {
            //ArrayList<CatalogueItem> catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
            ArrayList<CatalogueItem> catalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, fat);

            System.out.println("该目录为：" + absouletRoute);
            if (catalogueItemList.isEmpty()) {
                System.out.println("该目录是个空目录！");
            } else {
                System.out.println("**************************************************************************");
                for (CatalogueItem catalogueItemList1 : catalogueItemList) {
                    if (catalogueItemList1.getAttribute()[3] == 1) {
                        System.out.print("目录名：");
                        for (int i = 0; i < catalogueItemList1.getName().length(); i++) {
                            if (catalogueItemList1.getName().charAt(i) != '$') {  //‘$’视为空
                                System.out.print(catalogueItemList1.getName().charAt(i));
                            }
                        }
                        System.out.println("");
                        System.out.println("目录属性：" + Arrays.toString(catalogueItemList1.getAttribute()));
                        System.out.println("目录存储盘块：" + catalogueItemList1.getInitialDiskNumber());
                        System.out.println("----------------------------------------------------");
                    } else {
                        System.out.print("文件名：");
                        for (int i = 0; i < catalogueItemList1.getName().length(); i++) {
                            if (catalogueItemList1.getName().charAt(i) != '$') {  //‘$’视为空
                                System.out.print(catalogueItemList1.getName().charAt(i));
                            }
                        }
                        System.out.println(" 文件类型：" + catalogueItemList1.getType());
                        System.out.println("文件属性：" + Arrays.toString(catalogueItemList1.getAttribute()));
                        System.out.println("文件起始盘块：" + catalogueItemList1.getInitialDiskNumber());
                        System.out.println("文件长度（盘块数）：" + catalogueItemList1.getLength());
                        System.out.println("----------------------------------------------------");
                    }
                }
            }
        }
        return true;
    }

    public boolean isRemoveTheDirectory(String absouletRoute, CatalogueItem catalogueItemForCurrentRoute, int[] fat, ArrayList<OFTLE> oftleList) {
        OFTLEManager oftleManager = new OFTLEManager();

        int diskNumber = this.searchDiskNumberOfStoringCatalogueInformation(absouletRoute, fat);
        ArrayList<CatalogueItem> catalogueItemList;
        if (catalogueItemForCurrentRoute.getAttribute()[3] == 0) {
            if (oftleManager.returnOFTLEIndex(oftleList, absouletRoute) != -1) {
                return false;
            } else {
                return true;
            }
        } else {
            //catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
            catalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, fat);
        }

        if (catalogueItemList.isEmpty()) {
            return true;
        } else {
            for (CatalogueItem catalogueItemList1 : catalogueItemList) {
                String tempS = "";
                if (catalogueItemList1.getAttribute()[3] == 1) {
                    for (int k = 0; k < catalogueItemList1.getName().length(); k++) {
                        if (catalogueItemList1.getName().charAt(k) != '$') {  //必需特别地去处理这个‘$’符号
                            tempS += catalogueItemList1.getName().charAt(k);
                        }
                    }
                } else {
                    for (int k = 0; k < catalogueItemList1.getName().length(); k++) {
                        if (catalogueItemList1.getName().charAt(k) != '$') {
                            tempS += catalogueItemList1.getName().charAt(k);
                        }
                    }
                    tempS += catalogueItemList1.getType();  //文件的类型格式已经带上点'.'（ASCII码为46）
                }

                if (!isRemoveTheDirectory(absouletRoute + "/" + tempS, catalogueItemList1, fat, oftleList)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void deleteDirectory(String absouletRoute, CatalogueItem catalogueItemForCurrentRoute, int[] fat) {
        DiskManager diskManager = new DiskManager();
        FATManager fatManager = new FATManager();

        int diskNumber = this.searchDiskNumberOfStoringCatalogueInformation(absouletRoute, fat);
        /* 可能是目录存储盘块号或者文件起始盘块号 */
        ArrayList<CatalogueItem> catalogueItemList;
        if (catalogueItemForCurrentRoute.getAttribute()[3] == 0) {
            int temp = diskNumber, t_temp = fat[temp];
            while (temp != FATManager.END_FLAG) {  //归还空间有两步，一步是改FAT表，一步是清空磁盘
                fat[temp] = 0;
                diskManager.clearRealDisk(temp);
                temp = t_temp;
                if (t_temp != FATManager.END_FLAG) {
                    t_temp = fat[t_temp];
                }
            }
            fatManager.saveFATToDisk(fat);
        } else {
            //catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
            catalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, fat);

            if (!catalogueItemList.isEmpty()) {  //非空目录才递归进行处理
                for (CatalogueItem catalogueItemList1 : catalogueItemList) {
                    String tempS = "";
                    if (catalogueItemList1.getAttribute()[3] == 1) {
                        for (int k = 0; k < catalogueItemList1.getName().length(); k++) {
                            if (catalogueItemList1.getName().charAt(k) != '$') {  //必需特别地去处理这个‘$’符号
                                tempS += catalogueItemList1.getName().charAt(k);
                            }
                        }
                    } else {
                        for (int k = 0; k < catalogueItemList1.getName().length(); k++) {
                            if (catalogueItemList1.getName().charAt(k) != '$') {
                                tempS += catalogueItemList1.getName().charAt(k);
                            }
                        }
                        tempS += catalogueItemList1.getType();  //文件的类型格式已经带上点'.'（ASCII码为46）
                    }

                    deleteDirectory(absouletRoute + "/" + tempS, catalogueItemList1, fat);
                }

                catalogueItemList.clear();
                diskManager.saveCatalogueItemFormatInformationToDiskForDeleteCatalogueItem(diskNumber, catalogueItemList, fat);  //fat表的修改已经包含在里面

                fatManager.saveFATToDisk(fat);
                /* 一次性把所有目录项清掉 */

            } else {  //空目录也有分配一个磁盘，哈哈
                fat[diskNumber] = 0;  //记得修改fat表
                fatManager.saveFATToDisk(fat);
            }
        }
    }

    public String removeDirectory(String absouletRoute, int[] fat, ArrayList<OFTLE> oftleList) {
        DiskManager diskManager = new DiskManager();
        FATManager fatManager = new FATManager();

        if (absouletRoute.lastIndexOf('/') == -1) {  //稍有改动，因为根目录变成了C:  D:  E:  等等,已把元目录包含在内
//            System.out.println("根目录无法被删除！");
//            return false;
            return "根目录无法被删除！";
        }

        String fatherDirectory, subDirectory;
//        if (absouletRoute.lastIndexOf('/') == 0) {
//            fatherDirectory = "/";
//        } else {
        fatherDirectory = absouletRoute.substring(0, absouletRoute.lastIndexOf('/'));
//        }
        subDirectory = absouletRoute.substring(absouletRoute.lastIndexOf("/") + 1);
        /* 始终要从父目录处删掉它的目录项的，所以干脆坚持父目录-子目录搜索法 */

        int diskNumber;  //记录父目录的存储磁盘号
        if ((diskNumber = this.searchDiskNumberOfStoringCatalogueInformation(fatherDirectory, fat)) == -1) {
//            System.out.println("该目录不存在，无法删除！");  //父目录不存在
//            return false;
            return "该目录不存在，无法删除！";
        } else {
            //ArrayList<CatalogueItem> catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
            ArrayList<CatalogueItem> catalogueItemList = this.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, fat);

            CatalogueItem catalogueItem = null;
            boolean isDirectoryExists = false;
            for (CatalogueItem catalogueItemList1 : catalogueItemList) {
                String tempS = "";
                if (catalogueItemList1.getAttribute()[3] == 1) {
                    for (int k = 0; k < catalogueItemList1.getName().length(); k++) {
                        if (catalogueItemList1.getName().charAt(k) != '$') {
                            tempS += catalogueItemList1.getName().charAt(k);
                        }
                    }
                    if (subDirectory.equals(tempS)) {
                        catalogueItem = catalogueItemList1;
                        isDirectoryExists = true;
                        break;
                    }
                }
            }
            if (!isDirectoryExists) {
//                System.out.println("该目录不存在，无法删除！");  //真正的对应目录不存在
//                return false;
                return "该目录不存在，无法删除！";
            }

            if (!isRemoveTheDirectory(absouletRoute, catalogueItem, fat, oftleList)) {
//                System.out.println("该目录里面有打开的文件，不能删除！");
//                return false;
                return "该目录里面有打开的文件，不能删除！";
            } else {
                deleteDirectory(absouletRoute, catalogueItem, fat);  //把目录内容删除
                fat[catalogueItem.getInitialDiskNumber()] = 0;  //这一步相当重要，目录起始盘块号也要清零
                catalogueItemList.remove(catalogueItem);
                diskManager.saveCatalogueItemFormatInformationToDiskForDeleteCatalogueItem(diskNumber, catalogueItemList, fat);  //删除目录登记项
            }
        }
        fatManager.saveFATToDisk(fat);
//        return true;
        return "";
    }
}

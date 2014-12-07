package com.os.controller;

import com.os.disk.DiskManager;
import com.os.entity.FatItem;
import com.os.entity.FileTreeItem;
import com.os.entity.MemoryItem;
import com.os.main.Main;
import com.os.operate.CatalogueItem;
import com.os.operate.DirectoryOpreator;
import com.os.operate.FileOperator;
import com.os.operate.Filter;
import com.os.ram.FATManager;
import com.os.ram.OFTLE;
import com.os.ram.OFTLEManager;
import com.os.ram.RAMManager;
import com.os.utils.MsgQueue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.event.EventType;

/**
 * Created by Jeremie on 2014/10/13.
 */
public class MainController implements Initializable {

    @FXML
    private Button initDiskInfoButton;
    @FXML
    private Button loadFatTableButton;
    @FXML
    private TableView fatTable, memoryTable;
    @FXML
    private TreeView<FileTreeItem> pathTree;
    @FXML
    private TextArea openedFile, currentFileContent, writeFileContent;
    @FXML
    private TableColumn diskNumber1, diskNumber2, memoryColumn;
    @FXML
    private Pane main;
    @FXML
    private AnchorPane input, msg;

    private static MainController mainController;

    public static MainController getInstance() {
        return mainController;
    }

    public void changeMain(boolean flag) {
        main.setDisable(flag);
    }

    public void changeInput(boolean flag) {
        input.setVisible(flag);
    }

    public void changeMsg(boolean flag) {
        msg.setVisible(flag);
    }

    private ObservableList<FatItem> FatItemObservableList;
    private ArrayList<FatItem> FatItemArrayList = new ArrayList<>();

    /* 初始化主存，建立操作器 */
    private RAMManager ramManager = new RAMManager();
    private DirectoryOpreator directoryOpreator = new DirectoryOpreator();
    private FileOperator fileOperator = new FileOperator();
    private DiskManager diskManager = new DiskManager();
    private FATManager fatManager = new FATManager();
    private OFTLEManager oftleManager = new OFTLEManager();
    private Filter filter = new Filter();
    private String something;
    private int[] attribute = {0, 0, 1, 0, 0, 0, 0, 0};
    private int[] t_attribute = {0, 0, 0, 1, 0, 0, 0, 0};
    private TreeItem<FileTreeItem> itemTreeItem = new TreeItem<>(new FileTreeItem(null, "我的电脑", true));

    private ObservableList<MemoryItem> memoryItemObservableList;
    private ArrayList<MemoryItem> memoryItems = new ArrayList<>();

    private MsgQueue<String> queue = new MsgQueue<>();

    /**
     * 初始化磁盘信息
     */
    @FXML
    private void initDiskInfo() {
        MessageController messageController = MessageController.getInstance();
        try {
            for (int i = 0; i < 128; i++) {
                File file = new File(i + ".txt");
                PrintWriter output = new PrintWriter(file);
                output.close();
            }
            /*
             int[] testFat = new int[128];
             testFat[0] = 1;
             testFat[1] = FATManager.END_FLAG;
             testFat[2] = FATManager.END_FLAG;
             for (int i = 3; i < testFat.length; i++) {
             testFat[i] = 0;
             }
             fatManager.saveFATToDisk(testFat);
             */
            int[] testFat = new int[128];
            testFat[0] = 1;
            testFat[1] = FATManager.END_FLAG;
            testFat[2] = FATManager.END_FLAG;
            testFat[3] = FATManager.END_FLAG;
            testFat[4] = FATManager.END_FLAG;
            testFat[5] = FATManager.END_FLAG;
            testFat[6] = FATManager.END_FLAG;
            testFat[7] = FATManager.END_FLAG;
            for (int i = 8; i < testFat.length; i++) {
                testFat[i] = 0;
            }
            fatManager.saveFATToDisk(testFat);

            CatalogueItem catalogueItem1 = new CatalogueItem("C:$", " ", t_attribute, DiskManager.C_DISK_NUMBER, 0);
            CatalogueItem catalogueItem2 = new CatalogueItem("D:$", " ", t_attribute, DiskManager.D_DISK_NUMBER, 0);
            CatalogueItem catalogueItem3 = new CatalogueItem("E:$", " ", t_attribute, DiskManager.E_DISK_NUMBER, 0);
            CatalogueItem catalogueItem4 = new CatalogueItem("F:$", " ", t_attribute, DiskManager.F_DISK_NUMBER, 0);
            CatalogueItem catalogueItem5 = new CatalogueItem("G:$", " ", t_attribute, DiskManager.G_DISK_NUMBER, 0);

            ArrayList<CatalogueItem> catalogueItemList = new ArrayList<>();
            catalogueItemList.add(catalogueItem1);
            catalogueItemList.add(catalogueItem2);
            catalogueItemList.add(catalogueItem3);
            catalogueItemList.add(catalogueItem4);
            catalogueItemList.add(catalogueItem5);
            this.diskManager.saveCatalogueItemFormatInformationToDisk(DiskManager.ORIGINAL_DISK_NUMBER, catalogueItemList, ramManager.getFat());

            ramManager.initRam();
            messageController.showTips("初始化磁盘信息成功");

            loadMemoryTable();
            StringBuffer text = new StringBuffer();
            for (OFTLE oftle : ramManager.getOftleList()) {
                text.append(oftle.getAbsoultRoute() + "\n");
            }
            openedFile.setText(text.toString());
            reLoadFatTable();
            writeFileContent.setText("");
            currentFileContent.setText("");
            initDirectory();
        } catch (IOException ex) {
            ex.printStackTrace();
            messageController.showTips("初始化磁盘信息失败");
        }

    }

    /**
     * 加载Fat表
     */
    @FXML
    private void loadFatTable() {
        ramManager.initRam();
        reLoadFatTable();
        initDirectory();
    }

    /**
     * 重Fat表
     */
    private void reLoadFatTable() {
        FatItemArrayList.clear();
        int[] fat = fatManager.returnTheFAT();
        for (int i = 0; i < fat.length; i++) {
            FatItemArrayList.add(new FatItem(i, fat[i]));
        }
        FatItemObservableList.clear();
        FatItemObservableList.addAll(FatItemArrayList);
    }

    /**
     * 创建文件目录
     */
    @FXML
    private void createDirectory() {
        input.setVisible(true);
        main.setDisable(true);
        InputController inputController = InputController.getInputController();
        inputController.setPathLabel("请输入新建的文件目录");
        inputController.setApplyEvent(event -> {
            String absouletRoute = inputController.getPathStr();//真正的absouleRoute从用户输入框获取//
            MessageController messageController = MessageController.getInstance();
            if (!(absouletRoute == null || absouletRoute.isEmpty() || "".equals(absouletRoute))) {
                absouletRoute = filter.initeFilte(absouletRoute);
                String tips = filter.filteDirectoryName(absouletRoute);
                if (!tips.isEmpty()) {
                    //输出提示
                    messageController.showTips(tips);
                } else {
                    tips = directoryOpreator.makeDirectory(absouletRoute, ramManager.getFat());
                    if (!tips.isEmpty()) {
                        //输出失败的原因，原因被存储在tips中
                        messageController.showTips(tips);
                    } else {
                        messageController.showTips("创建目录成功");
                        //重新加载Fat表
                        reLoadFatTable();
                        initDirectory();
                    }
                }
            } else {
                //目录为空
                messageController.showTips("目录路径为空");
            }
        });
    }

    private void initDirectory() {
        itemTreeItem.getChildren().clear();
        getCompleteDirectoryInfo(itemTreeItem, 2);
        pathTree.setRoot(itemTreeItem);
    }

    private void getCompleteDirectoryInfo(TreeItem<FileTreeItem> fatherItemTreeItem, int diskNumber) {
        ArrayList<CatalogueItem> catalogueItemList = directoryOpreator.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, ramManager.getFat());
        for (CatalogueItem catalogueItem : catalogueItemList) {
            String tempS = "";
            if (catalogueItem.getAttribute()[3] == 1) {
                for (int k = 0; k < catalogueItem.getName().length(); k++) {
                    if (catalogueItem.getName().charAt(k) != '$') {  //必需特别地去处理这个‘$’符号
                        tempS += catalogueItem.getName().charAt(k);
                    }
                }
                TreeItem<FileTreeItem> tempItemTreeItem = new TreeItem<>(new FileTreeItem(null, tempS, true));
                getCompleteDirectoryInfo(tempItemTreeItem, catalogueItem.getInitialDiskNumber());
                fatherItemTreeItem.getChildren().add(tempItemTreeItem);
            } else {
                for (int k = 0; k < catalogueItem.getName().length(); k++) {
                    if (catalogueItem.getName().charAt(k) != '$') {
                        tempS += catalogueItem.getName().charAt(k);
                    }
                }
                tempS += catalogueItem.getType();
                fatherItemTreeItem.getChildren().add(new TreeItem<>(new FileTreeItem(null, tempS, false)));
            }
        }
    }

    private void getDirectoryIndexList(ArrayList<Integer> routeIndexList, String[] routes, int cur, int diskNumber) {
        if (cur == routes.length) {
            return;
        }

        ArrayList<CatalogueItem> catalogueItemList = directoryOpreator.getCompleteCatalogueItemFormatInformationFromDisk(diskNumber, ramManager.getFat());

        for (CatalogueItem catalogueItem : catalogueItemList) {
            if (catalogueItem.getAttribute()[3] == 1) {
                String tempS = "";
                for (int k = 0; k < catalogueItem.getName().length(); k++) {
                    if (catalogueItem.getName().charAt(k) != '$') {  //必需特别地去处理这个‘$’符号
                        tempS += catalogueItem.getName().charAt(k);
                    }
                }
                if (routes[cur].equals(tempS)) {
                    routeIndexList.add(catalogueItemList.indexOf(catalogueItem));
                    getDirectoryIndexList(routeIndexList, routes, cur + 1, catalogueItem.getInitialDiskNumber());
                    break;
                }
            }
        }
    }

    @FXML
    private void showDirectoryInfo() {
        input.setVisible(true);
        main.setDisable(true);
        InputController inputController = InputController.getInputController();
        inputController.setPathLabel("请输入的文件目录");
        inputController.setApplyEvent(event -> {
            String absouletRoute = inputController.getPathStr();//真正的absouleRoute从用户输入框获取//
            MessageController messageController = MessageController.getInstance();
            if (!(absouletRoute == null || absouletRoute.isEmpty() || "".equals(absouletRoute))) {
                absouletRoute = this.filter.initeFilte(absouletRoute);
                String tips = this.filter.filteDirectoryName(absouletRoute);

                if (!tips.isEmpty()) {
                    //用一个框输出提示
                    messageController.showTips(tips);
                } else {
                    int diskNumber;
                    if ((diskNumber = this.directoryOpreator.searchDiskNumberOfStoringCatalogueInformation(absouletRoute, ramManager.getFat())) == -1) {
                        //提示该目录不存在
                        messageController.showTips("目录不存在");
                    } else {
                        ArrayList<CatalogueItem> catalogueItemList = this.diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);

                        String[] routes = absouletRoute.split("/");
                        ArrayList<Integer> routeIndexList = new ArrayList<>();
                        int nextDiskNumber = DiskManager.ORIGINAL_DISK_NUMBER;
                        switch (routes[0].toUpperCase()) {
                            case "C:":
                                routeIndexList.add(0);
                                nextDiskNumber = DiskManager.C_DISK_NUMBER;
                                break;
                            case "D:":
                                routeIndexList.add(1);
                                nextDiskNumber = DiskManager.D_DISK_NUMBER;
                                break;
                            case "E:":
                                routeIndexList.add(2);
                                nextDiskNumber = DiskManager.E_DISK_NUMBER;
                                break;
                            case "F:":
                                routeIndexList.add(3);
                                nextDiskNumber = DiskManager.F_DISK_NUMBER;
                                break;
                            case "G:":
                                routeIndexList.add(4);
                                nextDiskNumber = DiskManager.G_DISK_NUMBER;
                                break;
                        }

                        getDirectoryIndexList(routeIndexList, routes, 1, nextDiskNumber);

                        if (catalogueItemList.isEmpty()) {
                            //提示该目录是个空目录
                            messageController.showTips("空目录");
                            initDirectory();
                            //最后一层莫要展开
                            extendsItemTree(routeIndexList);
                        } else {
                            initDirectory();
                            //最后一层也要展开
                            extendsItemTree(routeIndexList);
                        }
                    }
                }

                //可能还要记下当前所在目录//
            }
        });
    }

    private void extendsItemTree(ArrayList<Integer> list) {
        TreeItem<FileTreeItem> item = itemTreeItem;
        for (int i : list) {
            item.setExpanded(true);
            item = item.getChildren().get(i);
        }
        item.setExpanded(true);
    }

    @FXML
    private void deleteDirectory() {
        input.setVisible(true);
        main.setDisable(true);
        InputController inputController = InputController.getInputController();
        inputController.setPathLabel("请输入删除的文件目录");
        inputController.setApplyEvent(event -> {
            String absouletRoute = inputController.getPathStr();//真正的absouleRoute从用户输入框获取//
            MessageController messageController = MessageController.getInstance();
            if (!(absouletRoute == null || absouletRoute.isEmpty() || "".equals(absouletRoute))) {
                absouletRoute = filter.initeFilte(absouletRoute);
                String tips = filter.filteDirectoryName(absouletRoute);
                if (!tips.isEmpty()) {
                    //用一个框输出提示
                    messageController.showTips(tips);
                } else {
                    tips = directoryOpreator.removeDirectory(absouletRoute, ramManager.getFat(), ramManager.getOftleList());
                    if (!tips.isEmpty()) {
                        //输出失败的原因，原因被存储在tips中
                        messageController.showTips(tips);
                    } else {
                        //提示删除目录成功
                        messageController.showTips("删除目录成功");
                        //重新加载Fat表
                        reLoadFatTable();
                        initDirectory();
                    }
                }
            } else {
                //目录为空
                messageController.showTips("目录路径为空");
            }
        });

    }

    @FXML
    private void appendInfoToFile() {
        input.setVisible(true);
        main.setDisable(true);
        InputController inputController = InputController.getInputController();
        inputController.setPathLabel("请输入要写的文件");
        inputController.setApplyEvent(event -> {
            String absouletRoute = inputController.getPathStr();//真正的absouleRoute从用户输入框获取//
            MessageController messageController = MessageController.getInstance();
            if (!(absouletRoute == null || absouletRoute.isEmpty() || "".equals(absouletRoute))) {
                absouletRoute = this.filter.initeFilte(absouletRoute);
                //将用户从输入框里面输入的东西弄进主存
                String tips = this.filter.filteFileName(absouletRoute);

                if (!tips.isEmpty()) {
                    //用一个框输出提示
                    messageController.showTips(tips);
                } else {
                    ramManager.setRamSpaceForWrite(new ArrayList<>());

                    String test = writeFileContent.getText();
                    for (int i = 0; i < test.length(); i++) {
                        ramManager.getRamSpaceForWrite().add(test.charAt(i));
                    }

                    int index = 0, b1_index = 0;
                    boolean isSucceed = true;
                    if (ramManager.getRamSpaceForWrite().isEmpty()) {
                        tips = fileOperator.writeFile(absouletRoute, ramManager.getBuffer1(), b1_index, ramManager.getFat(), ramManager.getOftleList());
                        if (!tips.isEmpty()) {
                            isSucceed = false;
                        }
                    } else {
                        while (index < ramManager.getRamSpaceForWrite().size()) {
                            if (b1_index == ramManager.getBuffer1().length) {
                                tips = fileOperator.writeFile(absouletRoute, ramManager.getBuffer1(), b1_index, ramManager.getFat(), ramManager.getOftleList());
                                if (!tips.isEmpty()) {
                                    b1_index = 0;
                                    isSucceed = false;
                                    break;
                                }
                                b1_index = 0;
                            } else {
                                ramManager.getBuffer1()[b1_index] = ramManager.getRamSpaceForWrite().get(index);
                                b1_index++;
                                index++;
                            }
                        }
                        if (b1_index > 0 && isSucceed) {  //缓冲区内还有内容，还要再写一遍
                            tips = fileOperator.writeFile(absouletRoute, ramManager.getBuffer1(), b1_index, ramManager.getFat(), ramManager.getOftleList());
                            if (!tips.isEmpty()) {
                                b1_index = 0;
                                isSucceed = false;
                            }
                        }
                    }

                    if (isSucceed) {
                        //提示用户输入添加的信息成功
                        messageController.showTips("输入添加的信息成功");
                        writeFileContent.setText("");
                    } else {
                        //输出tips里面的提示
                        messageController.showTips(tips);
                    }
                    //ramManager.setRamSpaceForWrite(new ArrayList<>());
                }
            }
            loadMemoryTable();
            StringBuffer text = new StringBuffer();
            for (OFTLE oftle : ramManager.getOftleList()) {
                text.append(oftle.getAbsoultRoute() + "\n");
            }
            openedFile.setText(text.toString());
            reLoadFatTable();
            initDirectory();
        });
    }

    /**
     * 读取内存数据
     */
    private void loadMemoryTable() {
        memoryItems.clear();
        int i = 0;
        memoryItems.add(new MemoryItem("=======读内存======="));
        StringBuffer string = new StringBuffer();
        for (char c : ramManager.getRamSpaceForRead()) {
            i++;
            string.append(c);
            if (i == 8) {
                i = 0;
                memoryItems.add(new MemoryItem(string.toString()));
                string = new StringBuffer();
            }
        }
        if (i != 0) {
            i = 0;
            memoryItems.add(new MemoryItem(string.toString()));
            string = new StringBuffer();
        }

        memoryItems.add(new MemoryItem("=======写内存======="));
        for (char c : ramManager.getRamSpaceForWrite()) {
            i++;
            string.append(c);
            if (i == 8) {
                i = 0;
                memoryItems.add(new MemoryItem(string.toString()));
                string = new StringBuffer();
            }
        }
        if (i != 0) {
            i = 0;
            memoryItems.add(new MemoryItem(string.toString()));
            string = new StringBuffer();
        }
        memoryItemObservableList.clear();
        memoryItemObservableList.addAll(memoryItems);
    }

    @FXML
    private void readFile() {
        input.setVisible(true);
        main.setDisable(true);
        InputController inputController = InputController.getInputController();
        inputController.setPathLabel("请输入文件路径");
        inputController.readFile();
        inputController.setApplyEvent(event -> {
            String absouletRoute = inputController.getPathStr();//真正的absouleRoute从用户输入框获取//
            MessageController messageController = MessageController.getInstance();
            if (!(absouletRoute == null || absouletRoute.isEmpty() || "".equals(absouletRoute))) {
                if (inputController.getReadLength() != 0) {
                    absouletRoute = this.filter.initeFilte(absouletRoute);
                    int readLength = inputController.getReadLength();//真正的absouleRoute和length从用户输入框获取//
                    String tips = this.filter.filteFileName(absouletRoute);
                    if (!tips.isEmpty()) {
                        //用一个框输出提示
                        messageController.showTips(tips);
                    } else {
                        ramManager.setRamSpaceForRead(new ArrayList<>());
                        tips = fileOperator.readFile(absouletRoute, readLength, ramManager.getRamSpaceForRead(), ramManager.getFat(), ramManager.getOftleList());
                        if (!tips.isEmpty()) {
                            //输出tips里面的提示
                            messageController.showTips(tips);
                        } else {
                            //提示成功
                            messageController.showTips("读取文件成功");
                            loadMemoryTable();
                        }
                    }
                } else {
                    messageController.showTips("文件长度输入错误");
                }
            }
            StringBuffer text = new StringBuffer();
            for (OFTLE oftle : ramManager.getOftleList()) {
                text.append(oftle.getAbsoultRoute() + "\n");
            }
            openedFile.setText(text.toString());
            inputController.closeReadFile();
        });
    }

    @FXML
    private void createFile() {
        input.setVisible(true);
        main.setDisable(true);
        InputController inputController = InputController.getInputController();
        inputController.setPathLabel("请输入新建的文件");
        inputController.setApplyEvent(event -> {
            String absouletRoute = inputController.getPathStr();//真正的absouleRoute从用户输入框获取//
            MessageController messageController = MessageController.getInstance();
            if (!(absouletRoute == null || absouletRoute.isEmpty() || "".equals(absouletRoute))) {
                absouletRoute = this.filter.initeFilte(absouletRoute);
                String tips = this.filter.filteFileName(absouletRoute);
                if (!tips.isEmpty()) {
                    //输出tips里面的提示
                    messageController.showTips(tips);
                } else {
                    tips = fileOperator.creatFile(absouletRoute, attribute, ramManager.getFat(), ramManager.getOftleList());
                    if (!tips.isEmpty()) {
                        //输出tips里面的提示
                        messageController.showTips(tips);
                    } else {
                        //提示成功
                        messageController.showTips("成功新建文件");
                        reLoadFatTable();
                        initDirectory();
                    }
                }
            }
            StringBuffer text = new StringBuffer();
            for (OFTLE oftle : ramManager.getOftleList()) {
                text.append(oftle.getAbsoultRoute() + "\n");
            }
            openedFile.setText(text.toString());
        });
    }

    @FXML
    private void openFile() {
        input.setVisible(true);
        main.setDisable(true);
        InputController inputController = InputController.getInputController();
        inputController.setPathLabel("请输入打开的文件");
        inputController.setApplyEvent(event -> {
            String absouletRoute = inputController.getPathStr();//真正的absouleRoute从用户输入框获取//
            MessageController messageController = MessageController.getInstance();
            if (!(absouletRoute == null || absouletRoute.isEmpty() || "".equals(absouletRoute))) {
                absouletRoute = this.filter.initeFilte(absouletRoute);
                String tips = this.filter.filteFileName(absouletRoute);
                if (!tips.isEmpty()) {
                    //输出tips里面的提示
                    messageController.showTips(tips);
                } else {
                    tips = fileOperator.openFile(absouletRoute, 1, ramManager.getFat(), ramManager.getOftleList());
                    if (!tips.isEmpty()) {
                        //输出tips里面的提示
                        messageController.showTips(tips);
                    } else {
                        //提示成功
                        messageController.showTips("成功打开文件");
                    }
                }
            }
            StringBuffer text = new StringBuffer();
            for (OFTLE oftle : ramManager.getOftleList()) {
                text.append(oftle.getAbsoultRoute() + "\n");
            }
            openedFile.setText(text.toString());
        });
    }

    @FXML
    private void showFileInfo() {
        input.setVisible(true);
        main.setDisable(true);
        InputController inputController = InputController.getInputController();
        inputController.setPathLabel("请输入文件路径");
        inputController.setApplyEvent(event -> {
            String absouletRoute = inputController.getPathStr();//真正的absouleRoute从用户输入框获取//
            MessageController messageController = MessageController.getInstance();
            if (!(absouletRoute == null || absouletRoute.isEmpty() || "".equals(absouletRoute))) {
                //可能要用正则表达式判断absouletRoute是否合法//
                absouletRoute = this.filter.initeFilte(absouletRoute);
                String tips = this.filter.filteFileName(absouletRoute);
                if (!tips.isEmpty()) {
                    //用一个框输出提示
                    messageController.showTips(tips);
                } else {
                    String fatherDirectory, fileNameType;
                    fatherDirectory = absouletRoute.substring(0, absouletRoute.lastIndexOf('/'));
                    fileNameType = absouletRoute.substring(absouletRoute.lastIndexOf('/') + 1);
                    /* 父目录-子目录搜索法 */

                    int diskNumber;
                    if ((diskNumber = directoryOpreator.searchDiskNumberOfStoringCatalogueInformation(fatherDirectory, ramManager.getFat())) == -1) {
                        //提示该文件不存在
                        messageController.showTips("文件不存在");
                    } else {
                        ArrayList<CatalogueItem> catalogueItemList = diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);
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
                            //提示该文件不存在
                            messageController.showTips("文件不存在");
                        }

                        if (oftleManager.returnOFTLEIndex(ramManager.getOftleList(), absouletRoute) != -1) {
                            //提示文件已被打开，无法查看
                            messageController.showTips("文件已被打开，无法查看!");
                        } else {
//                OFTLE tempOFTLET = oftleList.get(oftleNumber);
                            int temp = catalogueItem.getInitialDiskNumber();
                            ArrayList<Character> fileInformationPerDisk;
                            String f_information = absouletRoute + "\n";

                            do {
                                fileInformationPerDisk = diskManager.getFileFormatInformationFromDisk(temp);
                                for (Character fileInformationPerDisk1 : fileInformationPerDisk) {
                                    if (fileInformationPerDisk1 != '#') {
                                        f_information += fileInformationPerDisk1;
                                    }
                                }
                                temp = ramManager.getFat()[temp];
                            } while (temp != FATManager.END_FLAG);

                            //将f_information内容用一个框显示出来
                            currentFileContent.setText(f_information);
                            messageController.showTips("显示文件内容成功");
                        }
                    }
                }
            }
        });
    }

    /**
     * 删除文件
     */
    @FXML
    private void deleteFile() {
        input.setVisible(true);
        main.setDisable(true);
        InputController inputController = InputController.getInputController();
        inputController.setPathLabel("请输入删除的文件路径");
        inputController.setApplyEvent(event -> {
            String absouletRoute = inputController.getPathStr();//真正的absouleRoute从用户输入框获取//
            MessageController messageController = MessageController.getInstance();
            if (!(absouletRoute == null || absouletRoute.isEmpty() || "".equals(absouletRoute))) {
                absouletRoute = this.filter.initeFilte(absouletRoute);

                String tips = this.filter.filteFileName(absouletRoute);
                if (!tips.isEmpty()) {
                    //用一个框输出提示
                    messageController.showTips(tips);
                } else {
                    tips = fileOperator.deleteFile(absouletRoute, ramManager.getFat(), ramManager.getOftleList());

                    if (!tips.isEmpty()) {
                        //输出tips里面的提示
                        messageController.showTips(tips);
                    } else {
                        //提示成功
                        messageController.showTips("删除成功");
                        reLoadFatTable();
                        initDirectory();
                    }
                }
            }
        });
    }

    /**
     * 关闭文件
     */
    @FXML
    private void closeFile() {
        input.setVisible(true);
        main.setDisable(true);
        InputController inputController = InputController.getInputController();
        inputController.setPathLabel("请输入要关闭的文件");
        inputController.setApplyEvent(event -> {
            String absouletRoute = inputController.getPathStr();//真正的absouleRoute从用户输入框获取//
            MessageController messageController = MessageController.getInstance();
            if (!(absouletRoute == null || absouletRoute.isEmpty() || "".equals(absouletRoute))) {
                absouletRoute = this.filter.initeFilte(absouletRoute);
                String tips = this.filter.filteFileName(absouletRoute);
                if (!tips.isEmpty()) {
                    //用一个框输出提示
                    messageController.showTips(tips);
                } else {
                    tips = fileOperator.closeFile(absouletRoute, ramManager.getFat(), ramManager.getOftleList());
                    if (tips.isEmpty()) {
                        //提示关闭成功！
                        messageController.showTips("关闭成功");
                    } else {
                        //提示关闭失败！
                        messageController.showTips(tips);
                    }
                }
            }
            StringBuffer text = new StringBuffer();
            for (OFTLE oftle : ramManager.getOftleList()) {
                text.append(oftle.getAbsoultRoute() + "\n");
            }
            openedFile.setText(text.toString());
        });

    }

    @FXML
    private void changeFileAttribute() {
        input.setVisible(true);
        main.setDisable(true);
        InputController inputController = InputController.getInputController();
        inputController.setPathLabel("请输入文件路径和选择属性");
        inputController.showSelected();
        inputController.setApplyEvent(event -> {
            String absouletRoute = inputController.getPathStr();//真正的absouleRoute从用户输入框获取//
            MessageController messageController = MessageController.getInstance();
            if (!(absouletRoute == null || absouletRoute.isEmpty() || "".equals(absouletRoute))) {
                //属性号码
                absouletRoute = this.filter.initeFilte(absouletRoute);
                String tips = this.filter.filteFileName(absouletRoute);
                int num = inputController.getSelectNum();

                if (!tips.isEmpty()) {
                    //用一个框输出提示
                    messageController.showTips(tips);
                } else {
                    tips = fileOperator.change(absouletRoute, num - 1, ramManager.getFat(), ramManager.getOftleList());
                    if (!tips.isEmpty()) {
                        messageController.showTips(tips);
                    } else {
                        if (num == 1) {
                            messageController.showTips("文件修改为普通模式成功！");
                        } else {
                            messageController.showTips("文件修改为只读模式成功！");
                        }
                    }
                }
            }
            inputController.closeSelected();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController = this;
        input.setVisible(false);
        msg.setVisible(false);

        FatItemObservableList = FXCollections.observableArrayList(FatItemArrayList);
        diskNumber1.setCellValueFactory(new PropertyValueFactory<>("diskNumber1"));
        diskNumber2.setCellValueFactory(new PropertyValueFactory<>("diskNumber2"));
        fatTable.setItems(FatItemObservableList);

        memoryItemObservableList = FXCollections.observableArrayList(memoryItems);
        memoryColumn.setCellValueFactory(new PropertyValueFactory<>("memory"));
        memoryTable.setItems(memoryItemObservableList);

        MessageController messageController = MessageController.getInstance();
        messageController.setApplyEvent(event1 -> {
            //消息窗口消失
            //输入窗口消失
            //主窗口可用
            msg.setVisible(false);
            input.setVisible(false);
            main.setDisable(false);
        });
        //关闭窗口事件
        Main.stage.setOnCloseRequest(event -> {
            if (ramManager.getOftleList() != null) {
                ArrayList<String> closeFileAbsoulteRoute = new ArrayList<>();
                for (OFTLE oftle : ramManager.getOftleList()) {
                    closeFileAbsoulteRoute.add(oftle.getAbsoultRoute());
                }

                for (String fileAbsoulteRoute : closeFileAbsoulteRoute) {  //关闭进程的同时把所有打开的文件都关掉
                    fileOperator.closeFile(fileAbsoulteRoute, ramManager.getFat(), ramManager.getOftleList());
                }
            }
            System.exit(0);
        });
    }
}

package com.os.controller;

import com.os.disk.DiskManager;
import com.os.entity.FatItem;
import com.os.operate.CatalogueItem;
import com.os.operate.DirectoryOpreator;
import com.os.operate.FileOperator;
import com.os.operate.Filter;
import com.os.ram.FATManager;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
    private TreeView pathTree;
    @FXML
    private TextArea openedFile, currentFileContent;
    @FXML
    private TableColumn diskNumber1, diskNumber2;
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
                    }
                }
            } else {
                //目录为空
                messageController.showTips("目录路径为空");
            }

        });
    }

    @FXML
    private void showDirectoryInfo() {
        //todo
        String absouletRoute = "";//真正的absouleRoute从用户输入框获取//
        absouletRoute = this.filter.initeFilte(absouletRoute);

        String tips = this.filter.filteDirectoryName(absouletRoute);
        if (!tips.isEmpty()) {
            //用一个框输出提示
        } else {
            int diskNumber;
            if ((diskNumber = this.directoryOpreator.searchDiskNumberOfStoringCatalogueInformation(absouletRoute, ramManager.getFat())) == -1) {
                //提示该目录不存在
            } else {
                ArrayList<CatalogueItem> catalogueItemList = this.diskManager.getCatalogueItemFormatInformationFromDisk(diskNumber);

                if (catalogueItemList.isEmpty()) {
                    //提示该目录是个空目录
                } else {
                    //你用一些漂亮的效果将其显示出来呗
                }
            }
        }

        //可能还要记下当前所在目录//
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
        //todo
        String absouletRoute = "";//真正的absouleRoute从用户输入框获取//
        absouletRoute = this.filter.initeFilte(absouletRoute);

        //将用户从输入框里面输入的东西弄进主存//
        String tips = this.filter.filteFileName(absouletRoute);
        if (!tips.isEmpty()) {
            //用一个框输出提示
        } else {
            int index = 0, b1_index = 0;
            boolean isSucceed = true;
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
            if (isSucceed) {
                //提示用户输入添加的信息成功
            } else {
                //输出tips里面的提示
            }
            ramManager.setRamSpaceForWrite(new ArrayList<>());
        }
    }

    @FXML
    private void readFile() {
        //todo
        String absouletRoute = "";
        absouletRoute = this.filter.initeFilte(absouletRoute);

        int readLength = 0;//真正的absouleRoute和length从用户输入框获取//

        String tips = this.filter.filteFileName(absouletRoute);
        if (!tips.isEmpty()) {
            //用一个框输出提示
        } else {
            ramManager.setRamSpaceForRead(new ArrayList<>());
            tips = fileOperator.readFile(absouletRoute, readLength, ramManager.getRamSpaceForRead(), ramManager.getFat(), ramManager.getOftleList());

            if (!tips.isEmpty()) {
                //输出tips里面的提示
            } else {
                //提示成功
            }
        }
    }

    @FXML
    private void createFile() {
        //todo
        String absouletRoute = "";
        
        absouletRoute = this.filter.initeFilte(absouletRoute);
        String tips = this.filter.filteFileName(absouletRoute);
        if (!tips.isEmpty()) {
            //输出tips里面的提示
        } else {
            tips = fileOperator.creatFile(absouletRoute, attribute, ramManager.getFat(), ramManager.getOftleList());
            if (!tips.isEmpty()) {
                //输出tips里面的提示
            } else {
                //提示成功
            }
        }
    }

    @FXML
    private void openFile() {
        //todo
        String absouletRoute = "";
        
        absouletRoute = this.filter.initeFilte(absouletRoute);
        String tips = this.filter.filteFileName(absouletRoute);
        
        if (!tips.isEmpty()) {
            //输出tips里面的提示
        } else {
            tips = fileOperator.openFile(absouletRoute, 0, ramManager.getFat(), ramManager.getOftleList());
            if (!tips.isEmpty()) {
                //输出tips里面的提示
            } else {
                //提示成功
            }
        }
    }

    @FXML
    private void showFileInfo() {
        //todo
        String absouletRoute = "";//真正的absouleRoute从用户输入框获取//
        //可能要用正则表达式判断absouletRoute是否合法//
        absouletRoute = this.filter.initeFilte(absouletRoute);

        String tips = this.filter.filteFileName(absouletRoute);
        if (!tips.isEmpty()) {
            //用一个框输出提示
        } else {
            String fatherDirectory, fileNameType;
            fatherDirectory = absouletRoute.substring(0, absouletRoute.lastIndexOf('/'));
            fileNameType = absouletRoute.substring(absouletRoute.lastIndexOf('/') + 1);
            /* 父目录-子目录搜索法 */

            int diskNumber;
            if ((diskNumber = directoryOpreator.searchDiskNumberOfStoringCatalogueInformation(fatherDirectory, ramManager.getFat())) == -1) {
                //提示该文件不存在
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
                }

                if (oftleManager.returnOFTLEIndex(ramManager.getOftleList(), absouletRoute) != -1) {
                    //提示文件已被打开，无法查看
                } else {
//                OFTLE tempOFTLET = oftleList.get(oftleNumber);
                    int temp = catalogueItem.getInitialDiskNumber();
                    ArrayList<Character> fileInformationPerDisk;
                    String f_information = "";

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
                }
            }
        }
    }

    @FXML
    private void deleteFile() {
        //todo
        String absouletRoute = "";//真正的absouleRoute从用户输入框获取//
        absouletRoute = this.filter.initeFilte(absouletRoute);

        String tips = this.filter.filteFileName(absouletRoute);
        if (!tips.isEmpty()) {
            //用一个框输出提示
        } else {
            tips = fileOperator.deleteFile(absouletRoute, ramManager.getFat(), ramManager.getOftleList());

            if (!tips.isEmpty()) {
                //输出tips里面的提示
            } else {
                //提示成功
            }
        }
    }

    @FXML
    private void closeFile() {
        //todo
        String absouletRoute = "";//真正的absouleRoute从用户输入框获取//
        absouletRoute = this.filter.initeFilte(absouletRoute);

        String tips = this.filter.filteFileName(absouletRoute);
        if (!tips.isEmpty()) {
            //用一个框输出提示
        } else {
            tips = fileOperator.closeFile(absouletRoute, ramManager.getFat(), ramManager.getOftleList());
            if (tips.isEmpty()) {
                //提示关闭成功！
            } else {
                //提示关闭失败！
            }
        }

    }

    @FXML
    private void changeFileAttribute() {
        //todo
        //实现我亲自说
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
        MessageController messageController = MessageController.getInstance();
        messageController.setApplyEvent(event1 -> {
            //消息窗口消失
            //输入窗口消失
            //主窗口可用
            msg.setVisible(false);
            input.setVisible(false);
            main.setDisable(false);
        });
    }
}

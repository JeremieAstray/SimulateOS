package com.os.controller;

import com.os.disk.DiskManager;
import com.os.entity.FatItem;
import com.os.operate.DirectoryOpreator;
import com.os.operate.FileOperator;
import com.os.ram.FATManager;
import com.os.ram.RAMManager;
import com.os.utils.MsgQueue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableView fatTable;
    @FXML
    private TableColumn diskNumber1,diskNumber2;

    private ObservableList<FatItem> FatItemObservableList;
    private ArrayList<FatItem> FatItemArrayList = new ArrayList<>();

    /* 初始化主存，建立操作器 */
    private RAMManager ramManager = new RAMManager();
    private DirectoryOpreator directoryOpreator = new DirectoryOpreator();
    private FileOperator fileOperator = new FileOperator();
    private DiskManager diskManager = new DiskManager();
    private FATManager fatManager = new FATManager();

    private MsgQueue<String> queue  = new MsgQueue<>();

    @FXML
    private void initDiskInfo(){
        /* 初始化磁盘信息 */
        try {
            for (int i = 0; i < 128; i++) {
                File file = new File(i + ".txt");
                PrintWriter output = new PrintWriter(file);
                output.close();
            }
            int[] testFat = new int[128];
            testFat[0] = 1;
            testFat[1] = FATManager.END_FLAG;
            testFat[2] = FATManager.END_FLAG;
            for (int i = 3; i < testFat.length; i++) {
                testFat[i] = 0;
            }
            fatManager.saveFATToDisk(testFat);
        }catch(IOException ex){
            ex.printStackTrace();
            System.out.println("初始化磁盘信息失败");
        }
    }

    @FXML
    private void loadFatTable(){
        /* 加载Fat表 */
        ramManager.initRam();
        FatItemArrayList.clear();
        int[] fat = fatManager.returnTheFAT();
        for (int i = 0; i < fat.length; i++)
            FatItemArrayList.add(new FatItem(i,fat[i]));
        FatItemObservableList.clear();
        FatItemObservableList.addAll(FatItemArrayList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FatItemObservableList = FXCollections.observableArrayList(FatItemArrayList);
        diskNumber1.setCellValueFactory(new PropertyValueFactory<>("diskNumber1"));
        diskNumber2.setCellValueFactory(new PropertyValueFactory<>("diskNumber2"));
        fatTable.setItems(FatItemObservableList);
    }
}

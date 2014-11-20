package com.os.disk;

import java.util.ArrayList;
import com.os.operate.CatalogueItem;

/**
 *
 * @author FeiFan Liang
 */
public class DiskCodeInterpreter {

    String[] typeList = new String[255];
    int typeListSize;

    public DiskCodeInterpreter() {
        this.loadTypeList();
    }

    public char explainToChar(int code) {
        if (code == 0) {
            return '$';
        } else {
            return (char) code;
        }
    }

    public int[] explainToAttribute(int code) {
        int t_i = 0;
        int t_attribute = code;
        int[] tempAttribute = new int[8];
        while (t_attribute != 0) {
            tempAttribute[t_i] = t_attribute % 2;
            t_attribute /= 2;
            t_i++;  //小心！这里的二进制码是从低位到高位的
        }
        return tempAttribute;
    }

    public String exlpainToFileType(int code) {
        return this.typeList[code];
    }

    public int turnTheCharToCode(char c) {
        if (c == '$') {
            return 0;
        } else {
            return (int) c;
        }
    }

    @SuppressWarnings("empty-statement")
    public int turnTheAttributeToCode(int[] tempAttribute) {
        int index, code = 0;
        for (index = tempAttribute.length - 1; index >= 0 && tempAttribute[index] == 0; index--);
        //System.out.println("index为:"+index);
        if (index == -1) {
            return 0;
        } else {
            for (int i = index; i >= 0; i--) {
                code = code * 2 + tempAttribute[i];
                //System.out.println("i = "+i+"code = "+code);
            }
            return code;
        }
    }

    public int turnTheFileTypeToCode(String s) {
        for (int i = 0; i < typeListSize; i++) {
            if (s.equals(this.typeList[i])) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<CatalogueItem> explainDiskInformationForCatalogueFormat(ArrayList<Integer> diskOriginInformation) { //以后实现的时候把此传递参数改为ArrayList//
        ArrayList<CatalogueItem> catalogueItemList = new ArrayList<>();
        for (int i = 0; i < diskOriginInformation.size(); i += 8) {
            String tempS = "";
            for (int j = i; j < i + 3; j++) {
                tempS += this.explainToChar(diskOriginInformation.get(j));
            }

            String tempType = "";
            tempType += this.explainToChar(diskOriginInformation.get(i + 3)) + this.exlpainToFileType(diskOriginInformation.get(i + 4));
            int[] t_attribute = this.explainToAttribute(diskOriginInformation.get(i + 5));
            int firstAddress = diskOriginInformation.get(i + 6);
            int length = diskOriginInformation.get(i + 7);

            CatalogueItem catalogueItem = new CatalogueItem(tempS, tempType, t_attribute, firstAddress, length);
            catalogueItemList.add(catalogueItem);
        }
        return catalogueItemList;
    }

    public ArrayList<Character> explainDiskInformationForFileFormat(ArrayList<Integer> diskOriginInformation) {
        ArrayList<Character> fileInformation = new ArrayList<>();
        for (int i = 0; i < diskOriginInformation.size(); i++) {
            //文件结束符读进来
            fileInformation.add(this.explainToChar(diskOriginInformation.get(i)));
        }
        return fileInformation;
    }

    public ArrayList<Integer> turnCatalogueFormatInformationToDiskInformation(ArrayList<CatalogueItem> catalogueItemList) {
        ArrayList<Integer> diskOriginInformation = new ArrayList<>();
        int[] byteInformation = new int[8];

        for (int i = 0; i < catalogueItemList.size(); i++) {
            byteInformation[0] = this.turnTheCharToCode(catalogueItemList.get(i).getName().charAt(0));
            byteInformation[1] = this.turnTheCharToCode(catalogueItemList.get(i).getName().charAt(1));
            byteInformation[2] = this.turnTheCharToCode(catalogueItemList.get(i).getName().charAt(2));
            byteInformation[3] = this.turnTheCharToCode(catalogueItemList.get(i).getType().charAt(0));
            byteInformation[4] = this.turnTheFileTypeToCode(catalogueItemList.get(i).getType().substring(1));
            byteInformation[5] = this.turnTheAttributeToCode(catalogueItemList.get(i).getAttribute());
            byteInformation[6] = catalogueItemList.get(i).getInitialDiskNumber();
            byteInformation[7] = catalogueItemList.get(i).getLength();

            for (int j = 0; j < byteInformation.length; j++) {
                diskOriginInformation.add(byteInformation[j]);
            }
        }
//        System.out.println("转码以后的目录项");
//        for (int i = 0; i < information.size(); i++) {
//            System.out.print("第" + i + "个字节：" + information.get(i) + " ");
//        }
//        System.out.println("");

        return diskOriginInformation;
    }

    public ArrayList<Integer> turnFileFormatInformationToDiskInformation(ArrayList<Character> fileInformation) {
        ArrayList<Integer> diskOriginInformation = new ArrayList<>();
        for (Character fileInformation1 : fileInformation) {
            diskOriginInformation.add(this.turnTheCharToCode(fileInformation1));
        }
        return diskOriginInformation;
    }

    public void loadTypeList() {
        this.typeList[0] = "";  //代表空类型，暂定
        this.typeList[1] = "txt";
        this.typeListSize = 2;
    }
}

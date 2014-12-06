/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.os.operate;

import com.os.disk.DiskCodeInterpreter;

/**
 *
 * @author Administrator
 */
public class Filter {

    public Filter() {

    }

    public String initeFilte(String absoluteRoute) {
        if (absoluteRoute.lastIndexOf("/") == absoluteRoute.length() - 1) {
            return absoluteRoute.substring(0, absoluteRoute.length() - 1);
        } else {
            return absoluteRoute;
        }
    }

    public String filteDirectoryName(String absoluteRoute) {
        if (absoluteRoute.lastIndexOf("/") == -1) {
            if (absoluteRoute.length() == 0) {
                //return true;
                return "";
            } else {
                switch (absoluteRoute.toUpperCase()) {
                    case "C:":
                        //return true;
                        return "";
                    case "D:":
                        //return true;
                        return "";
                    case "E:":
                        //return true;
                        return "";
                    case "F:":
                        //return true;
                        return "";
                    case "G:":
                        //return true;
                        return "";
                    default:
                        //System.out.println("根目录非法！");
                        //return false;
                        return "你输入的根目录非法！";
                }
            }
        }

        String[] s = absoluteRoute.split("/");  //将路径名分隔

        switch (s[0].toUpperCase()) {
            case "C:":
                break;
            case "D:":
                break;
            case "E:":
                break;
            case "F:":
                break;
            case "G:":
                break;
            default:
                return "你输入的根目录非法！";
        }

        for (int i = 1; i < s.length; i++) {
            String subDirectory = s[i];
            if (subDirectory.isEmpty()) {
                //System.out.println("第" + (i + 1) + "层" + "目录的名字为空！");
                //return false;
                return "你输入的第" + (i + 1) + "层" + "目录的名字为空！";
            }
            if (subDirectory.length() > 3) {
//                System.out.println("第" + (i + 1) + "层" + "目录名的长度超过3！");
//                return false;
                return "你输入的第" + (i + 1) + "层" + "目录名的长度超过3！";
            }
            if (!subDirectory.matches("[^$^\\.]+")) {
//                System.out.println("第" + (i + 1) + "层" + "目录名中含有非法字符\"$\"或者\".\"");
                return "你输入的第" + (i + 1) + "层" + "目录名中含有非法字符\"$\"或者\".\"";
            }
        }

//        return true;
        return "";
    }

    public String filteFileName(String absoluteRoute) {
        DiskCodeInterpreter diskCodeInterpreter = new DiskCodeInterpreter();

        if (absoluteRoute.lastIndexOf("/") == -1) {
//            System.out.println("该文件的绝对路径名非法！");
//            return false;
            return "你输入文件的绝对路径名非法！";
        }

        String[] s = absoluteRoute.split("/");  //将路径名分隔

        switch (s[0].toUpperCase()) {
            case "C:":
                break;
            case "D:":
                break;
            case "E:":
                break;
            case "F:":
                break;
            case "G:":
                break;
            default:
                return "你输入的根目录非法！";
        }

        for (int i = 1; i < s.length - 1; i++) {  //扫描创建文件的父目录情况
            String subDirectory = s[i];
            if (subDirectory.isEmpty()) {
//                System.out.println("第" + (i + 1) + "层" + "目录的名字为空！");
//                return false;
                return "你输入的第" + (i + 1) + "层" + "目录的名字为空！";
            }
            if (subDirectory.length() > 3) {
//                System.out.println("第" + (i + 1) + "层" + "目录名的长度超过3！");
//                return false;
                return "你输入的第" + (i + 1) + "层" + "目录名的长度超过3！";
            }
            if (!subDirectory.matches("[^$^\\.]+")) {
//                System.out.println("第" + (i + 1) + "层" + "目录名中含有非法字符\"$\"或者\".\"");
//                return false;
                return "你输入的第" + (i + 1) + "层" + "目录名中含有非法字符\"$\"或者\".\"";
            }
        }

        if (s[s.length - 1].lastIndexOf(".") == -1) {
//            System.out.println("新文件缺乏所需类型！");
//            return false;
            return "你输入的新文件缺乏所需类型！";
        }

        String fileName = s[s.length - 1].substring(0, s[s.length - 1].lastIndexOf("."));
        String fileType = s[s.length - 1].substring(s[s.length - 1].lastIndexOf(".") + 1);

        if (fileName.matches(".")) {
//            System.out.println("文件名中含非法字符\".\"！");
//            return false;
            return "文件名中含非法字符\".\"！";
        }
        if (fileName.length() > 3) {
//            System.out.println("文件名长度超过3！");
//            return false;
            return "文件名长度超过3！";
        }

        if (diskCodeInterpreter.turnTheFileTypeToCode(fileType) == 0) {
//            System.out.println("新文件缺乏所需类型！");
//            return false;
            return "你输入的新文件缺乏所需类型！";
        }
        if (diskCodeInterpreter.turnTheFileTypeToCode(fileType) == -1) {
//            System.out.println("该文件类型是非法类型！");
//            return false;
            return "该文件类型是非法类型！";
        }

        return "";
    }
}

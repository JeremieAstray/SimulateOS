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
        String tempAbsoulteRoute = "";
        int index = 0;
        while (index < absoluteRoute.length()) {  //处理路径分隔符问题
            if (absoluteRoute.charAt(index) == '\\') {
                tempAbsoulteRoute += '/';
                index++;
            } else if (index < absoluteRoute.length() - 1
                    && absoluteRoute.charAt(index) == '/'
                    && absoluteRoute.charAt(index + 1) == '/') {
                tempAbsoulteRoute += '/';
                index += 2;
            } else {
                tempAbsoulteRoute += absoluteRoute.charAt(index);
                index++;
            }
        }

        absoluteRoute = "";
        for (int i = 0; i < tempAbsoulteRoute.length(); i++) {
            absoluteRoute += tempAbsoulteRoute.charAt(i);
        }

        if (absoluteRoute.lastIndexOf("/") == absoluteRoute.length() - 1) {
            return absoluteRoute.substring(0, absoluteRoute.length() - 1);
        } else {
            return absoluteRoute;
        }
    }

    public String filteDirectoryName(String absoluteRoute) {
        if (absoluteRoute.lastIndexOf("/") == -1) {
            if (absoluteRoute.length() == 0) {
                return "";
            } else {
                switch (absoluteRoute.toUpperCase()) {
                    case "C:":
                        return "";
                    case "D:":
                        return "";
                    case "E:":
                        return "";
                    case "F:":
                        return "";
                    case "G:":
                        return "";
                    default:
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
                return "你输入的第" + (i + 1) + "层" + "目录的名字为空！";
            }
            if (subDirectory.length() > 3) {
                return "你输入的第" + (i + 1) + "层" + "目录名的长度超过3！";
            }
            if (!subDirectory.matches("[^$^\\.]+")) {
                return "你输入的第" + (i + 1) + "层" + "目录名中含有非法字符\"$\"或者\".\"";
            }
        }

        return "";
    }

    public String filteFileName(String absoluteRoute) {
        DiskCodeInterpreter diskCodeInterpreter = new DiskCodeInterpreter();

        if (absoluteRoute.lastIndexOf("/") == -1) {
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
                return "你输入的第" + (i + 1) + "层" + "目录的名字为空！";
            }
            if (subDirectory.length() > 3) {
                return "你输入的第" + (i + 1) + "层" + "目录名的长度超过3！";
            }
            if (!subDirectory.matches("[^$^\\.]+")) {
                return "你输入的第" + (i + 1) + "层" + "目录名中含有非法字符\"$\"或者\".\"";
            }
        }

        if (s[s.length - 1].lastIndexOf(".") == -1) {
            return "你输入的新文件缺乏所需类型！";
        }

        String fileName = s[s.length - 1].substring(0, s[s.length - 1].lastIndexOf("."));
        String fileType = s[s.length - 1].substring(s[s.length - 1].lastIndexOf(".") + 1);

        if (!fileName.matches("[^$^\\.]+")) {
            return "文件名中含非法字符\".\"或者\".\"！";
        }
        if (fileName.length() > 3) {
            return "文件名长度超过3！";
        }

        if (diskCodeInterpreter.turnTheFileTypeToCode(fileType) == 0) {
            return "你输入的新文件缺乏所需类型！";
        }
        if (diskCodeInterpreter.turnTheFileTypeToCode(fileType) == -1) {
            return "该文件类型是非法类型！";
        }

        return "";
    }
}

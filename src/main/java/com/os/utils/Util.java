package com.os.utils;

import java.lang.reflect.Array;

/**
 * Created by Jeremie on 2014/10/9.
 */
public class Util {

    public static <T> T[] newArray(Class<T> clazz, int number) {
        T[] array = (T[]) Array.newInstance(clazz, number);
        try {
            for (int i = 0; i < number; i++) {
                array[i] = clazz.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return array;
    }
}

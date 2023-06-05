package com.financemanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Helper {

    private static Map<String, Integer> monthMap = new HashMap<>();

    static {
        monthMap.put("January", 0);
        monthMap.put("February", 1);
        monthMap.put("March", 2);
        monthMap.put("April", 3);
        monthMap.put("May", 4);
        monthMap.put("June", 5);
        monthMap.put("July", 6);
        monthMap.put("August", 7);
        monthMap.put("September", 8);
        monthMap.put("October", 9);
        monthMap.put("November", 10);
        monthMap.put("December", 11);
    }
    
    public static String getGitVersion() throws IOException{
        InputStream inputStream = Runtime.getRuntime().exec("git describe --abbrev=7 --always  --long --match v* main").getInputStream();
        try (Scanner s = new Scanner(inputStream).useDelimiter("\\A")) {
            String result = s.hasNext() ? s.next() : null;
            return result;
        }
    }

    public static int countElementsIn3dArray(String[][][] array) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                count += array[i][j].length;
            }
        }
        return count;
    }

    public static void verifyDatabase(JDBC jdbc) {
        int curr_year = Calendar.getInstance().get(Calendar.YEAR);
        int max_year = jdbc.getMaxYear();

        if (max_year != curr_year) {
            jdbc.addYear(Calendar.getInstance().get(Calendar.YEAR));
        }
    }
    
    public static float currencyToFloat(String s) {
        if (s.charAt(0) == '$') {
            s = s.substring(1);
          }
         
          return Float.parseFloat(s);
    }

    public static int monthToInt(String month) {
        return monthMap.get(month);
    }

    @Deprecated
    public static String[][] convertListToArray(List<String> list, int row_count, int col_count) {
        String[][] array = new String[row_count][col_count]; 
       
        for (int i = 0; i < row_count; i++) {
            for (int j = 0; j < col_count; j++) {
                
                int index = i * col_count + j;
                if (index < list.size() && list.get(index) != "") {
                    array[i][j] = list.get(index);
                } 
                else {
                    array[i][j] = null;
                }
            }
        }
        return array;
    }

    @Deprecated
    public static void print2DArray(String[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }
}

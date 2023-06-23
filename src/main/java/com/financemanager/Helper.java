package com.financemanager;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.financemanager.type.Category;
import com.financemanager.type.Header;

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

    public static String floatToCurrency(float f) {
        return String.format("$%.02f", f);
    }

    public static LocalDate stringToDate(String date_string) {
        String[] tokens = date_string.split("-");
        int year = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1]);
        int day = Integer.parseInt(tokens[2]);
        return LocalDate.of(year, month, day);
    }

    public static int monthToInt(String month) {
        return monthMap.get(month);
    }

    public static Category idToCategory(int id, Header[] headers) {
        for (Header header : headers) {
            for (Category category : header.categories) {
                if (category.id == id) {
                    return category;
                }
            }
        }
        return null;
    }

    public static int getCategoryId(String type_name, String header_name, String category_name, Header[] headers) {
        int id = -1;
        for (Header header : headers) {
            if (header.type.equals(type_name) && header.name.equals(header_name)) {
                for (Category category : header.categories) {
                    if (category.name.equals(category_name)) {
                        id = category.id;
                    }
                }
            }
        }
        return id;
    }
    
    public static String getMinDate(int year, int month) {
        return LocalDate.of(year, month + 1, 1).toString();
    }

    public static String getMaxDate(int year, int month) {
        return LocalDate.of(year, month + 1, 1).with(TemporalAdjusters.lastDayOfMonth()).toString();
    }

    public static <T> T[] combineArrays(T[] array1, T[] array2) {
        int length = array1.length + array2.length;
        T[] result = Arrays.copyOf(array1, length);
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
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

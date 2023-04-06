package com.financemanager.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import com.financemanager.Header;

public class Helper {
    
    public static String getGitVersion() throws IOException{

        InputStream inputStream = Runtime.getRuntime().exec("git describe --abbrev=7 --always  --long --match v* main").getInputStream();
        try (Scanner s = new Scanner(inputStream).useDelimiter("\\A")) {
            String result = s.hasNext() ? s.next() : null;
            return result;
        }
    }

    public static int getTableHeight(Header[] headers) {
        int header_count = headers.length;
        int category_count = 0;
        for (Header header : headers) {
            category_count += header.categories.length;
        }
        return 2 * header_count + category_count + 2 * 2;
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

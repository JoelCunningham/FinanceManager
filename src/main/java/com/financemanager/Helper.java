package com.financemanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Helper {
    
    public static String getGitVersion() throws IOException{

        InputStream inputStream = Runtime.getRuntime().exec("git describe --abbrev=7 --always  --long --match v* main").getInputStream();
        try (Scanner s = new Scanner(inputStream).useDelimiter("\\A")) {
            String result = s.hasNext() ? s.next() : null;
            return result;
        }
    }
}

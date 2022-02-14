package com.assingment.Milestone2.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JSONReaderHelper {
    public static String read(String path) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(path));
        String content = "";

        while (sc.hasNextLine())
            content += sc.nextLine();
        sc.close();

        return content;
    }
}

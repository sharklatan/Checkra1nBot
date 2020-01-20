package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class AppTest {
    public static void main(String[] args) throws IOException {
        File file = new File("tweets.log");
        FileWriter fileWriter = new FileWriter(file, true);
        for (int i = 0; i < 10; i++) {
            fileWriter.write("test\n");
        }
        fileWriter.close();
    }
}
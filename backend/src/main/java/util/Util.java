package util;

import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    public static String getSql(String name) throws IOException {
        String fileName = "wahlparser/src/res/sql-statements/" + name;

        File file = new File(fileName);

        List<String> result = Files.readAllLines(file.toPath());
        return result.stream().collect(Collectors.joining("\n"));
    }
}

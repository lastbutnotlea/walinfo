import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ParseWords {

    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner = new Scanner(new File("wahlparser/src/res/wortsammlung.txt"));
        StringBuilder stringBuilder = new StringBuilder();

        String start = "INSERT INTO words (word_id, word) VALUES ";
        String end = ";";

        int counter = 0;
        boolean firstWord = true;

        stringBuilder.append(start);

        while (scanner.hasNextLine()){
            String word = scanner.nextLine();
            if(word.length() > 3 && word.length() < 13 && !word.contains("-")) {
                if(firstWord) {
                    stringBuilder.append("(");
                    firstWord = false;
                }
                else stringBuilder.append(",(");

                stringBuilder.append(counter);
                stringBuilder.append(",");
                stringBuilder.append("'");
                stringBuilder.append(word);
                stringBuilder.append("'");
                stringBuilder.append(")");
                counter++;
            }
        }
        stringBuilder.append(end);

        System.out.println(stringBuilder.toString());

        try (Connection             conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5433/walinfo", "postgres",
                "mysecretpassword");) {
            Statement statement = conn.createStatement();
            statement.execute(stringBuilder.toString());
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

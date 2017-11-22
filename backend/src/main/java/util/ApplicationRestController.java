package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicLong;

import hello.Greeting;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationRestController {

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/test")
    @CrossOrigin(origins = "http://localhost:4200")
    public Greeting greeting() throws SQLException {

        Connection conn = DatabaseConnection.getConnection();
        Statement statement = conn.createStatement();

        statement.execute("select count(*) from kandidaten");

        ResultSet result = statement.getResultSet();

        String text = null;
        while (result.next()) {
            text = result.getString(1);
        }

        conn.close();

        return new Greeting(counter.incrementAndGet(),
                text);
    }
}
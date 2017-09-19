package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private final Connection connection;

    public DataSource() {
        this.connection = createConnection();
    }

    private static Connection createConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            /*MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setDatabaseName("game");
            dataSource.setUser("root");
            dataSource.setPassword("u6t9eR67");*/

            StringBuilder url = null;
            try {
                url = new StringBuilder()
                        .append("jdbc:mysql://")
                        .append("localhost:")
                        .append("3306/")
                        .append("game?")
                        .append("user=root&")
                        .append("password=")
                        .append(getPassword());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("URL: " + url + "\n");

            assert url != null;
            return DriverManager.getConnection(url.toString());
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    private static String getPassword() throws IOException {
        Path path = Paths.get("databaseRootPassword.txt");
        if (Files.exists(path)) {
            return Files.readAllLines(path).get(0);
        } else {
            throw new FileNotFoundException();
        }
    }
}

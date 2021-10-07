package com.evsyukoov.project;

import com.evsyukoov.project.errors.ServerError;
import com.evsyukoov.project.messages.Message;
import org.h2.tools.RunScript;
import org.h2.tools.Server;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.sql.*;

public class H2Server {

    private static Server server;

    public static void run() throws Exception {
        try {
            server = Server.createTcpServer("-tcpAllowOthers").start();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServerError(Message.H2_ERROR);
        }
        initDb();
    }

    public static void stop() throws Exception {
        server.stop();
    }

    private static void initDb() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:h2:~/BankTask", "sa","");
        RunScript.execute(connection, new InputStreamReader(
                H2Server.class.getClassLoader().getResourceAsStream("init/drop_schema.sql")));
        RunScript.execute(connection, new InputStreamReader(
                H2Server.class.getClassLoader().getResourceAsStream("init/init_schema.sql")));
        RunScript.execute(connection, new InputStreamReader(
                H2Server.class.getClassLoader().getResourceAsStream("init/data.sql")));
    }
}

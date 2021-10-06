package com.evsyukoov.project;

import com.evsyukoov.project.errors.ServerError;
import com.evsyukoov.project.init.DataBaseInitializerParser;
import com.evsyukoov.project.messages.Message;
import org.h2.tools.Server;

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
        new DataBaseInitializerParser().load();
    }

    public static void stop() throws Exception {
        server.stop();
    }
}

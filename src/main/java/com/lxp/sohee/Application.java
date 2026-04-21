package com.lxp.sohee;

import com.lxp.sohee.config.JDBCConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        try (Connection connection = JDBCConnection.getConnection();
                Scanner sc = new Scanner(System.in);) {
            System.out.println("연결 성공: " + connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

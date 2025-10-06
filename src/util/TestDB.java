package util;

import util.DBConnection;

public class TestDB {
    public static void main(String[] args) {
        if(DBConnection.getConnection() != null) {
            System.out.println("DB Connected!");
        } else {
            System.out.println("DB Connection Failed!");
        }
    }
}

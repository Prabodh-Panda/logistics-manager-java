package home;

import DatabaseConnector.MySQLConnection;
import guiapplication.Bookings;
import guiapplication.Home;
import guiapplication.initialPopUp;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Prabodh Kumar Panda on 4/8/2021.
 */

public class LogisticsManager {


    private static void createOrLoadConfigFile() {
        //Create File and Directory if doesn't exist
        File configDir = new File("C:\\Logistics_Manager");
        File configFile = new File("C:\\Logistics_Manager\\logistics_manager.config");
        configDir.mkdir();
        try {
            configFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(configFile.length() == 0){
            initialPopUp popUp = new initialPopUp();
            popUp.setVisible(true);
        } else {

            try {
                FileReader fr = new FileReader("C:\\Logistics_Manager\\logistics_manager.config");
                Properties p = new Properties();
                p.load(fr);
                String usrname = p.getProperty("username");
                String pswd = p.getProperty("password");

                MySQLConnection.setCredentials(usrname,pswd);

                fr.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Home home = new Home();
            home.setVisible(true);
        }
    }

    public static void main(String[] args) throws SQLException {
        createOrLoadConfigFile();
    }
}
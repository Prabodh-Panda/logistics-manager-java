package guiapplication;

import DatabaseConnector.MySQLConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Prabodh Kumar Panda on 6/16/2021.
 */
public class initialPopUp extends JFrame{
    private JPanel rootPanel;
    private JTextField userTF;
    private JTextField passTF;
    private JButton createButton;

    private String username;
    private String password;


    public initialPopUp() {
        add(rootPanel);
        setTitle("Logistics Manager | Login");
        setSize(400, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = userTF.getText();
                password = passTF.getText();

                try {
                    FileWriter fw = new FileWriter("C:\\Logistics_Manager\\logistics_manager.config",true);
                    fw.write("username=" + username + "\n");
                    fw.write("password=" + password);
                    fw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                MySQLConnection.setCredentials(username,password);

                Home home = new Home();
                home.setVisible(true);

                close();
            }
        });
    }

    private void close(){
        this.dispose();
    }
}

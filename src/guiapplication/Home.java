package guiapplication;

import DatabaseConnector.MySQLConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Prabodh Kumar Panda on 4/8/2021.
 */
public class Home extends JFrame{

    //Generated Variables

    private JTextField textField1;
    private JPasswordField passwordField1;
    private JComboBox comboBox2;
    private JButton loginButton;
    private JButton validateButton;
    private JPanel rootPanel;
    private JButton registerButton;

    //Declared Variables
    private String username,password,loginRole;
    private int CMPID,BRNCHID;
    private boolean validated = false;
    public Home() {

        add(rootPanel);
        setTitle("Logistics Manager | Login");
        setSize(400, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = textField1.getText();
                password = String.valueOf(passwordField1.getPassword());
                validated = validate(username,password);
                comboBox2.removeAllItems();
                if(validated){
                    MySQLConnection conn = new MySQLConnection();
                    ResultSet rs = conn.query("select * from branches where CMPID = " + CMPID);
                    try {
                        while (rs.next()){
                            String name = rs.getString("Name");
                            comboBox2.addItem(name);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }finally {
                        conn.shutConnection();
                    }
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validated){

                    String branchName = comboBox2.getSelectedItem().toString();
                    MySQLConnection conn = new MySQLConnection();
                    ResultSet rs = conn.query("select BRNCHID from branches where Name = " + "\'" + branchName + "\'");
                    try {
                        if(rs.next()) BRNCHID = rs.getInt("BRNCHID");
                        Bookings bookings = new Bookings(CMPID,BRNCHID);
                        bookings.setVisible(true);
                        close();
                    } catch (SQLException e1) {
                        System.err.println("ERROR while fetching Branch ID");
                    } finally {
                        conn.shutConnection();
                    }
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register register = new Register();
                register.setVisible(true);
                close();
            }
        });
    }


    private boolean validate(String username, String password) {
        MySQLConnection conn = new MySQLConnection();
        ResultSet rs = conn.query("select * from users where username = " + "\'" + username + "\'");
        try {
            if (rs.next()) {
                if(password.equals(rs.getString("password"))){
                    CMPID = rs.getInt("CMPID");
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            System.err.println("VALIDATION FAILED");
            e.printStackTrace();
            return false;
        } finally {
            conn.shutConnection();
        }
        return false;
    }

    private void close(){
        this.dispose();
    }
}

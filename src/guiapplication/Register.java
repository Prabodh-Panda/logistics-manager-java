package guiapplication;

import DatabaseConnector.MySQLConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Prabodh Kumar Panda on 4/15/2021.
 */
public class Register extends JFrame{
    private JTabbedPane RegistrationTabbedPane;
    private JPanel rootPanel;
    private JTextField CMPIDTF;
    private JTextField CMPNAMETF;
    private JTextField USRNAMETF;
    private JButton registerButton1;
    private JTextField validateUserField;
    private JButton validateButton;
    private JTextField CMPLOCATIONTF;
    private JTextField BranchIDField;
    private JTextField BranchNameTF;
    private JTextField BranchLocationTF;
    private JButton registerButton;
    private JPasswordField validatePasswordField;
    private JPasswordField PASSTF;
    private JTextField CMPIDTextField;

    private int CMPID, BRNCHID;

    public Register(){
        add(rootPanel);
        setTitle("Registration");
        setSize(600,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            initializeNewCompanyRegistration();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        registerButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createNewCompany();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validateNewBranchCreation();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createNewBranch();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void initializeNewCompanyRegistration() throws SQLException {
        MySQLConnection conn = new MySQLConnection();
        ResultSet rs = conn.query("select auto_increment from information_schema.TABLES WHERE TABLE_SCHEMA = 'logistics_manager' and TABLE_NAME = 'companies'");
        if(rs.next()){
            int CMPID = rs.getInt("auto_increment");
            CMPIDTF.setText(String.valueOf(CMPID));
        }
        CMPNAMETF.setText("");
        USRNAMETF.setText("");
        CMPLOCATIONTF.setText("");
        PASSTF.setText("");

        conn.shutConnection();
    }

    private void createNewCompany() throws SQLException {
        MySQLConnection conn = new MySQLConnection();
        String pass = String.valueOf(PASSTF.getPassword());
        conn.createNewCompany(CMPNAMETF.getText(),CMPLOCATIONTF.getText(),USRNAMETF.getText(),pass, Integer.parseInt(CMPIDTF.getText()));
        conn.shutConnection();
        initializeNewCompanyRegistration();
    }

    private void validateNewBranchCreation() throws SQLException {
        String username = validateUserField.getText();
        String password = String.valueOf(validatePasswordField.getPassword());
        MySQLConnection conn = new MySQLConnection();
        ResultSet rs = conn.query("select * from users where username = " + "\'" + username + "\'");
        if (rs.next()) {
            if(password.equals(rs.getString("password"))){
                CMPID = rs.getInt("CMPID");
                CMPIDTextField.setText(String.valueOf(CMPID));

                rs = conn.query("select auto_increment from information_schema.TABLES WHERE TABLE_SCHEMA = 'logistics_manager' and TABLE_NAME = 'branches'");
                rs.next();
                BRNCHID = rs.getInt("auto_increment");
                BranchIDField.setText(String.valueOf(BRNCHID));

            } else{
                System.out.println("Invalid Password");
                System.out.println(password);
            }
        } else {
            System.out.println("Invalid Username");
        }

        conn.shutConnection();
    }

    private void initializeNewBranchRegistration() {
        validateUserField.setText("");
        validatePasswordField.setText("");

        CMPIDTextField.setText("");
        BranchIDField.setText("");
        BranchLocationTF.setText("");
        BranchNameTF.setText("");
    }

    private void createNewBranch() throws  SQLException {
        MySQLConnection conn = new MySQLConnection();
        conn.createNewBranch(CMPID, BranchNameTF.getText(), BranchLocationTF.getText());
        initializeNewBranchRegistration();
    }
}

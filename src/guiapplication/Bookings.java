package guiapplication;

import DatabaseConnector.MySQLConnection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.print.Book;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by Prabodh Kumar Panda on 4/8/2021.
 */
public class Bookings extends JFrame{
    private JTabbedPane tabbedPane;
    private JTable table1;
    private JTextField shippingIDTF;
    private JTextField orderIDTF;
    private JTextField itemWeightTF;
    private JTextField itemValueTF;
    private JTextField itemsTF;
    private JComboBox ToCB;
    private JComboBox vehicleCB;
    private JButton createNewBookingButton;
    private JTable table2;
    private JTextField regTF;
    private JTextField ownerTF;
    private JTextField driverTF;
    private JButton registerNewVehicleButton;
    private JPanel panelMain;
    private JLabel headingLabel;
    private JTextField dateTF;
    private JTextField fromTF;
    private JTable sendShippingTable;
    private JButton sendShippingButton;
    private JTable recieveShippingTable;
    private JButton recieveBookingButton;


    private MySQLConnection conn = new MySQLConnection();
    private String cmpname, brnchname;
    private int CMPID, BRNCHID;

    public Bookings(int CMPID, int BRNCHID) {
        this.CMPID = CMPID;
        this.BRNCHID = BRNCHID;
        setCMPBRNCHName(CMPID,BRNCHID);
        headingLabel.setText(cmpname+"("+CMPID+")"+ " / " +brnchname+"("+BRNCHID+")");
        add(panelMain);
        setTitle("Logistics Manager -- DEV");
        setSize(900,600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch (tabbedPane.getSelectedIndex()){
                    case 0:
                        initializeAllBookings();
                        break;
                    case 1:
                        initializeNewBooking();
                        break;
                    case 2:
                        initializeRecieveBooking();
                        break;
                    case 3:
                        initializeSendBooking();
                        break;
                    case 4:
                        initializeAllVehicles();
                        break;
                }
            }
        });
        initializeAllBookings();
        createNewBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String orderID = orderIDTF.getText();
                    int FromBRNCHID = BRNCHID;
                    String RegNo = vehicleCB.getSelectedItem().toString();
                    String Items = itemsTF.getText();
                    int ItemValue = Integer.parseInt(itemValueTF.getText());
                    int ItemWeight = Integer.parseInt(itemWeightTF.getText());
                    String BRNCHName = ToCB.getSelectedItem().toString();
                    ResultSet rs = conn.query("select * from branches where Name = " + "\'" + BRNCHName + "\'");
                    String date = dateTF.getText();
                    if(rs.next()){
                        int toID = rs.getInt("BRNCHID");
                        conn.insertNewShipping(orderID,FromBRNCHID,toID,RegNo,Items,ItemValue,ItemWeight,date);
                        initializeNewBooking();
                    }
                } catch (Exception excp) {
                    excp.printStackTrace();
                }
            }
        });
        sendShippingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ShippingID = (int) sendShippingTable.getValueAt(sendShippingTable.getSelectedRow(),0);
                String vehicleReg = (String) sendShippingTable.getValueAt(sendShippingTable.getSelectedRow(),6);
                try {
                    conn.sendShipping(ShippingID, vehicleReg);
                    initializeSendBooking();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        registerNewVehicleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String vehicleRegNo = regTF.getText();
                String owner = ownerTF.getText();
                String driver = driverTF.getText();
                try {
                    conn.RegisterNewVehicle(vehicleRegNo,owner,driver,CMPID);
                    regTF.setText("");
                    ownerTF.setText("");
                    driverTF.setText("");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        recieveBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int shippingID = (int) recieveShippingTable.getValueAt(recieveShippingTable.getSelectedRow(),0);
                String vehicleReg = (String) recieveShippingTable.getValueAt(recieveShippingTable.getSelectedRow(),6);
                try {
                    conn.recieveShipping(shippingID,vehicleReg);
                    initializeRecieveBooking();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void setCMPBRNCHName(int CMPID, int BRNCHID) {
        ResultSet rs = conn.query("select Name from companies where CMPID = " + CMPID);
        try {
            if(rs.next()){
                cmpname = rs.getString("Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet rs2 = conn.query("select Name from branches where BRNCHID = " + BRNCHID);
        try {
            if(rs2.next()){
                brnchname = rs2.getString("Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeAllBookings(){
        ResultSet rs = conn.query("select * from shippings where FromBRNCHID = " + BRNCHID + " or ToBRNCHID = " + BRNCHID);
        try{
            table1.setModel(buildTableModel(rs));
        } catch (SQLException e){
            e.printStackTrace();
        }
        table1.getColumnModel().getColumn(0).setPreferredWidth(50);
        table1.getColumnModel().getColumn(1).setPreferredWidth(50);
        table1.getColumnModel().getColumn(2).setPreferredWidth(50);
        table1.getColumnModel().getColumn(3).setPreferredWidth(50);
        table1.getColumnModel().getColumn(4).setPreferredWidth(150);
    }

    private void initializeNewBooking(){
        ResultSet rs = conn.query("select auto_increment from information_schema.TABLES WHERE TABLE_SCHEMA = 'logistics_manager' and TABLE_NAME = 'shippings'");
        vehicleCB.removeAllItems();
        ToCB.removeAllItems();
        shippingIDTF.setText("");
        itemsTF.setText("");
        itemValueTF.setText("");
        itemWeightTF.setText("");
        try {
            if(rs.next()){
                int next_shipping_id = rs.getInt("auto_increment");
                shippingIDTF.setText(Integer.toString(next_shipping_id));
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateobj = new Date();
                dateTF.setText(df.format(dateobj));
            }
            rs = conn.query("select * from branches where BRNCHID = " + BRNCHID);
            if(rs.next()){
                fromTF.setEditable(false);
                fromTF.setText(rs.getString("Name"));
                rs = conn.query("select * from branches where CMPID = " + CMPID);
                while (rs.next()){
                    ToCB.addItem(rs.getString("name"));
                }
                rs = conn.query("select * from vehicles where CMPID = " + CMPID + " AND Status = 'AVAILABLE'");
                while (rs.next()){
                    vehicleCB.addItem(rs.getString("reg_no"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeRecieveBooking(){
        ResultSet rs = conn.query("select * from shippings where ToBRNCHID = " + BRNCHID + " AND Status = 'SHIPPED'");
        try {
            recieveShippingTable.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeAllVehicles(){
        ResultSet rs = conn.query("select * from vehicles where CMPID = " + CMPID);
        try {
            table2.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeSendBooking() {
        ResultSet rs = conn.query("select * from shippings where FromBRNCHID = " + BRNCHID + " AND Status = 'ORDERED'");
        try {
            sendShippingTable.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException{
        ResultSetMetaData rsMetaData = rs.getMetaData();


        //Name of Columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = rsMetaData.getColumnCount();
        for(int column = 1; column <= columnCount; column++){
            columnNames.add(rsMetaData.getColumnName(column));
        }

        //Data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()){
            Vector<Object> vector = new Vector<Object>();
            for(int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }





















}

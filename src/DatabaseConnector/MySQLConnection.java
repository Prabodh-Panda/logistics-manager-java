package DatabaseConnector;

/**
 * Created by Prabodh Kumar Panda on 3/27/2021.
 */

import java.sql.*;

public class MySQLConnection {

    Connection conn;
    Statement stmt;
    ResultSet rs;

    static String username = "root";
    static String password = "";

    public MySQLConnection() {
        try {
            Class.forName("java.sql.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/logistics_manager", username, password);
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQLException");
            e.printStackTrace();
        }
    }

    public static void setCredentials(String username, String password) {
        MySQLConnection.username = username;
        MySQLConnection.password = password;
    }

    public ResultSet query(String query){
        try {
            rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertNewShipping(String orderID,int FromBRNCHID, int ToBRNCHID,String VehRegNo, String Items, int ItemValue, int ItemWeight, String date) throws SQLException {
        String SQL = "INSERT INTO `logistics_manager`.`shippings` (`OrderID`, `FromBRNCHID`, `ToBRNCHID`, `VehicleRegNo`, `Items`, `ItemsValue`, `ItemsWeight`, `BookedOn`) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1,orderID);
        pstmt.setInt(2, FromBRNCHID);
        pstmt.setInt(3, ToBRNCHID);
        pstmt.setString(4, VehRegNo);
        pstmt.setString(5, Items);
        pstmt.setInt(6, ItemValue);
        pstmt.setInt(7, ItemWeight);
        pstmt.setString(8, date);
        pstmt.executeUpdate();

        SQL = "UPDATE `vehicles` SET `status`='BOOKED' WHERE `Reg_No`= " + "\'" + VehRegNo + "\'";
        pstmt = conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();
    }

    public void sendShipping(int shippingID, String vehicleReg) throws SQLException {
        String SQL = "UPDATE `shippings` SET `Status`='SHIPPED' WHERE `ShippingID`= " + shippingID;
        PreparedStatement pstmt = conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();

        SQL = "UPDATE `vehicles` SET `Status`='IN TRANSIT' WHERE `Reg_No`= " + '\'' + vehicleReg + '\'';
        pstmt = conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();
    }

    public void recieveShipping(int shippingID, String vehicleReg) throws SQLException {
        String SQL = "UPDATE `shippings` SET `Status`='DELIVERED' WHERE `ShippingID`= " + shippingID;
        PreparedStatement pstmt = conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();

        SQL = "UPDATE `vehicles` SET `Status`='AVAILABLE' WHERE `Reg_No`= " + '\'' + vehicleReg + '\'';
        pstmt = conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();
    }

    public void RegisterNewVehicle(String vehicleRegNo, String Owner, String Driver, int CMPID) throws  SQLException{
        String SQL = "INSERT INTO `logistics_manager`.`vehicles` (`Reg_No`, `Owner`, `Driver`, `CMPID`) VALUES (?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1,vehicleRegNo);
        pstmt.setString(2,Owner);
        pstmt.setString(3,Driver);
        pstmt.setInt(4,CMPID);
        pstmt.executeUpdate();

        SQL = "UPDATE `vehicles` SET `Status`='AVAILABLE' WHERE `Reg_No`= " + '\'' + vehicleRegNo + '\'';
        pstmt = conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();
    }

    public void createNewCompany(String Name, String Location, String username, String password, int CMPID) throws SQLException {
        String SQL = "INSERT INTO `logistics_manager`.`companies`(`Name`,`Location`) VALUES (?,?)";
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1,Name);
        pstmt.setString(2,Location);
        pstmt.executeUpdate();
        SQL = "INSERT INTO `logistics_manager`.`users` VALUES (?,?,?)";
        pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1,username);
        pstmt.setString(2,password);
        pstmt.setInt(3,CMPID);
        pstmt.executeUpdate();
    }

    public void createNewBranch(int CMPID, String Name, String Location) throws SQLException {
        String SQL = "INSERT INTO `logistics_manager`.`branches`(`Name`,`Location`, `CMPID`) VALUES (?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1,Name);
        pstmt.setString(2,Location);
        pstmt.setInt(3,CMPID);
        pstmt.executeUpdate();
    }

    public int shutConnection() {
        try {
            rs.close();
            return 0;
        } catch (Exception e) {
            System.out.println("NO Result Set Found");
        }
        try {
            stmt.close();
            conn.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
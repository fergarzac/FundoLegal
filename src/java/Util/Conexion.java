/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author FernandoG
 */
public class Conexion {
    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            //Connection con=DriverManager.getConnection("jdbc:mysql://192.168.1.4:3306/fondolegal","boletas","bol34*12");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/fondolegal","root","");
            return con;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static void close(Connection con){
        try{
            con.close();
        }catch(Exception e){
            
        }
    }
}

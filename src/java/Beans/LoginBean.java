/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Modelo.Login;
import Util.Conexion;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author FernandoG
 */
@ManagedBean
@RequestScoped
public class LoginBean implements Serializable{
    private static final long serialVersionUID=1L;
    
    Connection con = null;
    PreparedStatement ps=null;
    Login user= new Login();
    private String usuario,contraseña;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    
    public LoginBean() {
    }
    
   
    
    public boolean valida(String usuario,String contraseña) throws SQLException{
        try{
            con = Conexion.getConnection();
            ps=con.prepareStatement("SELECT * from login where usuario = ? and contraseña = ?");  
            ps.setString(1, usuario);
            ps.setString(2, contraseña);        
            ResultSet res=ps.executeQuery();
            
            if(res.next()){
                res.close();
                ps.close();
                con.close();
                return true;
            }
            res.close();
            ps.close();
            con.close();
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage("sesion:password", new FacesMessage("Error en base de datos "+e.getMessage(),"Error en base de datos "+e.getMessage()));
            Conexion.close(con);
            return false;
        }
        return false;
    }

    public String validacion(){
        boolean validar;
        System.out.println("Usuario " +this.usuario);
        try {
            validar = valida(this.usuario,getMD5(this.contraseña));
            if(validar){
                return "admin";
            }else{
                FacesContext.getCurrentInstance().addMessage("sesion:password", new FacesMessage("Usuario y Contraseña Incorrectos.","Ingresa un Usuario y Contraseña Correctos."));
                return "index";
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            return "index";
        }
        
    }
    
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

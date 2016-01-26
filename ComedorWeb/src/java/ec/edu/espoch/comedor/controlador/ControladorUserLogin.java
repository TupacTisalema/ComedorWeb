/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espoch.comedor.controlador;

import ec.edu.espoch.comedor.modelo.mLogin;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import wsInfoCarrera.Persona;
import wsSeguridad.RolCarrera;

/**
 *
 * @author Tupac
 */
@ManagedBean
@SessionScoped
@Named("controladorUserLogin")
public class ControladorUserLogin implements Serializable {

    /*
     Atributos
     */
    private static final long serialVersionUID = -2152389656664659476L;
    private String username;
    private String password;
    private Persona objUserLogin;
    private boolean logeado = false;
    private RolCarrera rolCarrera;

    /**
     * Creates a new instance of ControladorUserLogin
     */
    public ControladorUserLogin() {
    }

    public boolean estaLogeado() {
        return logeado;
    }

    public RolCarrera getRolCarrera() {
        return rolCarrera;
    }
    /*
     Getters and Setters
     */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*
     Login
     */
    public void login(ActionEvent event) {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage message = null;

        if (username != null && password != null) {
            ArrayList<RolCarrera> lstRoles;
            lstRoles = new ArrayList<>();
            lstRoles = (ArrayList<RolCarrera>) mLogin.loginUsuario(username, password);
            if (lstRoles.size() > 0) {
                logeado = true;
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@", username);
                rolCarrera = lstRoles.get(0);
                this.objUserLogin = mLogin.datosUsuario(rolCarrera.getCodigoCarrera(), username);
            } else {
                logeado = false;
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Credenciales no válidas");
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        context.addCallbackParam("loggedIn", logeado);
        if (logeado) {
            context.addCallbackParam("view", "UsuarioNormal/bienvenida/inicio.xhtml");
        }
    }

    /*
     private void redireccionarPaginas(RequestContext context) {
     switch (this.objUsuarioLogin.getObjRol().getStrDescripcionRol()) {
     case "Administrador":
     context.addCallbackParam("view", "Administrador/bienvenida/inicio.xhtml");
     break;
     case "Secretaria":
     context.addCallbackParam("view", "Secretaria/bienvenida/inicio.xhtml");
     break;
     default:
     context.addCallbackParam("view", "UsuarioNormal/bienvenida/inicio.xhtml");
     }*/
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espoch.comedor.modelo;

import java.util.List;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import ec.edu.espoch.comedor.handler.LogMessageHandler;
import java.util.ArrayList;
import wsInfoCarrera.Persona;
import wsSeguridad.ArrayOfRolCarrera;
import wsSeguridad.RolCarrera;

/**
 *
 * @author Tupac
 */
public class mLogin {

    /*
     Autenticar Usuario
     */
    public static List<RolCarrera> loginUsuario(String strUsuarioCedula, String strUsuarioPassword) {
        List<RolCarrera> roles = new ArrayList<>();
        try {
            ArrayOfRolCarrera usuario = autenticarUsuarioCarrera(strUsuarioCedula, strUsuarioPassword);
            if (usuario != null) {
                List<RolCarrera> rol = usuario.getRolCarrera();
                rol.stream().forEach((Rol) -> {
                    /*
                     System.out.println("\nCod Carrera: " + Rol.getCodigoCarrera());
                     System.out.println("Rol: " + Rol.getNombreRol());
                     */

                    roles.add(Rol);
                });
            }
        } catch (Exception e) {
            throw e;
        }
        return roles;
    }

    /*
     Consumir servicio para autenticar  
     */
    private static ArrayOfRolCarrera autenticarUsuarioCarrera(java.lang.String login, java.lang.String password) {
        wsSeguridad.Seguridad service = new wsSeguridad.Seguridad();
        wsSeguridad.SeguridadSoap port = service.getSeguridadSoap();

        BindingProvider bindingProvider = (BindingProvider) port;
        Binding binding = bindingProvider.getBinding();
        List<Handler> handlerChain = binding.getHandlerChain();
        handlerChain.add(new LogMessageHandler());
        binding.setHandlerChain(handlerChain);

        return port.autenticarUsuarioCarrera(login, password);
    }

    /*
     Consumo servico para recoger datos de la persona al autenticar
     */
    public static Persona datosUsuario(String strCarreraCod, String strUsuarioCedula) {
        Persona usuPersona = new Persona();
        try {
            usuPersona = getDatosUsuarioCarrera(strCarreraCod, strUsuarioCedula);
        } catch (Exception e) {
            throw e;
        }
        return usuPersona;
    }

    private static wsInfoCarrera.Persona getDatosUsuarioCarrera(java.lang.String codCarrera, java.lang.String cedula) {
        wsInfoCarrera.InfoCarrera service = new wsInfoCarrera.InfoCarrera();
        wsInfoCarrera.InfoCarreraSoap port = service.getInfoCarreraSoap();
        BindingProvider bindingProvider = (BindingProvider) port;
        Binding binding = bindingProvider.getBinding();
        List<Handler> handlerChain = binding.getHandlerChain();
        handlerChain.add(new LogMessageHandler());
        binding.setHandlerChain(handlerChain);
        return port.getDatosUsuarioCarrera(codCarrera, cedula);
    }

}

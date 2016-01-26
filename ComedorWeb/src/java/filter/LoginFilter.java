/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import ec.edu.espoch.comedor.controlador.ControladorUserLogin;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ©foqc
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = {"*.xhtml"})
public class LoginFilter implements Filter {

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public LoginFilter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("LoginFilter:DoBeforeProcessing");
        }

        // Write code here to process the request and/or response before
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log items on the request object,
        // such as the parameters.
	/*
         for (Enumeration en = request.getParameterNames(); en.hasMoreElements(); ) {
         String name = (String)en.nextElement();
         String values[] = request.getParameterValues(name);
         int n = values.length;
         StringBuffer buf = new StringBuffer();
         buf.append(name);
         buf.append("=");
         for(int i=0; i < n; i++) {
         buf.append(values[i]);
         if (i < n-1)
         buf.append(",");
         }
         log(buf.toString());
         }
         */
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("LoginFilter:DoAfterProcessing");
        }

	// Write code here to process the request and/or response after
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log the attributes on the
        // request object after the request has been processed. 
	/*
         for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) {
         String name = (String)en.nextElement();
         Object value = request.getAttribute(name);
         log("attribute: " + name + "=" + value.toString());

         }
         */
        // For example, a filter might append something to the response.
	/*
         PrintWriter respOut = new PrintWriter(response.getWriter());
         respOut.println("<P><B>This has been appended by an intrusive filter.</B>");
         */
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        // Obtengo el bean que representa el usuario desde el scope sesión
        ControladorUserLogin loginBean = (ControladorUserLogin) req.getSession().getAttribute("controladorUserLogin");

        //Proceso la URL que está requiriendo el cliente
        String urlStr = req.getRequestURL().toString().toLowerCase();
        boolean noProteger = noProteger(urlStr);
        //Si no requiere protección(true) continúo normalmente.
        if (noProteger) {
            System.out.println("no proteger!!");
            chain.doFilter(request, response);
        } else {
            //El usuario no está logueado
            if ((loginBean == null || !loginBean.estaLogeado())) {
                res.sendRedirect(req.getContextPath() + "/faces/index.xhtml");
            } else {
                //El recurso requiere protección, pero el usuario ya está logueado.                
                if (paginasAdmin(urlStr, loginBean)) {
                    chain.doFilter(request, response);
                } else {
                    if (paginasUsuarioNormal(urlStr, loginBean)) {
                        chain.doFilter(request, response);
                    } else {
                        if (paginasSecretaria(urlStr, loginBean)) {
                            chain.doFilter(request, response);
                        } else {
                            if (redireccionarAdmin(urlStr, loginBean)) {
                                res.sendRedirect(req.getContextPath() + "/faces/Administrador/bienvenida/inicio.xhtml");
                            } else {
                                if (redireccionarUsuarioNormal(urlStr, loginBean)) {
                                    res.sendRedirect(req.getContextPath() + "/faces/UsuarioNormal/bienvenida/inicio.xhtml");
                                } else {
                                    if (redireccionarSecretaria(urlStr, loginBean)) {
                                        res.sendRedirect(req.getContextPath() + "/faces/Secretaria/bienvenida/inicio.xhtml");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Paginas del Administrador
    /*
     private Boolean paginasAdmin(String urlStr, ControladorLogin loginBean) {
     Boolean respuesta = false;
     try {
     if (urlStr.contains("administrador")
     && loginBean.getObjUsuarioLogin().getObjRol().getStrDescripcionRol().equals("Administrador")) {
     System.out.println("admin!!");
     respuesta = true;
     }
     } catch (Exception e) {
     throw e;
     }
     return respuesta;
     }

     private Boolean redireccionarAdmin(String urlStr, ControladorLogin loginBean) {
     Boolean respuesta = false;
     try {
     if (urlStr.contains("usuarionormal")
     && loginBean.getObjUsuarioLogin().getObjRol().getStrDescripcionRol().equals("Administrador")) {
     System.out.println("admin!!");
     respuesta = true;
     }
     } catch (Exception e) {
     throw e;
     }
     return respuesta;
     }

     private Boolean redireccionarSecretaria(String urlStr, ControladorLogin loginBean) {
     Boolean respuesta = false;
     try {
     if (urlStr.contains("administrador")
     && !loginBean.getObjUsuarioLogin().getObjRol().getStrDescripcionRol().equals("Administrador")) {
     System.out.println("admin!!");
     respuesta = true;
     }
     } catch (Exception e) {
     throw e;
     }
     return respuesta;
     }

     private Boolean redireccionarUsuarioNormal(String urlStr, ControladorLogin loginBean) {
     Boolean respuesta = false;
     try {
     if (urlStr.contains("administrador")
     && !loginBean.getObjUsuarioLogin().getObjRol().getStrDescripcionRol().equals("Administrador")) {
     System.out.println("admin!!");
     respuesta = true;
     }
     } catch (Exception e) {
     throw e;
     }
     return respuesta;
     }

     private Boolean paginasSecretaria(String urlStr, ControladorLogin loginBean) {
     Boolean respuesta = false;
     try {
     if (urlStr.contains("secretaria")
     && loginBean.getObjUsuarioLogin().getObjRol().getStrDescripcionRol().equals("Secretaria")
     && !loginBean.getObjUsuarioLogin().getObjRol().getStrDescripcionRol().equals("Administrador")) {
     System.out.println("secretaria!!");
     respuesta = true;
     }
     } catch (Exception e) {
     throw e;
     }
     return respuesta;
     }

     private Boolean paginasUsuarioNormal(String urlStr, ControladorLogin loginBean) {
     Boolean respuesta = false;
     try {
     if (urlStr.contains("usuarionormal")
     && !loginBean.getObjUsuarioLogin().getObjRol().getStrDescripcionRol().equals("Administrador")
     && !loginBean.getObjUsuarioLogin().getObjRol().getStrDescripcionRol().equals("Secretaria")) {
     System.out.println("usuario normal!!");
     respuesta = true;
     }
     } catch (Exception e) {
     throw e;
     }
     return respuesta;
     }

     */
    private Boolean redireccionarAdmin(String urlStr, ControladorUserLogin loginBean) {
        Boolean respuesta = false;
        try {
            if (urlStr.contains("usuarionormal") && loginBean.getRolCarrera().getNombreRol().equals("Administrador")) {
                System.out.println("admin!!");
                respuesta = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return respuesta;
    }

    private Boolean paginasAdmin(String urlStr, ControladorUserLogin loginBean) {
        Boolean respuesta = false;
        try {
            if (urlStr.contains("administrador") && loginBean.getRolCarrera().getNombreRol().equals("Administrador")) {
                System.out.println("admin!!");
                respuesta = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return respuesta;
    }

    private Boolean redireccionarUsuarioNormal(String urlStr, ControladorUserLogin loginBean) {
        Boolean respuesta = false;
        try {
            if (urlStr.contains("administrador") && !loginBean.getRolCarrera().getNombreRol().equals("Administrador")) {
                System.out.println("admin!!");
                respuesta = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return respuesta;
    }

    private Boolean paginasUsuarioNormal(String urlStr, ControladorUserLogin loginBean) {
        Boolean respuesta = false;
        try {
            if (urlStr.contains("usuarionormal") || loginBean.getRolCarrera().getNombreRol().equals("Administrador")
                    || loginBean.getRolCarrera().getNombreRol().equals("Secretaria")) {
                System.out.println("usuario normal!!");
                respuesta = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return respuesta;
    }

    private Boolean redireccionarSecretaria(String urlStr, ControladorUserLogin loginBean) {
        Boolean respuesta = false;
        try {
            if (urlStr.contains("secretaria") && loginBean.getRolCarrera().getNombreRol().equals("Administrador")) {
                System.out.println("secretaria!!");
                respuesta = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return respuesta;
    }

    private Boolean paginasSecretaria(String urlStr, ControladorUserLogin loginBean) {
        Boolean respuesta = false;
        try {
            if (urlStr.contains("secretaria") && loginBean.getRolCarrera().getNombreRol().equals("Secretaria")
                    || urlStr.contains("secretaria") && loginBean.getRolCarrera().getNombreRol().equals("Administrador")) {
                System.out.println("secretaria!!");
                respuesta = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return respuesta;
    }

    /*
     No Proteger
     */
    private boolean noProteger(String urlStr) {

        /*
         * Este es un buen lugar para colocar y programar todos los patrones que
         * creamos convenientes para determinar cuales de los recursos no
         * requieren protección. Sin duda que habría que crear un mecanizmo tal
         * que se obtengan de un archivo de configuración o algo que no requiera
         * compilación.
         */
        //URL's permitidas sin sesion
        Boolean protegido = false;
        String[] paginas = {"index.xhtml", "login.xhtml", "registroadmin.xhtml", "nosotros.xhtml"};
        for (String pagina : paginas) {
            if (urlStr.contains(pagina)) {
                protegido = true;
                break;
            }
        }
        return protegido;
    }

    /**
     * Return the filter configuration object for this filter.
     *
     * @return
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("LoginFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     *
     * @return
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("LoginFilter()");
        }
        StringBuilder sb = new StringBuilder("LoginFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}

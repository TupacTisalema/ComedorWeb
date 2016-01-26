/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espoch.comedor.handler;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author Tupac
 */
public class CredentialsHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        try {
            SOAPMessage msg = messageContext.getMessage();

            boolean bolMsgSalida = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            // Obtenemos el contenedor del mensaje SOAP
            SOAPPart sp = msg.getSOAPPart();
            // A partir del contendor, obtenemos el nodo "Envelope"
            SOAPEnvelope env = sp.getEnvelope();
            if (env.getHeader() != null) {
                env.getHeader().detachNode();
            }

            // Instanciamos un objeto SOAPFactory para crear cualquier elemento perteneciente a un mensaje SOAP, en nuestro caso, los nodos que formarán la cabecera
            SOAPFactory soapFactory = SOAPFactory.newInstance();

//          Definimos los elementos a incluir en el mensaje
            SOAPElement soapElementoCabecera = soapFactory.createElement("credentials", "", "http://academico.espoch.edu.ec/");

            SOAPElement soapUsername = soapFactory.createElement("username", "", "http://academico.espoch.edu.ec/");
            SOAPElement soapPassword = soapFactory.createElement("password", "", "http://academico.espoch.edu.ec/");

//          Rellenamos la información del nodo credencial
            soapUsername.addTextNode("webmail");
            soapPassword.addTextNode("webmail");

//          Incluimos los elementos dentro de los objetos correspondientes
            soapElementoCabecera.addChildElement(soapUsername);
            soapElementoCabecera.addChildElement(soapPassword);

            SOAPHeader soapHeader = env.addHeader(); // Crea un elemento cabecera SOAP

            soapHeader.addChildElement(soapElementoCabecera);
            if (bolMsgSalida) {
                try {
                    //get the soap message and envelope
                    SOAPMessage soapMsg = messageContext.getMessage();
                    //SOAPEnvelope env1 = soapMsg.getSOAPPart().getEnvelope();
                    soapMsg.getSOAPPart().getEnvelope().setPrefix("soap");
                    soapMsg.getSOAPBody().setPrefix("soap");
                    soapMsg.getSOAPHeader().setPrefix("soap");

                    soapMsg.getSOAPPart().getEnvelope().setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
                    soapMsg.getSOAPPart().getEnvelope().removeAttributeNS("http://schemas.xmlsoap.org/soap/envelope/", "S");
                    soapMsg.getSOAPPart().getEnvelope().setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
                    soapMsg.getSOAPPart().getEnvelope().removeAttributeNS("http://schemas.xmlsoap.org/soap/envelope/", "S");
                    soapMsg.getSOAPPart().getEnvelope().setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
                    soapMsg.getSOAPPart().getEnvelope().removeAttribute("xmlns:S");
                    soapMsg.getSOAPPart().getEnvelope().removeAttribute("xmlns:SOAP-ENV");
                    //env1.setPrefix("soap");
                } catch (SOAPException e) {
                }
            }
            
            msg.setProperty(SOAPMessage.WRITE_XML_DECLARATION, Boolean.TRUE.toString());
            msg.saveChanges();
        } catch (SOAPException ex) {
            Logger.getLogger(CredentialsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {

    }

}

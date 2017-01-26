/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dulcejosefina.ventadulcejosefina.server;

import com.dulcejosefina.ejb.EJBVentaSucursalService;
import com.dulcejosefina.ventadulcejosefina.reporte.Reportes;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import org.w3c.dom.Document;

/**
 *
 * @author Edgardo
 */
@WebServlet(name = "ShowReportVenta", urlPatterns = {"/ShowReportVenta"})
public class ShowReportVenta extends HttpServlet {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EJBVentaSucursalService/EJBVentaSucursal.wsdl")
    private EJBVentaSucursalService service;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control","no-cache");  //Para evitar el cache
        response.setHeader("Pragma","no-cache");
        response.setDateHeader("Expires", 0);        
        response.setContentType("application/pdf");
        ServletOutputStream servletOutputStream = null;
        String xml;        
        String nroVenta;
        Long idVenta;
        Reportes reporte = new Reportes();
        nroVenta=request.getParameter("nroVenta");
        idVenta=Long.valueOf(nroVenta);
        
        try {
            
      
        
            com.dulcejosefina.ejb.EJBVentaSucursal port = service.getEJBVentaSucursalPort();
       
       
            xml = port.selectUnaVenta(idVenta);
            
            
            servletOutputStream = response.getOutputStream();
            Document documento = reporte.obtenerDocumentoParseado(xml);
            byte[] bytes = reporte.obtenerReporteJasper(documento, "/Lista/venta",reporte.obtenerVenta());
                                 response.setContentType("application/pdf");
                                 response.setContentLength(bytes.length);
                                 servletOutputStream.write(bytes, 0, bytes.length);
                                 servletOutputStream.flush();
     
        } catch (Exception e) {
        }
           

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

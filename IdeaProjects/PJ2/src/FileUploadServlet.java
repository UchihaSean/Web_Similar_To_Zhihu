/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sun.misc.BASE64Decoder;

public class FileUploadServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        boolean success = false;
        try {
            /* TODO output your page here. You may use following sample code. */
            String image = request.getParameter("image");
            System.out.println(image);

            /*
            base64编码开头是data:image/***;base64,然后是内容，中间用逗号隔开
            */
            String header_jpeg = "data:image/jpeg;base64,";
            String header_png="data:image/png;base64,";
            if (image.indexOf(header_jpeg) != 0 && image.indexOf(header_png) != 0) {
                response.getWriter().print(wrapJSON(false));
                return;
            }
            if(image.indexOf(header_jpeg)==0)
                image = image.substring(header_jpeg.length());
            else if(image.indexOf(header_png)==0)
                image = image.substring(header_png.length());

            BASE64Decoder decoder = new BASE64Decoder();

           
            
            byte[] decodedBytes = decoder.decodeBuffer(image);
            
            String imgFilePath =Paths.get(this.getServletConfig().getServletContext().getRealPath("/image/"),UUID.randomUUID().toString()).toString();
            System.out.println(imgFilePath);
            FileOutputStream out = new FileOutputStream(imgFilePath);
            out.write(decodedBytes);
            out.close();
            success = true;

        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        response.getWriter().print(wrapJSON(success));
    }

    private String wrapJSON(boolean success) {
        return "{\"success\":" + success + "}";
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

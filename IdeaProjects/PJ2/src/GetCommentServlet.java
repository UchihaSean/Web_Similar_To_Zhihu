/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetCommentServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String aid=request.getParameter("aid");
            Connection conn=SQLHelper.getConnetion();
            ResultSet rs=conn.createStatement().executeQuery("SELECT * FROM `comment_t` WHERE `aid`="+aid);
            JSONArray ret=new JSONArray();
            while(rs.next()){
                String uid=rs.getString("uid");
                String content=rs.getString("content");
                String time=rs.getString("time");
                ResultSet rs_tmp=conn.createStatement().executeQuery("SELECT * FROM `user_t` WHERE `uid`="+uid);
                rs_tmp.next();
                String username=rs_tmp.getString("username");
                String portrait=rs_tmp.getString("portrait");
                JSONObject json_comment=new JSONObject();
                json_comment.put("uid", uid);
                json_comment.put("username", username);
                json_comment.put("portrait", portrait);
                json_comment.put("time", time.substring(0,time.length()-2));
                System.out.println("content="+content);
                json_comment.put("content", content);
                ret.add(json_comment);
            }
            out.println(ret);
            conn.close();
        }catch(Exception e){
            System.err.println(e.toString());
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

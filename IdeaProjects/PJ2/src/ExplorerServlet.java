/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class ExplorerServlet extends HttpServlet {

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
        response.setHeader("Access-Control-Allow-Origin","*");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            Connection conn=SQLHelper.getConnetion();
            ResultSet rs=conn.createStatement().executeQuery("SELECT * FROM `question_t`");
            ArrayList<QAEntry> retList=new ArrayList<>();

            while(rs.next()){
                String aids=rs.getString("aids");           
                //如果没有回答
                if(aids.equals("")){
                    ResultSet rs_tmp=conn.createStatement().executeQuery("SELECT * FROM `user_t` WHERE `uid`="+rs.getString("uid"));
                    rs_tmp.next();
                    retList.add(new QAEntry(rs.getString("time"),
                            rs.getString("uid"),rs_tmp.getString("username"),
                            rs.getString("qid"),rs.getString("title"),""
                    ));
                    
                }else{
                    String _aids[]=aids.split(" ");
                    ResultSet rs_tmp=conn.createStatement().executeQuery("SELECT * FROM `answer_t` WHERE `aid`="+_aids[0]);
                    rs_tmp.next();
                    String time=rs_tmp.getString("time");
                    String answer=rs_tmp.getString("text");
                    String uid=rs_tmp.getString("uid");
                    rs_tmp=conn.createStatement().executeQuery("SELECT * FROM `user_t` WHERE `uid`="+uid);
                    rs_tmp.next();
                    String username=rs_tmp.getString("username");
                    retList.add(new QAEntry(time,
                            uid,username,
                            rs.getString("qid"),rs.getString("title"),answer
                    ));
                }
            }
            Collections.sort(retList, (QAEntry qae1, QAEntry qae2) -> qae2.time.compareTo(qae1.time));
            JSONArray ret = new JSONArray();
            retList.stream().forEach((qae) -> {
                ret.add(qae.toJSON());
            });
            out.println(ret);
            conn.close();
            
        }catch(Exception e){
            System.err.println(e.toString());
        }
    }
    
    class QAEntry {

        String time;
        String qid;
        String uid;
        String username;
        String question;
        String answer;

        public QAEntry(String time, String uid, String username,String qid,  String question, String answer) {
            this.time = time.substring(0,time.length()-2);
            this.uid = uid;
            this.username = username;
            this.qid = qid;
            this.question = question;
            this.answer = answer;
        }
        
        public JSONObject toJSON(){
            JSONObject jsono=new JSONObject();
            jsono.put("time", time);
            jsono.put("uid", uid);
            jsono.put("username", username);
            jsono.put("qid", qid);
            jsono.put("question", question);
            jsono.put("answer", answer);
            return jsono;
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

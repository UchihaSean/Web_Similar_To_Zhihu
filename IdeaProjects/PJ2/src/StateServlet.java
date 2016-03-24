/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StateServlet extends HttpServlet {

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
            String uid = request.getParameter("uid");
            Connection conn = SQLHelper.getConnetion();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM `user_t` WHERE `uid` = " + uid);
            rs.next();
            String rs_f = rs.getString("followee");
            ArrayList<QAEntry> retList = new ArrayList<>();
            if (!rs_f.equals("")) {
                String[] followers = rs_f.split(" ");
                for (String follower : followers) {
                    ResultSet rs_user = conn.createStatement().executeQuery("SELECT * FROM `user_t` WHERE `uid` = " + follower);
                    rs_user.next();
                    String followed_name = rs_user.getString("username");

                    ResultSet rs_question = conn.createStatement().executeQuery("SELECT * FROM `question_t` WHERE `uid` = " + follower);
                    while (rs_question.next()) {
                        System.err.println(rs_question.getString("time"));
                        retList.add(new QAEntry(
                                rs_question.getString("time"),
                                follower, followed_name,
                                rs_question.getString("qid"), rs_question.getString("title"), ""
                        ));
                    }
                    ResultSet rs_answer = conn.createStatement().executeQuery("SELECT * FROM `answer_t` WHERE `uid` = " + follower);
                    while (rs_answer.next()) {
                        ResultSet rs_tmp = conn.createStatement().executeQuery("SELECT * FROM `question_t` WHERE `qid` = " + rs_answer.getString("qid"));
                        rs_tmp.next();
                        retList.add(new QAEntry(
                                rs_answer.getString("time"),
                                follower, followed_name,
                                rs_tmp.getString("qid"), rs_tmp.getString("title"), rs_answer.getString("text")
                        ));
                    }
                }
            }

            Collections.sort(retList, (QAEntry qae1, QAEntry qae2) -> qae2.time.compareTo(qae1.time));
            JSONArray ret = new JSONArray();
            for (QAEntry qae : retList) {
                ret.add(qae.toJSON());
            }
            out.println(ret);
            conn.close();
        } catch (Exception e) {
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

        public QAEntry(String time, String uid, String username, String qid, String question, String answer) {
            this.time = time.substring(0,time.length()-2);
            this.uid = uid;
            this.username = username;
            this.qid = qid;
            this.question = question;
            this.answer = answer;
        }

        public JSONObject toJSON() {
            JSONObject jsono = new JSONObject();
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

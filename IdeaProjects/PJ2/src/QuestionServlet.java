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


public class QuestionServlet extends HttpServlet {

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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            int PAGE_SIZE=5;
            int page = Integer.parseInt(request.getParameter("page"));
            String qid = request.getParameter("qid");
            Connection conn = SQLHelper.getConnetion();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM `question_t` WHERE `qid`=" + qid);
            rs.next();
            String title = rs.getString("title");
            String content = rs.getString("content");
            String uid = rs.getString("uid");
            ResultSet rs_tmp = conn.createStatement().executeQuery("SELECT * FROM `user_t` WHERE `uid`=" + uid);
            rs_tmp.next();

            //问题信息
            JSONObject json_question = new JSONObject();
            json_question.put("title", title);
            json_question.put("content", content);
            json_question.put("uid", uid);
            json_question.put("username", rs_tmp.getString("username"));
            json_question.put("portrait", rs_tmp.getString("portrait"));

            String aids = rs.getString("aids");
            JSONArray jsonarray_answers = new JSONArray();
            if (!aids.equals("")) {
                String _aids[] = aids.split(" ");
                int start = PAGE_SIZE * (page - 1);
                if (start < _aids.length) {
                    int stop = start + PAGE_SIZE;
                    if (stop > _aids.length) {
                        stop = _aids.length;
                    }
                    for (int i=start;i<stop;i++) {
                        String aid=_aids[i];
                        ResultSet rs_answer = conn.createStatement().executeQuery("SELECT * FROM `answer_t` WHERE `aid`=" + aid);
                        rs_answer.next();
                        String answer_id = rs_answer.getString("uid");
                        ResultSet rs_answerer = conn.createStatement().executeQuery("SELECT * FROM `user_t` WHERE `uid`=" + answer_id);
                        rs_answerer.next();
                        String time=rs_answer.getString("time");
                        JSONObject json_answer = new JSONObject();
                        json_answer.put("text", rs_answer.getString("text"));
                        json_answer.put("image", rs_answer.getString("image"));
                        json_answer.put("aid", rs_answer.getString("aid"));
                        json_answer.put("time", time.substring(0,time.length()-2));
                        json_answer.put("uid", rs_answerer.getString("uid"));
                        json_answer.put("username", rs_answerer.getString("username"));
                        json_answer.put("portrait", rs_answerer.getString("portrait"));
                        json_answer.put("introduction",rs_answerer.getString("introduction"));
                        ResultSet rs_comment=conn.createStatement().executeQuery("SELECT COUNT(*) FROM `comment_t` WHERE `aid`="+rs_answer.getString("aid"));
                        rs_comment.next();
                        json_answer.put("comment_num", rs_comment.getString(1));

                        jsonarray_answers.add(json_answer);
                    }
                }
            }

            JSONObject ret = new JSONObject();
            ret.put("question", json_question);
            ret.put("answers", jsonarray_answers);
            out.println(ret);
            conn.close();
        } catch (Exception e) {
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

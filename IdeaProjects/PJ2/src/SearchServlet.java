import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String keyword = request.getParameter("keyword");
        keyword=new String(keyword.getBytes("iso-8859-1"),"UTF-8");
        try {
            Connection conn = SQLHelper.getConnetion();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `user_t` WHERE " +
                    "username LIKE \'%" + keyword + "%\'");

            JSONObject rt = new JSONObject();

            JSONArray rt_user = new JSONArray();
            while (rs.next()){
                JSONObject obj = new JSONObject();
                obj.put("uid",rs.getString("uid"));
                obj.put("username",rs.getString("username"));
                rt_user.add(obj);
            }
            rt.put("user",rt_user);

            JSONArray rt_ques = new JSONArray();
            ResultSet rs_ques = stmt.executeQuery("SELECT * FROM `question_t` WHERE " +
                    "title LIKE \'%" + keyword + "%\'");
            while (rs_ques.next()){
                JSONObject obj = new JSONObject();
                obj.put("qid",rs_ques.getString("qid"));
                obj.put("uid",rs_ques.getString("uid"));
                obj.put("title",rs_ques.getString("title"));
                obj.put("content",rs_ques.getString("content"));
                rt_ques.add(obj);
            }
            rt.put("question",rt_ques);
            response.getWriter().print(rt);
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
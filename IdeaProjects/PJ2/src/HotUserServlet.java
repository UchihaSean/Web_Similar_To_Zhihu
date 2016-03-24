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


public class HotUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        Connection conn= null;
//        int num = Integer.parseInt(request.getParameter("num"));
        try {
            conn = SQLHelper.getConnetion();
            ResultSet rs=conn.createStatement()
                    .executeQuery("select * from `user_t` ORDER BY follow_num DESC;");
//            int cnt = num;
//            boolean continued = true;
            JSONArray rt = new JSONArray();
            while (rs.next() ){
                JSONObject obj = new JSONObject();
                String uid = rs.getString("uid");
                String intro = rs.getString("introduction");
                String portrait = rs.getString("portrait");
                String username = rs.getString("username");
                obj.put("username",username);
                obj.put("uid",uid);
                obj.put("introduction",intro);
                obj.put("portrait",portrait);
                rt.add(obj);
            }
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

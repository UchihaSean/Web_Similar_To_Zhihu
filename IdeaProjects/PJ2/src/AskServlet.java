import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AskServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            String uid = request.getParameter("uid");
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String aids = "";

            Date date = new Date();//获得系统时间
            String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
            Timestamp convertedTime = Timestamp.valueOf(nowTime);//把时间转换
            Connection conn = SQLHelper.getConnetion();
            Statement stmt = conn.createStatement();
            System.out.println("INSERT INTO question_t (uid, title,content, time, aids) " +
                    "VALUES (\'" + uid + "\',\'" + title + "\',\'" + content + "\'," + convertedTime + ",\'" + aids + "\');");

            stmt.executeUpdate("INSERT INTO question_t (uid, title,content, time, aids) " +
                    "VALUES (\'" + uid + "\',\'" + title + "\',\'" + content + "\',\'" + convertedTime + "\',\'" + aids + "\');");
             JSONObject rt = new JSONObject();
            rt.put("status","true");
            response.getWriter().print(rt);
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

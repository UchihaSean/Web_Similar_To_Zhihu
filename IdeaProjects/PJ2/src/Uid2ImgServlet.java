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

public class Uid2ImgServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String uid = request.getParameter("uid");
        try {
            Connection conn = SQLHelper.getConnetion();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM user_t WHERE uid=" + uid+";");
            if (rs.next()) {
                String img = rs.getString("portrait");
                JSONObject obj = new JSONObject();
                obj.put("portrait", img);
                obj.put("username", rs.getString("username"));
                response.getWriter().print(obj);
            }
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

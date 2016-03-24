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

public class GetInfoServlet extends HttpServlet {
    /*
    Given UID, return personal info.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String uid = request.getParameter("uid");
        String sqlCmd = "select * from `user_t` where uid=\"" + uid + "\";";
        Connection conn = null;
        try {
            conn = SQLHelper.getConnetion();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlCmd);
            if (rs.next()) {
                String email = rs.getString("email");
                String password = rs.getString("password");
                String username = rs.getString("username");
                String introduction = rs.getString("introduction");
                String img = rs.getString("portrait");
                JSONObject obj = new JSONObject();
                obj.put("email", email);
                obj.put("password", password);
                obj.put("username", username);
                obj.put("introduction", introduction);
                obj.put("portrait", img);
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

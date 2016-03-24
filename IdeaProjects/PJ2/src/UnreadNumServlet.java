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

public class UnreadNumServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String uid = request.getParameter("uid");
        try {
            Connection con = SQLHelper.getConnetion();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM `message_t` " +
                    "WHERE `unread`='1' AND `receiver_uid`=" + uid + ";");
            int cnt = 0;
            while (rs.next()) {
                cnt += 1;
            }
            JSONObject obj = new JSONObject();
            obj.put("num", cnt);
            response.getWriter().print(obj);
            con.close();

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

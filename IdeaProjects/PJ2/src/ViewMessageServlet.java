import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

public class ViewMessageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            String uid = request.getParameter("uid");
            Connection conn = SQLHelper.getConnetion();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM `message_t` " +
                    "WHERE `sender_uid`=" + uid + " OR `receiver_uid`=" + uid + ";");

            JSONArray rt = new JSONArray();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                String mid = rs.getString("mid");
                String sender_uid = rs.getString("sender_uid");
                String receiver_uid = rs.getString("receiver_uid");
                String content = rs.getString("content");
                String unread = rs.getString("unread");
                String time = rs.getString("time");
                ResultSet result = conn.createStatement().executeQuery("SELECT * FROM `user_t` " +
                        "WHERE `uid`=" + sender_uid + ";");
                if (result.next()) {
                    obj.put("sender_portrait", result.getString("portrait"));
                    obj.put("sender_username", result.getString("username"));
                }

                result = conn.createStatement().executeQuery("SELECT * FROM `user_t` " +
                        "WHERE `uid`=" + receiver_uid + ";");
                if (result.next()) {
                    obj.put("receiver_portrait", result.getString("portrait"));
                    obj.put("receiver_username", result.getString("username"));
                }
                obj.put("mid",mid);
                obj.put("sender_uid", sender_uid);
                obj.put("receiver_uid", receiver_uid);
                obj.put("content", content);
                obj.put("unread", unread);
                obj.put("time", time.substring(0,time.length()-2));
                rt.add(obj);
            }
//            if (!withMessage) {
//                JSONObject ret = new JSONObject();
//                ret.put("status", "false");
//                rt.add(ret);
//            }
            response.getWriter().print(rt);
            conn.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

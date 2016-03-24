import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FollowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        Format: follower follow followee
         */
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String follower = request.getParameter("follower_uid");
        String followee = request.getParameter("followee_uid");
        DBUtil db = new DBUtil();
        if (!db.followCheck(follower, followee)){
            if (db.follow(follower,followee)){
                JSONObject rt = new JSONObject();
                rt.put("status","true");
                response.getWriter().print(rt);
            }else {
                JSONObject rt = new JSONObject();
                rt.put("status","false");
                response.getWriter().print(rt);
            }
        }else{
            JSONObject rt = new JSONObject();
            rt.put("status","false");
            response.getWriter().print(rt);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}

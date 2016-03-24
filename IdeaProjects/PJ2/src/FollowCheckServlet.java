import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;


public class FollowCheckServlet extends HttpServlet {
    /*
    Check if A has followed B.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String follower_uid = request.getParameter("follower_uid");
        String followee_uid = request.getParameter("followee_uid");
        //Check the follower_list of follower_uid
        DBUtil db = new DBUtil();
        if (db.followCheck(follower_uid, followee_uid)){
            JSONObject obj = new JSONObject();
            obj.put("status","true");
            response.getWriter().print(obj);
        }else {
            JSONObject obj = new JSONObject();
            obj.put("status","false");
            response.getWriter().print(obj);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

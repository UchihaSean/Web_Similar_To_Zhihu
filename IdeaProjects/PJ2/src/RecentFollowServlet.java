import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecentFollowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // return
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            String uid= request.getParameter("uid");
            Connection conn = SQLHelper.getConnetion();

            System.out.println("ininininin");
            ResultSet rs= conn.createStatement().executeQuery("SELECT * FROM user_t WHERE uid="+uid);
            if (rs.next()){
                String follower = rs.getString("follower");
                String followee = rs.getString("followee");
                int follower_num = Integer.parseInt(rs.getString("follow_num"));
                int followee_num = Integer.parseInt(rs.getString("followed_num"));

                JSONObject obj = new JSONObject();

                JSONArray rt_follower = new JSONArray();
                String[] follower_split = follower.split(" ");
                ResultSet rs_f;
                for (int i = 0; i< follower_split.length; i++){
                    if (!follower_split[i].equals("")){
                        JSONObject follower_obj = new JSONObject();
                        follower_obj.put("uid",follower_split[i]);
                        rs_f=conn.createStatement().executeQuery("SELECT * FROM `user_t` WHERE uid="+follower_split[i]);
                        rs_f.next();
                        follower_obj.put("portrait", rs_f.getString("portrait"));
                        //TODO
                        rt_follower.add(follower_obj);
                    }
                }
                JSONArray rt_followee = new JSONArray();
                String[] followee_split = followee.split(" ");
                for (int i = 0; i< followee_split.length; i++){
                    if (!followee_split[i].equals("")){
                        JSONObject followee_obj = new JSONObject();
                        followee_obj.put("uid",followee_split[i]);
                        rs_f=conn.createStatement().executeQuery("SELECT * FROM `user_t` WHERE uid="+followee_split[i]);
                        rs_f.next();
                        followee_obj.put("portrait", rs_f.getString("portrait"));
                        rt_followee.add(followee_obj);
                    }
                }
                obj.put("follower",rt_follower);
                obj.put("followee",rt_followee);
                response.getWriter().print(obj);
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
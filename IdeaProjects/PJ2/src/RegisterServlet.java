import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin","*");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        DBUtil db = new DBUtil();//构建数据库对象
//        System.out.print("hello");
        String registerStat = db.registerSuccess(username, email, password);
        System.out.print(registerStat);
        if(!registerStat.equals("-1")){
            //注册成功！

            String uid = registerStat;

            JSONObject obj = new JSONObject();
            obj.put("status","true");
            obj.put("uid",uid);
//            obj.toString();
            response.getWriter().print(obj);
        }
        else{
            //注册失败
            JSONObject obj = new JSONObject();
            obj.put("status","false");
//            obj.put("uid","-1");
//            obj.toString();
            response.getWriter().print(obj);

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}

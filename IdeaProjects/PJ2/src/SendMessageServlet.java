import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SendMessageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin","*");

        String sender_uid = request.getParameter("sender_uid");
        String receiver_uid = request.getParameter("receiver_uid");
        String content = request.getParameter("content");
//        String unread = request.getParameter("unread");

        DBUtil db = new DBUtil();//构建数据库对象

        Date date = new Date();//获得系统时间.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
        Timestamp convertedTime =Timestamp.valueOf(nowTime);//把时间转换

        String sqlCommand = "INSERT INTO `message_t` (`sender_uid`, `receiver_uid`, `content`, `unread`, `time`) " +
                "VALUES (\'"+sender_uid+"', \'"+receiver_uid+"\', \'"+content+"\', \'"+"1"+"\',\' "+convertedTime+"\');";
        boolean stat = db.sendMessage(sqlCommand);

        JSONObject obj = new JSONObject();
        if (stat){
            //Success
            obj.put("status","true");
        }else {
            //Fail
            obj.put("status","false");
        }
        response.getWriter().print(obj);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request,response);
    }
}

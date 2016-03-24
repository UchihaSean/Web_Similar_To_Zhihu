import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class ChangeInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        String uid = request.getParameter("uid");
        String password = request.getParameter("password");
        String intro = request.getParameter("introduction");
        String image = request.getParameter("image");


        String imageID = "-1";
        if (!image.equals("-1")) {
            String header_jpeg = "data:image/jpeg;base64,";
            String header_png = "data:image/png;base64,";

            if (image.indexOf(header_jpeg) == 0) {
                image = image.substring(header_jpeg.length());
            } else if (image.indexOf(header_png) == 0) {
                image = image.substring(header_png.length());
            }
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decodedBytes = decoder.decodeBuffer(image);
            imageID = UUID.randomUUID().toString();
            String imgFilePath = Paths.get(this.getServletConfig().getServletContext().getRealPath("/image/"), imageID).toString();
            FileOutputStream fos = new FileOutputStream(imgFilePath);
            fos.write(decodedBytes);
            fos.close();
        }

        try {
            Connection conn = SQLHelper.getConnetion();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE `user_t` set password=\'" + password +
                    "\' where uid=" + uid);
            stmt.executeUpdate("UPDATE `user_t` set introduction=\'" + intro +
                    "\' where uid=" + uid);
            if (!image.equals("-1")) {
                stmt.executeUpdate("UPDATE `user_t` set portrait=\'" + imageID +
                        "\' where uid=" + uid);
            }
            JSONObject obj = new JSONObject();
            obj.put("status", "true");
            response.getWriter().print(obj);
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

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class DisplayImageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try{
            String pid=request.getParameter("pid");
            Connection conn=SQLHelper.getConnetion();
            String showImage = "select * from picture_t where pid="+pid+";";
            BufferedInputStream inputImage = null;
            Statement st = conn.createStatement();
            ResultSet rs= st.executeQuery(showImage);
            while(rs.next()) {
                Blob blob = (Blob)rs.getBlob("picture");
                inputImage = new BufferedInputStream(blob.getBinaryStream());
            }

            BufferedImage image = null;
            image= ImageIO.read(inputImage);
            ServletOutputStream sos = response.getOutputStream();
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(sos);
            encoder.encode(image);
            inputImage.close();
            conn.close();
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(IOException ie) {
            ie.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

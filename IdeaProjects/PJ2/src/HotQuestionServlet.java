import net.sf.json.JSONArray;
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
import java.util.*;

public class HotQuestionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            Connection conn = SQLHelper.getConnetion();
            Statement stmt = conn.createStatement();
            ResultSet qid_num = stmt.executeQuery("select MAX(qid) from answer_t;");
            int max_qid = 0;
            if (qid_num.next()){
                max_qid = Integer.parseInt(qid_num.getString("MAX(qid)"));
            }
            System.out.print("question #: "+max_qid);


            // HashMap < qid, # answers
            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (int i = 0; i<= max_qid; i++){
                map.put(i,0);
            }

            ResultSet rs = stmt.executeQuery("SELECT * FROM answer_t");
            while (rs.next()){
                int qid = Integer.parseInt(rs.getString("qid"));
                int ordinary_val = map.get(qid);
                map.put(qid,ordinary_val+1);
            }
            ArrayList<Map.Entry<Integer,Integer>> list = new ArrayList<Map.Entry<Integer,Integer>>(map.entrySet());

            Collections.sort(list, new Comparator<Object>() {
                public int compare(Object e1, Object e2) {
                    int v1 = Integer.parseInt(((Map.Entry) e1).getValue().toString());
                    int v2 = Integer.parseInt(((Map.Entry) e2).getValue().toString());
                    return v2 - v1;
                }
            });
            JSONArray rt = new JSONArray();

            for (Map.Entry<Integer, Integer> e:list){
                JSONObject obj = new JSONObject();
                int qid = e.getKey();
                ResultSet rs_qid = stmt.executeQuery("SELECT * FROM question_t WHERE qid=" + qid);
                String title = "";
                if (rs_qid.next()){
                    title = rs_qid.getString("title");
                }
                obj.put("qid",qid);
                obj.put("title",title);
                rt.add(obj);
            }
            response.getWriter().print(rt);
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

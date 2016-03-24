import java.io.File;
import java.io.FileInputStream;
import java.sql.*;

public class DBUtil {

    boolean bInited = false;
    //加载驱动

    public void initJDBC() throws ClassNotFoundException {
        //加载MYSQL JDBC驱动程序
        Class.forName("com.mysql.jdbc.Driver");
        bInited = true;
        System.out.println("Success loading Mysql Driver!");

    }

    public Connection getConnection() throws ClassNotFoundException,
            SQLException {
        if (!bInited) {
            initJDBC();
        }
        //连接URL为 jdbc:mysql//服务器地址/数据库名
        //后面的2个参数分别是登陆用户名和密码
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/note?characterEncoding=utf-8", "root", "jly941205");
        return conn;
    }


    public void StoreImage(String fileLocation) {
        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            PreparedStatement ps = null;

            File file = new File(fileLocation);
            FileInputStream fis = new FileInputStream(file);
            int id = 0;
            ps = con.prepareStatement("SELECT MAX(pid) FROM `picture_t`");
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("pid") + 1;
            }
            ps = con.prepareStatement("insert " + "into PIC values (?,?)");
            ps.setInt(1, id);
            ps.setBinaryStream(2, fis, (int) file.length());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    public void unfollow(String follower_uid, String followee_uid) {
        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs;

            //remove follower
            String sqlCmd = "SELECT * FROM user_t WHERE uid=\"" + follower_uid + "\";";
            rs = stmt.executeQuery(sqlCmd);
            if (rs.next()) {
                int num = rs.getInt("follow_num") - 1;
                String list = rs.getString("follower");
                stmt.executeUpdate("UPDATE `user_t` set follow_num =" + num + " where uid="
                        + follower_uid + ";");


                String[] uids = list.split(" ");
                boolean in = false;
                String removed_list = "";
                for (int i = 0; i < uids.length; i++) {
                    if (!uids[i].equals(followee_uid)) {
                        removed_list += uids[i] + " ";
                    }
                }
                System.out.println(removed_list);
                if (!removed_list.equals("")) {
                    removed_list = removed_list.substring(0, removed_list.length() - 1);
                }
                stmt.executeUpdate("UPDATE `user_t` set follower=\"" + removed_list + "\" " +
                        "where uid=" + follower_uid + ";");


            }

            //remove followee
            sqlCmd = "SELECT * FROM user_t WHERE uid=\"" + followee_uid + "\";";
            rs = stmt.executeQuery(sqlCmd);
            if (rs.next()) {
                int num = rs.getInt("followed_num") - 1;
                String list = rs.getString("followee");
                stmt.executeUpdate("UPDATE `user_t` set followed_num =" + num + " where uid="
                        + followee_uid + ";");

                String[] uids = list.split(" ");
                boolean in = false;
                String removed_list = "";
                for (int i = 0; i < uids.length; i++) {
                    if (!uids[i].equals(follower_uid)) {
                        removed_list += uids[i] + " ";
                    }
                }
                if (!removed_list.equals("")) {
                    removed_list = removed_list.substring(0, removed_list.length() - 1);
                }
                stmt.executeUpdate("UPDATE `user_t` set followee=\"" + removed_list + "\" " +
                        "where uid=" + followee_uid + ";");
            }
            con.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean follow(String follower_uid, String followee_uid) {
        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            String get_ordinary_followee_list = "SELECT * FROM `user_t` where `uid`=" + followee_uid + " ;";
            ResultSet rs = stmt.executeQuery(get_ordinary_followee_list);
            String ordinary_followee_list = "";
            if (rs.next()) {
                ordinary_followee_list = rs.getString("followee");
            }
            String cmd_followee_list;
            if (ordinary_followee_list.equals("")) {
                cmd_followee_list = "UPDATE user_t SET followee=\"" + follower_uid + "\" " +
                        "WHERE uid=" + followee_uid + ";";
            } else {
                cmd_followee_list = "UPDATE user_t SET followee=\""   + follower_uid +
                        " "+ ordinary_followee_list + "\" WHERE uid=" + followee_uid + ";";
            }
            stmt.executeUpdate(cmd_followee_list);


            String cmd_followee_num = "UPDATE `user_t` set followed_num = followed_num + 1 where uid="
                    + followee_uid + ";";
            stmt.execute(cmd_followee_num);


            String get_ordinary_follower_list = "SELECT * FROM `user_t` where `uid`=" + follower_uid + " ;";
            rs = stmt.executeQuery(get_ordinary_follower_list);
            String ordinary_follower_list = "";
            if (rs.next()) {
                ordinary_follower_list = rs.getString("follower");
            }
            String cmd_follower_list;
            if (ordinary_follower_list.equals("")) {
                cmd_follower_list = "UPDATE user_t SET follower=\"" + followee_uid + "\" " +
                        "WHERE uid=" + follower_uid + ";";
            } else {
                cmd_follower_list = "UPDATE user_t SET follower=\"" +followee_uid  +
                        " " + ordinary_follower_list + "\" WHERE uid=" + follower_uid + ";";
            }
            stmt.executeUpdate(cmd_follower_list);

            String cmd_follower_num = "UPDATE `user_t` set follow_num = follow_num + 1 where uid="
                    + follower_uid + ";";
            stmt.execute(cmd_follower_num);
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean followCheck(String follower_uid, String followee_uid) {
        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs;
            String sqlCmd = "SELECT * FROM user_t WHERE uid=\"" + follower_uid + "\";";
            rs = stmt.executeQuery(sqlCmd);
            if (rs.next()) {
                String list = rs.getString("follower");
                System.out.println(list);
                String[] uids = list.split(" ");

                boolean in = false;
                for (int i = 0; i < uids.length; i++) {
                    System.out.println(uids[i]);
                    if (uids[i].equals(followee_uid)) {
                        System.out.print("in!\n");
                        in = true;
                        break;
                    }
                }
                if (in) {
                    return true;
                } else {
                    return false;
                }
            }
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getHotUser(int num) {
        try {
            String command = "";
            Connection con = getConnection();
            Statement stat = con.createStatement();
            String cmd = "select * from `user_t` ORDER BY followed_num DESC;";
            ResultSet rs = stat.executeQuery(cmd);

            int cnt = num;
            boolean continued = true;
            while (rs.next()&&continued){

            }
            con.close();
//                stat.executeQuery();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";

    }

    public boolean sendMessage(String sqlCommand) {
        try {
            Connection connect = getConnection();
            Statement stmt = connect.createStatement();
            stmt.executeUpdate(sqlCommand);
            connect.close();
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String registerSuccess(String username, String email, String password) {

        boolean rt = false;
        String sqlCommand = "SELECT * FROM user_t";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                String emailInDB = rs.getString("email");
                String passwordInDB = rs.getString("password");
                String usernameInDB = rs.getString("username");
                if (emailInDB.equals(email) ||
                        usernameInDB.equals(username)) {
                    return "-1";
                }
            }
            //无重复，可以注册
            conn = getConnection();
            stmt = conn.createStatement();
            System.out.print("INSERT INTO user_t ( email, password, username,introduction) VALUES " +
                    "(\"" + email + "\",\"" + password + "\",\"" + username + "\",\"" + " \" );");
            String registerCommand = "INSERT INTO user_t (email, password, username,introduction,follower, followee, portrait) VALUES " +
                    "(\"" + email + "\",\"" + password + "\",\"" + username + "\",\"\",\"\", \"\",\"-1\");";
            stmt.executeUpdate(registerCommand);
            System.out.print("SELECT * FROM user_t WHERE email LIKE" + email + ";");
            String getuid = "SELECT * FROM user_t WHERE email LIKE " + "\"" + email + "\"" + ";";

            Connection con = getConnection();
            Statement st = con.createStatement();
            ResultSet r = st.executeQuery(getuid);
//            return r.getString("uid");

            if (r.next()) {
                String uid = r.getString("uid");
                con.close();
                conn.close();
                return uid;
            }
            con.close();
            conn.close();
//            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "-100";
    }

    public int loginSuccess(String email, String password) {
        int returnValue = -1;
        String sql = "SELECT * FROM user_t";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String emailInDB = rs.getString("email");
                String passwordInDB = rs.getString("password");
//                System.out.println(emailInDB);
//                System.out.println(emailInDB.equals(email) );
//                System.out.println(passwordInDB.equals(password));
                if (emailInDB.equals(email) && passwordInDB.equals(password)) {
//                    System.out.println(emailInDB+passwordInDB);
                    returnValue = rs.getInt("uid");
                    break;
                }
            }
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnValue;

    }
}

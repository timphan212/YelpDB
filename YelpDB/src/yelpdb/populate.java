package yelpdb;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 *
 * @author Timothy
 */
public class populate {
    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Error loading driver: " + cnfe);
        }
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/orclg", "scott", "tiger");
        System.out.println("Deleting previous data...");
        deleteData(conn);
        System.out.println("Delete completed.");
        System.out.println("Starting insertions...");
        parseBusinesses(conn, args[0]);
        parseUsers(conn, args[3]);
        parseReviews(conn, args[1]);
        System.out.println("Insertions completed.");
        conn.close();
    }

    private static void parseBusinesses(Connection conn, String file) throws FileNotFoundException, IOException, SQLException {
        //need to change directory and remove package to run without netbeans
        BufferedReader br = new BufferedReader(new FileReader("src/yelpdb/" + file));
        String line = br.readLine();
        
        while(line != null) {
            JsonElement ele = new JsonParser().parse(line);
            JsonObject obj = ele.getAsJsonObject();
            String bid = obj.get("business_id").getAsString();
            String addr = obj.get("full_address").getAsString();
            String available = obj.get("open").getAsString();
            String city = obj.get("city").getAsString();
            String state = obj.get("state").getAsString();
            String name = obj.get("name").getAsString();
            double rating = obj.get("stars").getAsDouble();
            JsonObject hours = obj.getAsJsonObject("hours").getAsJsonObject();
            JsonArray categories = obj.getAsJsonArray("categories");
            JsonObject attributes = obj.getAsJsonObject("attributes").getAsJsonObject();
            createBusinesses(conn, bid, addr, available, city, state, name, rating);
            createBusinessHours(conn, bid, hours);
            createBusinessCategories(conn, bid, categories);
            createBusinessAttributes(conn, bid, attributes);
            line = br.readLine();
        }
        
        br.close();
    }

    private static void createBusinesses(Connection conn, String bid, String addr, String available, 
            String city, String state, String name, double rating) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Businesses VALUES(?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, bid);
        ps.setString(2, addr);
        ps.setString(3, available);
        ps.setString(4, city);
        ps.setString(5, state);
        ps.setString(6, name);
        ps.setDouble(7, rating);
        ps.executeUpdate();
        ps.close();
    }

    private static void createBusinessHours(Connection conn, String bid, JsonObject hours) throws SQLException {
        for(Map.Entry<String, JsonElement> entry : hours.entrySet()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO BusinessHours VALUES(?, ?, ?, ?)");
            String day = entry.getKey();
            JsonObject hoursObj = entry.getValue().getAsJsonObject();
            String open = hoursObj.get("open").getAsString();
            String close = hoursObj.get("close").getAsString();
            ps.setString(1, bid);
            ps.setString(2, day);
            ps.setString(3, open);
            ps.setString(4, close);
            ps.executeUpdate();
            ps.close();
        }
    }

    private static void createBusinessCategories(Connection conn, String bid, JsonArray categories) throws SQLException {
        for(int i = 0; i < categories.size(); i++) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO BusinessCategory VALUES(?, ?)");
            ps.setString(1, bid);
            ps.setString(2, categories.get(i).getAsString());
            ps.executeUpdate();
            ps.close();
        }
    }

    private static void createBusinessAttributes(Connection conn, String bid, JsonObject attributes) throws SQLException {
        for(Map.Entry<String, JsonElement> entry: attributes.entrySet()) {
            String attribute = entry.getKey();
            
            if(entry.getValue().isJsonObject()) {
                JsonElement ele = entry.getValue();
                JsonObject obj = ele.getAsJsonObject();
                
                for(Map.Entry<String, JsonElement> nestedEntry: obj.entrySet()) {
                    String nestedAttribute = attribute + " " + nestedEntry.getKey();
                    String bool = nestedEntry.getValue().getAsString();
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO BusinessAttributes VALUES(?, ?, ?)");
                    ps.setString(1, bid);
                    ps.setString(2, nestedAttribute);
                    ps.setString(3, bool);
                    ps.executeUpdate();
                    ps.close();
                }
            }
            else {
                String bool = entry.getValue().getAsString();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO BusinessAttributes VALUES(?, ?, ?)");
                ps.setString(1, bid);
                ps.setString(2, attribute);
                ps.setString(3, bool);
                ps.executeUpdate();
                ps.close();
            }
            
        }
    }

    private static void parseUsers(Connection conn, String file) throws FileNotFoundException, IOException, SQLException {
        BufferedReader br = new BufferedReader(new FileReader("src/yelpdb/" + file));
        String line = br.readLine();
        
        while(line != null) {
            JsonElement ele = new JsonParser().parse(line);
            JsonObject obj = ele.getAsJsonObject();
            String userid = obj.get("user_id").getAsString();
            String name = obj.get("name").getAsString();
            createUsers(conn, userid, name);
            line = br.readLine();            
        }
        
        br.close();
    }

    private static void createUsers(Connection conn, String userid, String name) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO YelpUser VALUES(?, ?)");
        ps.setString(1, userid);
        ps.setString(2, name);
        ps.executeUpdate();
        ps.close();
    }

    private static void parseReviews(Connection conn, String file) throws FileNotFoundException, IOException, SQLException {
        BufferedReader br = new BufferedReader(new FileReader("src/yelpdb/" + file));
        String line = br.readLine();
        
        while(line != null) {
            JsonElement ele = new JsonParser().parse(line);
            JsonObject obj = ele.getAsJsonObject();
            String reviewid = obj.get("review_id").getAsString();
            int rating = obj.get("stars").getAsInt();
            String userid = obj.get("user_id").getAsString();
            String bid = obj.get("business_id").getAsString();
            String date = obj.get("date").getAsString();
            String text = obj.get("text").getAsString();
            JsonObject votes = obj.get("votes").getAsJsonObject();
            int fvotes = votes.get("funny").getAsInt();
            int uvotes = votes.get("useful").getAsInt();
            int cvotes = votes.get("cool").getAsInt();
            
            if(text.length() > 3000) {
                text = text.substring(0, 2999);
            }
            
            createReviews(conn, reviewid, rating, userid, bid, date, text, fvotes, uvotes, cvotes);
            line = br.readLine();
        }
        
        br.close();
    }

    private static void createReviews(Connection conn, String reviewid, int rating, String userid, String bid, String date,
            String text, int fvotes, int uvotes, int cvotes) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Reviews VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, reviewid);
        ps.setInt(2, rating);
        ps.setString(3, userid);
        ps.setString(4, bid);
        ps.setDate(5, java.sql.Date.valueOf(date));
        ps.setInt(6, fvotes);
        ps.setInt(7, uvotes);
        ps.setInt(8, cvotes);
        ps.setString(9, text);
        ps.executeUpdate();
        ps.close();
    }
    
    private static void deleteData(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "DELETE FROM Businesses";
        stmt.executeUpdate(sql);
        sql = "DELETE FROM BusinessHours";
        stmt.executeUpdate(sql);
        sql = "DELETE FROM BusinessCategory";
        stmt.executeUpdate(sql);
        sql = "DELETE FROM BusinessAttributes";
        stmt.executeUpdate(sql);
        sql = "DELETE FROM YelpUser";
        stmt.executeUpdate(sql);
        sql = "DELETE FROM Reviews";
        stmt.executeUpdate(sql);
        stmt.close();
    }
}

package Dashboard_1.Database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class connect {
    public static void main(String[] args) {
        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ruthless_raccoons", "root", "password_123");
            Statement statement = connection.createStatement();
            // CREATING SIGNUP TABLE

            String sql= "CREATE TABLE signup_tbl(unique_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255), " +
                    "gmail VARCHAR(255),"+
                    "phone_no VARCHAR(255),"+
                    "birth_date VARCHAR(255)," +
                    "gender CHAR(10),"+
                    "password VARCHAR(255))";

            /*String values= "INSERT INTO signup_tbl(name,gmail,phone_no,birth_date,gender,password) " +
                    "VALUES ('shyam'," +
                    "'shyam@gmail.com'," +
                    "'1234567890'," +
                    "'2021-03-08'," +
                    "'male'," +
                    "'shyam_123')";

             */

            String values= "INSERT INTO signup_tbl(name,gmail,phone_no,birth_date,gender,password) " +
                    "VALUES ('ram'," +
                    "'ram@gmail.com'," +
                    "'1234567890'," +
                    "'2021-03-08'," +
                    "'male'," +
                    "'ram')";


            // ADDING ONE MORE TABLE TO THE SIGNUP TABLE

            //String delete= "DROP TABLE signup_tbl";

            //String unique_id = "ALTER TABLE signup_tbl ADD unique_id INTEGER AUTO_INCREMENT PRIMARY KEY  ";

            //String delete= "DROP COLUMN unique_id";




            statement.executeUpdate(values);








        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


    

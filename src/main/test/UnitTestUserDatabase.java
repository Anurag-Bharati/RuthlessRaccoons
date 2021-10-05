package main.test;

import main.java.db.DatabaseManager;
import main.java.registration.User;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * <h2>Unit testing on User Details</h2>
 */
public class UnitTestUserDatabase {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    DatabaseManager databaseManager = new DatabaseManager();
    User user;

    @Before
    public void init() throws SQLException {

        String GMAIL = "anuragbharati26@gmail.com";

        connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement("Select * From CustomerDetail where gmail = ?");
        preparedStatement.setString(1,GMAIL);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            user = new User();
            user.setName(resultSet.getString("name"));
            user.setGmail(resultSet.getString("gmail"));
            user.setPhone(resultSet.getString("phone"));
            user.setDob((resultSet.getDate("dob").toLocalDate()));
            user.setGender(resultSet.getString("gender"));
            user.setPassword(resultSet.getString("pass"));
        }
        resultSet.close();
        preparedStatement.close();
        connection.close();

    }

    @Test
    public void isUserExist(){

        // Test
        assertNotNull(user);
    }
    @Test
    public void doesUserCredentialMatch(){

        // Given
        String pass = "abcd1234";

        // Test
        assertEquals(pass,user.getPassword());
    }
    @Test
    public void isUserDetailsRight(){

        // Given
        String name = "Anurag";
        String gmail = "anuragbharati26@gmail.com";
        String gender = "Male";
        String phone = "0123456789";
        String dob = "2021-10-07";

        // Tests
        assertEquals(name,user.getName());
        assertEquals(gmail,user.getGmail());
        assertEquals(gender,user.getGender());
        assertEquals(phone,user.getPhone());
        assertEquals(dob,user.getDob().toString());
    }
}

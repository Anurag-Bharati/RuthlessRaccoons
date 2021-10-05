package main.test;

import main.java.db.DatabaseManager;
import main.java.registration.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <h2>Unit testing on CustomerDetail table</h2>
 */
public class UnitTestDatabaseUserBooking {

    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    DatabaseManager databaseManager = new DatabaseManager();
    User user;
    int USER_ID;

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
            USER_ID = resultSet.getInt("CID");
        }
    }


    @Test
    public void isUserExist(){
        assertNotNull(user);
    }
    @Test
    public void hasUserBookedRoom() throws SQLException {
        assertTrue(fetchUserBooking());
    }
    @Test
    public void hasUserCanceledBooking() throws SQLException {
       assertTrue(fetchCanceledUserBooking());
    }
    private boolean fetchUserBooking() throws SQLException {
        Connection connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement("Select * From myBookings where CID = ? AND status = ?");
        preparedStatement.setInt(1,USER_ID);
        preparedStatement.setString(2,"BOOKED");
        resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
    private boolean fetchCanceledUserBooking() throws SQLException {
        Connection connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement("Select * From myBookings where CID = ? AND status = ?");
        preparedStatement.setInt(1,USER_ID);
        preparedStatement.setString(2,"PURGED");
        resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    @After
    public void purgeConnection() throws SQLException {
        if (resultSet!=null){resultSet.close();}
        if (preparedStatement!=null){preparedStatement.close();}
        if (connection!=null){connection.close();}
        databaseManager.disconnect();
    }
}

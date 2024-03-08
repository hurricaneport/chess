package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.sql.DBUserDAO;
import dataAccess.util.PasswordUtils;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserDAOTests {
    UserDAO userDAO = DBUserDAO.getUserDAO();
    @BeforeEach
    public void clearDatabase() throws Exception {
        userDAO.clear();
    }

    @Test
    @DisplayName("Add user")
    public void createUser() throws Exception {
        userDAO.addUser(new UserData("username", "password", "email"));

        UserData userData = userDAO.getUser("username");
        Assertions.assertEquals("username", userData.username());
        Assertions.assertEquals("email", userData.email());
        Assertions.assertTrue(PasswordUtils.checkPassword("password", userData.password()));

    }

    @Test
    @DisplayName("Create duplicate user")
    public void createDuplicateUser() throws Exception {
        userDAO.addUser(new UserData("username", "password", "email"));

        Assertions.assertThrows(DataAccessException.class, () -> userDAO.addUser(new UserData("username", "password2", "email2")));
    }

    @Test
    @DisplayName("Get existing user")
    public void getExistingUser() throws Exception {
        userDAO.addUser(new UserData("username", "password", "email"));

        UserData userData = userDAO.getUser("username");
        Assertions.assertEquals("username", userData.username());
        Assertions.assertEquals("email", userData.email());
        Assertions.assertTrue(PasswordUtils.checkPassword("password", userData.password()));
    }

    @Test
    @DisplayName("Get non-existing user")
    public void getNonExistingUser() throws Exception {
        userDAO.addUser(new UserData("username", "password", "email"));

        UserData userData = userDAO.getUser("username2");

        Assertions.assertNull(userData);
    }

    @Test
    @DisplayName("Clear database")
    public void clearDatabaseTest() throws Exception{
        userDAO.addUser(new UserData("username", "password", "email"));
        userDAO.clear();

        Assertions.assertTrue(userDAO.isEmpty());
    }
}

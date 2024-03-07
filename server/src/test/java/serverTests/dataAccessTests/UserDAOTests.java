package serverTests.dataAccessTests;

import dataAccess.UserDAO;
import dataAccess.sql.DBUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperties;

public class UserDAOTests {
    UserDAO userDAO = DBUserDAO.getUserDao();
    @BeforeEach
    public void clearDatabase() throws Exception {
        userDAO.clear();
    }

    @Test
    @DisplayName("Add user")
    public void createUser() throws Exception {
        userDAO.addUser(new UserData("username", "password", "email"));

    }
}

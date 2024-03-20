package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.sql.DBAuthDAO;
import dataAccess.sql.DBUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthDAOTests {
	AuthDAO authDAO = DBAuthDAO.getAuthDAO();
	UserDAO userDAO = DBUserDAO.getUserDAO();

	@BeforeEach
	public void clearDatabase() throws Exception {
		authDAO.clear();
		userDAO.clear();
	}

	@Test
	@DisplayName("Create auth for existing user")
	public void createAuth() throws Exception {
		userDAO.addUser(new UserData("username", "password", "email"));
		authDAO.addAuthData(new AuthData("striegiosefnieos", "username"));

		Assertions.assertNotNull(authDAO.getAuthData("striegiosefnieos"));

	}

	@Test
	@DisplayName("Create auth for non-existing user")
	public void createAuthForNonExistingUser() throws Exception {
		userDAO.addUser(new UserData("username", "password", "email"));
		Assertions.assertThrows(DataAccessException.class, () -> authDAO.addAuthData(new AuthData("fejsiofejs", "username2")));

	}

	@Test
	@DisplayName("Get auth data")
	public void getAuthData() throws Exception {
		userDAO.addUser(new UserData("username", "password", "email"));
		authDAO.addAuthData(new AuthData("striegiosefnieos", "username"));

		Assertions.assertNotNull(authDAO.getAuthData("striegiosefnieos"));

	}

	@Test
	@DisplayName("Get non-existent auth data")
	public void getNonExistentAuthData() throws Exception {
		AuthData authData = authDAO.getAuthData("feisof");
		Assertions.assertNull(authData);
	}

	@Test
	@DisplayName("Delete auth data")
	public void deleteAuthData() throws Exception {
		userDAO.addUser(new UserData("username", "password", "email"));
		authDAO.addAuthData(new AuthData("fesofo", "username"));

		authDAO.deleteAuthData(new AuthData("fesofo", "username"));

		Assertions.assertTrue(authDAO.isEmpty());
	}

	@Test
	@DisplayName("Delete non-existent auth")
	public void deleteNonExistentAuth() throws Exception {
		userDAO.addUser(new UserData("username", "password", "email"));
		authDAO.addAuthData(new AuthData("fesofo", "username"));

		authDAO.deleteAuthData(new AuthData("fesfjeos", "username"));

		Assertions.assertFalse(authDAO.isEmpty());
	}

	@Test
	@DisplayName("clear database")
	public void clearAuthDatabase() throws Exception {
		userDAO.addUser(new UserData("username", "password", "email"));
		authDAO.addAuthData(new AuthData("fesofo", "username"));

		authDAO.clear();

		Assertions.assertTrue(authDAO.isEmpty());
	}
}

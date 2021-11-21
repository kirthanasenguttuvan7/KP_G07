package models;

import org.junit.Test;
import org.junit.*;
import static org.junit.Assert.assertEquals;

import model.UserProfileModel;

public class UserProfileModelTest {

	@Test
	public void keywordTest() {
		UserProfileModel user = new UserProfileModel();
		user.setLogin("Sayali2608");
		assertEquals(user.getLogin(), "Sayali2608");
	}
}

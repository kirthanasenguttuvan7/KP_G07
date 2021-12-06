package models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.LicenseModel;
import model.TopicsModel;

public class LicenseModelTest {
	@Test
	public void licenseTest() {
		LicenseModel topic = new LicenseModel();
		topic.setKey("test");
		assertEquals(topic.getKey(), "test");
	}
}

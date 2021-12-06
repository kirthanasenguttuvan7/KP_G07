package models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.TopicsModel;

public class TopicsModelTest {
	@Test
	public void topicTest() {
		TopicsModel topic = new TopicsModel();
		topic.setTopics(null);
		assertEquals(topic.getTopics(), null);
	}
}

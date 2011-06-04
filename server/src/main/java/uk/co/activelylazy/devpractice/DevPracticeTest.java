package uk.co.activelylazy.devpractice;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface DevPracticeTest {

	public void executeFor(DevPracticeClient client) throws ClientProtocolException, IOException;
	
}

package uk.co.activelylazy.devpractice.tasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import uk.co.activelylazy.devpractice.DevPracticeClient;

public interface DevPracticeTask {

	public boolean executeFor(DevPracticeClient client, String text) throws ClientProtocolException, IOException;
	
}

package uk.co.activelylazy.devpractice;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface DevPracticeTask {

	public boolean executeFor(DevPracticeClient client, String text) throws ClientProtocolException, IOException;
	
}

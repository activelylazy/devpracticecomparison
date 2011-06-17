package uk.co.activelylazy.devpractice.tasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import uk.co.activelylazy.devpractice.DevPracticeClient;

public class SayHelloWorldTask implements DevPracticeTask {

	@Override
	public boolean executeFor(DevPracticeClient client, String text) throws ClientProtocolException, IOException {
		String response = client.makeRequest("/SayHelloWorld");
		if (response.trim().equalsIgnoreCase("Hello world")) {
			client.sendStatus("pass");
			return true;
		} else {
			client.sendStatus("fail");
			return false;
		}
	}

}

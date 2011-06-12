package uk.co.activelylazy.devpractice;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

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

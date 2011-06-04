package uk.co.activelylazy.devpractice;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public class SayHelloWorldTest implements DevPracticeTest {

	@Override
	public void executeFor(DevPracticeClient client) throws ClientProtocolException, IOException {
		String response = client.makeRequest("/SayHelloWorld");
		if (response.trim().equalsIgnoreCase("Hello world")) {
			client.sendStatus("pass");
		} else {
			client.sendStatus("fail");
		}
	}

}

package uk.co.activelylazy.devpractice;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public class EchoContentBackTask implements DevPracticeTask {

	@Override
	public void executeFor(DevPracticeClient client, String text) throws ClientProtocolException, IOException {
		String response = client.makePOSTRequest("/Echo","Echo this text back");
		if (response.trim().equalsIgnoreCase("Echo this text back")) {
			client.sendStatus("pass");
		} else {
			client.sendStatus("fail");
		}
	}

}

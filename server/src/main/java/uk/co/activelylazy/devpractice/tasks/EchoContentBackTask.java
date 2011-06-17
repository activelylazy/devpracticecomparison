package uk.co.activelylazy.devpractice.tasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import uk.co.activelylazy.devpractice.DevPracticeClient;

public class EchoContentBackTask implements DevPracticeTask {

	@Override
	public boolean executeFor(DevPracticeClient client, String text) throws ClientProtocolException, IOException {
		String response = client.makePOSTRequest("/Echo","Echo this text back");
		if (response.trim().equalsIgnoreCase("Echo this text back")) {
			client.sendStatus("pass");
			return true;
		} else {
			client.sendStatus("fail");
			return false;
		}
	}

}

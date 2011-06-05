package uk.co.activelylazy.devpractice;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public class CountWordsTask implements DevPracticeTask {

	@Override
	public void executeFor(DevPracticeClient client, String text) throws ClientProtocolException, IOException {
		String response = client.makePOSTRequest("/CountWords",text);
		int answer = Integer.parseInt(response.trim());
		int expected = countWords(text);
		if (answer == expected) {
			client.sendStatus("pass");
		} else {
			client.sendStatus("fail - expected "+expected+"; you sent "+answer);
		}
	}

	int countWords(String text) {
		int count = 1;
		if (text.trim().length() == 0) {
			return 0;
		}
		boolean lastWasWhitespace = false;
		for (int i=0; i<text.length(); i++) {
			if (Character.isWhitespace(text.charAt(i))) {
				if (!lastWasWhitespace) {
					count++;
				}
				lastWasWhitespace = true;
			} else if (text.charAt(i) == '-') {
				// Ignore
			} else {
				lastWasWhitespace = false;
			}
		}
		return count;
	}
}

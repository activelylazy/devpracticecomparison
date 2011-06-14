package uk.co.activelylazy.devpractice;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ClientMatcher extends TypeSafeDiagnosingMatcher<StubClient> {
	private String status;
	private String url;

	public static ClientMatcher that_the_server() {
		return new ClientMatcher();
	}
	
	public ClientMatcher after_requesting_url(String url) {
		this.url = url;
		return this;
	}

	public ClientMatcher sent_status(String status) {
		this.status = status;
		return this;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("the server to respond with status "+status+" after requesting url "+url);
	}

	@Override
	protected boolean matchesSafely(StubClient client, Description mismatchDescription) {
		mismatchDescription.appendText("the server");
		boolean matches = true;
		if (!client.getLastRequestedURL().equals(url)) {
			mismatchDescription.appendText(" requested url "+client.getLastRequestedURL());
			matches = false;
		}
		if (!client.getStatus().equals(status)) {
			mismatchDescription.appendText(" responded with status "+client.getStatus());
			matches = false;
		}
		return matches;
	}
	
}
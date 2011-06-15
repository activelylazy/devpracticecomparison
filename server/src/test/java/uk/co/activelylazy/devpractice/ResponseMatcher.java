package uk.co.activelylazy.devpractice;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ResponseMatcher extends TypeSafeDiagnosingMatcher<Response> {

	private final String contentType;
	private Matcher<? super String> content;

	private ResponseMatcher(String contentType) {
		this.contentType = contentType;
	}
	
	public ResponseMatcher with_content(Matcher<? super String> content) {
		this.content = content;
		return this;
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendText("a response of type "+contentType);
		if (content != null) {
			description.appendText(" with content ");
			content.describeTo(description);
		}
	}

	@Override
	protected boolean matchesSafely(Response response, Description mismatchDescription) {
		boolean matches = true;
		
		if (! response.getContentType().equals(contentType)) {
			matches = false;
			mismatchDescription.appendText("expected content type "+contentType+" but got "+response.getContentType());
		}
		
		if (content != null && !content.matches(response.getContent())) {
			matches = false;
			mismatchDescription.appendText("expected content ");
			content.describeTo(mismatchDescription);
			mismatchDescription.appendText(" but got "+response.getContent());
		}
		
		return matches;
	}
	
	public static ResponseMatcher plain_text() {
		return new ResponseMatcher("text/plain");
	}

	public static ResponseMatcher json() {
		return new ResponseMatcher("text/json");
	}
}

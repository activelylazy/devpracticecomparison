package uk.co.activelylazy.devpractice;

import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ResponseMatcher extends TypeSafeDiagnosingMatcher<Response> {

	private final String contentType;
	private Matcher<? super String> content;
	private Integer status;
	private Map<String, Matcher<? super String>> headers = new HashMap<String, Matcher<? super String>>();

	private ResponseMatcher(String contentType) {
		this.contentType = contentType;
	}
	
	public ResponseMatcher with_content(Matcher<? super String> content) {
		this.content = content;
		return this;
	}

	private ResponseMatcher with_status(int status) {
		this.status = status;
		return this;
	}
	
	public ResponseMatcher with_header(String header, Matcher<? super String> matching) {
		headers.put(header, matching);
		return this;
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendText("a ");
		if (status != null) {
			description.appendText("HTTP "+status+" ");
		}
		description.appendText("response ");
		if (contentType != null) {
			description.appendText("of type "+contentType+" ");
		}
		if (content != null) {
			description.appendText("with content ");
			content.describeTo(description);
		}
		if (!headers.isEmpty()) {
			description.appendText("with headers: {");
			for (String header : headers.keySet()) {
				description.appendText(header+": ");
				headers.get(header).describeTo(description);
				description.appendText(", ");
			}
			description.appendText("}");
		}
	}

	@Override
	protected boolean matchesSafely(Response response, Description mismatchDescription) {
		boolean matches = true;
		
		if (contentType != null && !contentType.equals(response.getContentType())) {
			matches = false;
			mismatchDescription.appendText("expected content type "+contentType+" but got "+response.getContentType());
		}
		
		if (content != null && !content.matches(response.getContent())) {
			matches = false;
			mismatchDescription.appendText("expected content ");
			content.describeTo(mismatchDescription);
			mismatchDescription.appendText(" but got "+response.getContent());
		}
		
		if (status != null && status != response.getStatus()) {
			matches = false;
			mismatchDescription.appendText("expected status "+status+" but got "+response.getStatus());
		}
		
		for (String header : headers.keySet()) {
			Matcher<? super String> expected = headers.get(header);
			final String actual = response.getHeaders().get(header);
			if (! expected.matches(actual)) {
				matches = false;
				mismatchDescription.appendText("header "+header);
				expected.describeTo(mismatchDescription);
			}
		}
		
		return matches;
	}
	
	public static ResponseMatcher plain_text() {
		return new ResponseMatcher("text/plain");
	}

	public static ResponseMatcher json() {
		return new ResponseMatcher("text/json");
	}

	public static ResponseMatcher javascript() {
		return new ResponseMatcher("text/javascript");
	}
	
	public static ResponseMatcher html() {
		return new ResponseMatcher("text/html");
	}

	public static ResponseMatcher redirect(String to) {
		return new ResponseMatcher(null).with_status(302).with_header("Location", is(to));
	}
}

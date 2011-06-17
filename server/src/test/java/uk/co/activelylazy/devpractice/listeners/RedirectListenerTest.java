package uk.co.activelylazy.devpractice.listeners;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import uk.co.activelylazy.devpractice.Response;
import uk.co.activelylazy.devpractice.ResponseMatcher;
import uk.co.activelylazy.devpractice.listeners.RedirectListener;


public class RedirectListenerTest {

	@Test public void
	responds_with_redirect() throws IOException {
		RedirectListener listener = new RedirectListener();
		
		Response response = listener.request(null);
		
		assertThat(response, is(ResponseMatcher.redirect("/index.html")));
	}
}

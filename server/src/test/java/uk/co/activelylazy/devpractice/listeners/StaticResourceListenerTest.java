package uk.co.activelylazy.devpractice.listeners;

import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import uk.co.activelylazy.devpractice.ResponseMatcher;
import uk.co.activelylazy.devpractice.listeners.StaticResourceListener;


public class StaticResourceListenerTest {

	private Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
	
	@Test public void
	returns_javascript() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		StaticResourceListener listener = new StaticResourceListener();
		
		context.checking(new Expectations() {{
			allowing(request).getRequestURI(); will(returnValue("/js/jquery.flot.js"));
		}});
		
		assertThat(listener.request(request), ResponseMatcher.javascript());
	}
	
	@Test public void
	returns_html() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		StaticResourceListener listener = new StaticResourceListener();
		
		context.checking(new Expectations() {{
			allowing(request).getRequestURI(); will(returnValue("/index.html"));
		}});
		
		assertThat(listener.request(request), ResponseMatcher.html());
	}
	
}

package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import uk.co.activelylazy.devpractice.listeners.RequestListener;


public class DevPracticeHandlerTest {

	private Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
	
	@Test public void
	handler_handles_request() throws IOException, ServletException {
		final Request baseRequest = context.mock(Request.class);
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final HttpServletResponse response = context.mock(HttpServletResponse.class);
		final RequestListener listener = context.mock(RequestListener.class);
		final PrintWriter writer = context.mock(PrintWriter.class);
		
		context.checking(new Expectations() {{
			oneOf(listener).request(request); will(returnValue(Response.plainText("OK")));
			oneOf(response).setContentType("text/plain");
			oneOf(response).setStatus(HttpServletResponse.SC_OK);
			oneOf(response).getWriter(); will(returnValue(writer));
			oneOf(writer).println("OK");
			oneOf(baseRequest).setHandled(true);
		}});
		
		DevPracticeHandler handler = new DevPracticeHandler(null, null, null, null, null, null);
		handler.registerListener("/test", listener);
		handler.handle("/test", baseRequest, request, response);
		
		context.assertIsSatisfied();
	}
	
	@Test public void
	handler_responds_to_unknown_path() throws IOException, ServletException {
		final Request baseRequest = context.mock(Request.class);
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final HttpServletResponse response = context.mock(HttpServletResponse.class);
		final PrintWriter writer = context.mock(PrintWriter.class);
		
		context.checking(new Expectations() {{
			oneOf(response).setContentType("text/plain");
			oneOf(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
			oneOf(response).getWriter(); will(returnValue(writer));
			oneOf(writer).println(DevPracticeHandler.NOT_FOUND.getContent());
			oneOf(baseRequest).setHandled(true);
		}});
		
		DevPracticeHandler handler = new DevPracticeHandler(null, null, null, null, null, null);
		handler.handle("/notfound", baseRequest, request, response);
		
		context.assertIsSatisfied();
	}
	
	@Test public void
	handler_responds_to_ping() throws IOException, ServletException {
		final Request baseRequest = context.mock(Request.class);
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final HttpServletResponse response = context.mock(HttpServletResponse.class);
		final RequestListener listener = context.mock(RequestListener.class);
		final PrintWriter writer = context.mock(PrintWriter.class);
		
		context.checking(new Expectations() {{
			oneOf(listener).request(request); will(returnValue(Response.plainText("Server OK")));
			oneOf(response).setContentType("text/plain");
			oneOf(response).setStatus(HttpServletResponse.SC_OK);
			oneOf(response).getWriter(); will(returnValue(writer));
			oneOf(writer).println("Server OK");
			oneOf(baseRequest).setHandled(true);
		}});
		
		DevPracticeHandler handler = new DevPracticeHandler(null, null, listener, null, null, null);
		handler.handle("/ping", baseRequest, request, response);
		
		context.assertIsSatisfied();
	}
	
	@Test public void
	handler_responds_to_javascript() throws IOException, ServletException {
		final Request baseRequest = context.mock(Request.class);
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final HttpServletResponse response = context.mock(HttpServletResponse.class);
		final RequestListener listener = context.mock(RequestListener.class);
		final PrintWriter writer = context.mock(PrintWriter.class);
		final String contentType = "text/javascript";
		final String responseText = "// nothing here";
		
		context.checking(new Expectations() {{
			oneOf(listener).request(request); 
			will(returnValue(new Response(contentType, responseText)));
			oneOf(response).setContentType(contentType);
			oneOf(response).setStatus(HttpServletResponse.SC_OK);
			oneOf(response).getWriter(); will(returnValue(writer));
			oneOf(writer).println(responseText);
			oneOf(baseRequest).setHandled(true);
		}});
		
		DevPracticeHandler handler = new DevPracticeHandler(null, listener, null, null, null, null);
		handler.handle("/js/jquery.flot.js", baseRequest, request, response);
		
		context.assertIsSatisfied();
	}
	
	@Test public void
	handler_responds_to_root_with_redirect() throws IOException, ServletException {
		final Request baseRequest = context.mock(Request.class);
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final HttpServletResponse response = context.mock(HttpServletResponse.class);
		final RequestListener listener = context.mock(RequestListener.class);
		final String location = "/somewhere";
		
		context.checking(new Expectations() {{
			oneOf(listener).request(request); will(returnValue(Response.redirect(location)));
			oneOf(response).setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			oneOf(response).setHeader("Location", location);
			oneOf(baseRequest).setHandled(true);
		}});
		
		DevPracticeHandler handler = new DevPracticeHandler(listener, null, null, null, null, null);
		handler.handle("/", baseRequest, request, response);
		
		context.assertIsSatisfied();
	}

	@Test public void
	handler_responds_to_html() throws IOException, ServletException {
		final Request baseRequest = context.mock(Request.class);
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final HttpServletResponse response = context.mock(HttpServletResponse.class);
		final RequestListener listener = context.mock(RequestListener.class);
		final PrintWriter writer = context.mock(PrintWriter.class);
		final String contentType = "text/html";
		final String responseText = "<html><body>Hello world</body></html>";
		
		context.checking(new Expectations() {{
			oneOf(listener).request(request); 
			will(returnValue(new Response(contentType, responseText)));
			oneOf(response).setContentType(contentType);
			oneOf(response).setStatus(HttpServletResponse.SC_OK);
			oneOf(response).getWriter(); will(returnValue(writer));
			oneOf(writer).println(responseText);
			oneOf(baseRequest).setHandled(true);
		}});
		
		DevPracticeHandler handler = new DevPracticeHandler(null, listener, null, null, null, null);
		handler.handle("/index.html", baseRequest, request, response);
		
		context.assertIsSatisfied();
	}

	@Test public void
	handler_responds_to_register() throws IOException, ServletException {
		final Request baseRequest = context.mock(Request.class);
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final HttpServletResponse response = context.mock(HttpServletResponse.class);
		final RequestListener listener = context.mock(RequestListener.class);
		final PrintWriter writer = context.mock(PrintWriter.class);
		
		context.checking(new Expectations() {{
			oneOf(listener).request(request); will(returnValue(Response.plainText("OK")));
			oneOf(response).setContentType("text/plain");
			oneOf(response).setStatus(HttpServletResponse.SC_OK);
			oneOf(response).getWriter(); will(returnValue(writer));
			oneOf(writer).println("OK");
			oneOf(baseRequest).setHandled(true);
		}});
		
		DevPracticeHandler handler = new DevPracticeHandler(null, null, null, listener, null, null);
		handler.handle("/register", baseRequest, request, response);
		
		context.assertIsSatisfied();
	}
	
	@Test public void
	handler_responds_to_force_test() throws IOException, ServletException {
		final Request baseRequest = context.mock(Request.class);
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final HttpServletResponse response = context.mock(HttpServletResponse.class);
		final RequestListener listener = context.mock(RequestListener.class);
		final PrintWriter writer = context.mock(PrintWriter.class);
		
		context.checking(new Expectations() {{
			oneOf(listener).request(request); will(returnValue(Response.plainText("OK")));
			oneOf(response).setContentType("text/plain");
			oneOf(response).setStatus(HttpServletResponse.SC_OK);
			oneOf(response).getWriter(); will(returnValue(writer));
			oneOf(writer).println("OK");
			oneOf(baseRequest).setHandled(true);
		}});
		
		DevPracticeHandler handler = new DevPracticeHandler(null, null, null, null, listener, null);
		handler.handle("/forceTest", baseRequest, request, response);
		
		context.assertIsSatisfied();
	}
	
	@Test public void
	handler_responds_to_scores() throws IOException, ServletException {
		final Request baseRequest = context.mock(Request.class);
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final HttpServletResponse response = context.mock(HttpServletResponse.class);
		final RequestListener listener = context.mock(RequestListener.class);
		final PrintWriter writer = context.mock(PrintWriter.class);
		final String responseText = "{\"score\":0}";
		
		context.checking(new Expectations() {{
			oneOf(listener).request(request); will(returnValue(Response.plainText(responseText)));
			oneOf(response).setContentType("text/plain");
			oneOf(response).setStatus(HttpServletResponse.SC_OK);
			oneOf(response).getWriter(); will(returnValue(writer));
			oneOf(writer).println(responseText);
			oneOf(baseRequest).setHandled(true);
		}});
		
		DevPracticeHandler handler = new DevPracticeHandler(null, null, null, null, null, listener);
		handler.handle("/scores.json", baseRequest, request, response);
		
		context.assertIsSatisfied();
	}
}

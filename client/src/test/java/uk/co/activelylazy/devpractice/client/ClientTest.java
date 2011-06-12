package uk.co.activelylazy.devpractice.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;


public class ClientTest {

	private Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
	
	@Test public void
	client_handles_request() throws ServletException, IOException {
		final RequestHandler.Factory factory = context.mock(RequestHandler.Factory.class);
		final RequestHandler requestHandler = context.mock(RequestHandler.class);
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final HttpServletResponse response = context.mock(HttpServletResponse.class);
		final String uri = "some uri";
		final Map<String, String[]> params = new HashMap<String, String[]>();
		final String responseText = "the response";
		final ServletOutputStream outputStream = context.mock(ServletOutputStream.class);
		
		context.checking(new Expectations() {{
			oneOf(factory).create(); will(returnValue(requestHandler));
			oneOf(request).getRequestURI(); will(returnValue(uri));
			oneOf(request).getParameterMap(); will(returnValue(params));
			oneOf(requestHandler).handle(uri, params); will(returnValue(responseText));
			exactly(2).of(response).getOutputStream(); will(returnValue(outputStream));
			oneOf(outputStream).flush();
			oneOf(outputStream).write(with(responseText.getBytes()));
		}});
		
		Client client = new Client();
		client.setRequestHandlerFactory(factory);
		
		client.service(request, response);
		
		context.assertIsSatisfied();
	}
	
}

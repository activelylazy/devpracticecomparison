package uk.co.activelylazy.devpractice.client;

import java.io.IOException;
import java.io.StringBufferInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;


public class ClientTest {

	private Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
	
	@Test public void
	registers_client() throws ServletException, IOException {
		final HttpClient httpClient = context.mock(HttpClient.class);
		final HttpResponse httpResponse = context.mock(HttpResponse.class);
		final HttpEntity httpEntity = context.mock(HttpEntity.class);
		final String OK = "OK";
		
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final HttpServletResponse response = context.mock(HttpServletResponse.class);

		context.checking(new Expectations() {{
			oneOf(httpClient).execute(with(any(HttpGet.class))); will(returnValue(httpResponse));
			oneOf(httpResponse).getEntity(); will(returnValue(httpEntity));
			oneOf(httpEntity).getContent(); will(returnValue(new StringBufferInputStream(OK)));
			allowing(httpEntity).getContentLength(); will(returnValue(new Integer(OK.length()).longValue()));
			allowing(httpEntity).getContentType();
			
			allowing(request).getRequestURI(); will(returnValue("/register"));
			ignoring(response);
		}});
		
		Client client = new Client();
		client.setClient(httpClient);
		
		client.service(request, response);
		
		context.assertIsSatisfied();
	}
	
}

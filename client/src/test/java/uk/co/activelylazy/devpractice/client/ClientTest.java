package uk.co.activelylazy.devpractice.client;

import java.io.IOException;
import java.io.StringBufferInputStream;

import javax.servlet.ServletException;

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
	init_registers_client() throws ServletException, IOException {
		final HttpClient httpClient = context.mock(HttpClient.class);
		final HttpResponse response = context.mock(HttpResponse.class);
		final HttpEntity entity = context.mock(HttpEntity.class);
		final String OK = "OK";

		context.checking(new Expectations() {{
			oneOf(httpClient).execute(with(any(HttpGet.class))); will(returnValue(response));
			oneOf(response).getEntity(); will(returnValue(entity));
			oneOf(entity).getContent(); will(returnValue(new StringBufferInputStream(OK)));
			allowing(entity).getContentLength(); will(returnValue(new Integer(OK.length()).longValue()));
			allowing(entity).getContentType();
		}});
		
		Client client = new Client();
		client.setClient(httpClient);
		client.init(null);
		
		context.assertIsSatisfied();
	}
}

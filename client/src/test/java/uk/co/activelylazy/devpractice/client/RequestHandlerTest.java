package uk.co.activelylazy.devpractice.client;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;


public class RequestHandlerTest {
	
	private Mockery context = new Mockery();
	
	@Test public void
	registers() throws ServletException, IOException {
		final HttpClient client = context.mock(HttpClient.class);
		final HttpResponse response = context.mock(HttpResponse.class);
		final HttpEntity entity = context.mock(HttpEntity.class);
		final String OK = "OK";
		final InputStream inputStream = new StringBufferInputStream(OK);
		
		context.checking(new Expectations() {{
			oneOf(client).execute(with(any(HttpGet.class))); will(returnValue(response));
			oneOf(response).getEntity(); will(returnValue(entity));
			oneOf(entity).getContent(); will(returnValue(inputStream));
			allowing(entity).getContentLength(); will(returnValue(new Long(OK.length())));
			allowing(entity).getContentType();
		}});
		
		RequestHandler handler = new RequestHandler();
		handler.setClient(client);
		String status = handler.handle("register", new HashMap<String, String[]>());

		assertThat(status, is(OK));
	}
	
	@Test public void
	responds_to_hello_world() throws ServletException, IOException {
		RequestHandler handler = new RequestHandler();
		String response = handler.handle("SayHelloWorld", new HashMap<String, String[]>());
		
		assertThat(response, is("Hello world"));
	}

}

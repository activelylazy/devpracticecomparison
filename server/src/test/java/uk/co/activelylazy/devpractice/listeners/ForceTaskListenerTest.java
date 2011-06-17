package uk.co.activelylazy.devpractice.listeners;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import uk.co.activelylazy.devpractice.ParticipantRegistry;
import uk.co.activelylazy.devpractice.ResponseMatcher;
import uk.co.activelylazy.devpractice.TaskRunner;
import uk.co.activelylazy.devpractice.listeners.ForceTestListener;


public class ForceTaskListenerTest {

	private Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
	
	@Test public void
	force_test_executes_specific_task() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final TaskRunner client = context.mock(TaskRunner.class);
		final String endpoint = "http://endpoint.example.com";
		ForceTestListener listener = new ForceTestListener(participants);
		
		context.checking(new Expectations() {{
			oneOf(request).getParameter("client"); will(returnValue(endpoint));
			oneOf(request).getParameter("iteration"); will(returnValue("0"));
			oneOf(request).getParameter("text"); will(returnValue(null));
			oneOf(participants).getParticipant(endpoint); will(returnValue(client));
			oneOf(client).executeTask(0, 0);
		}});
		
		assertThat(listener.request(request), ResponseMatcher.plain_text().with_content(equalTo("OK")));
		context.assertIsSatisfied();
	}
}

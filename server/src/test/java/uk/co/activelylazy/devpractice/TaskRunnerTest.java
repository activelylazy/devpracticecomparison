package uk.co.activelylazy.devpractice;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;


public class TaskRunnerTest {
	private Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
	
	@Test public void
	successful_task_increases_score() throws ClientProtocolException, IOException {
		final DevPracticeClient client = context.mock(DevPracticeClient.class);
		final DevPracticeTask task = context.mock(DevPracticeTask.class);
		
		TaskRunner runner = new TaskRunner(client);
		
		context.checking(new Expectations() {{
			allowing(task).executeFor(with(client), with(any(String.class)));
				will(returnValue(true));
		}});
		
		int initialScore = runner.getScore();
		runner.executeTask(0, task);

		assertThat(runner.getScore(), is(greaterThan(initialScore)));
	}
	
	@Test public void
	failed_task_keeps_score_same() throws IOException {
		final DevPracticeClient client = context.mock(DevPracticeClient.class);
		final DevPracticeTask task = context.mock(DevPracticeTask.class);
		
		TaskRunner runner = new TaskRunner(client);
		
		context.checking(new Expectations() {{
			allowing(task).executeFor(with(client), with(any(String.class)));
				will(returnValue(false));
		}});
		
		int initialScore = runner.getScore();
		runner.executeTask(0, task);

		assertThat(runner.getScore(), is(equalTo(initialScore)));
	}
}

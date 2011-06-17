package uk.co.activelylazy.devpractice.tasks;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import uk.co.activelylazy.devpractice.tasks.CountWordsTask;

public class CountWordsTaskTest {

	private CountWordsTask task = new CountWordsTask();
	
	@Test public void
	counts_words() {
		assertThat(task.countWords(""), is(0));
		assertThat(task.countWords("hello"), is(1));
		assertThat(task.countWords("hello world"), is(2));
		assertThat(task.countWords("hello  world"), is(2));
		assertThat(task.countWords("hello 2 a  world"), is(4));
		assertThat(task.countWords("the - dashed - words"), is(3));
		assertThat(task.countWords("but hyphenated-words don't"), is(3));
	}
	
}

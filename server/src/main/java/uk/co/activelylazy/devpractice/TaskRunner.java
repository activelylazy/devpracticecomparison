package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;

import uk.co.activelylazy.devpractice.tasks.CountWordsTask;
import uk.co.activelylazy.devpractice.tasks.DevPracticeTask;
import uk.co.activelylazy.devpractice.tasks.EchoContentBackTask;
import uk.co.activelylazy.devpractice.tasks.SayHelloWorldTask;

public class TaskRunner extends Thread {
	private static long TIME_BETWEEN_RUNS = 1000*10;
	private static Random random = new Random();
	private static String[] texts = new String[] {
			"",
			"word.",
			"A false conclusion: I hate it as an unfilled can. To be up after midnight and to go to bed then, is early: so that to go to bed after midnight is to go to bed betimes. Does not our life consist of the four elements?",
			"I did impeticos thy gratillity; for Malvolio\'s nose is no whipstock: my lady has a white hand, and the Myrmidons are no bottle-ale houses.",
			"What a caterwauling do you keep here! If my lady have not called up her steward Malvolio and bid him turn you out of doors, never trust me.",
			"My masters, are you mad? or what are you? Have ye no wit, manners, nor honesty, but to gabble like tinkers at this time of night? Do ye make an alehouse of my lady's house, that ye squeak out your coziers' catches without any mitigation or remorse of voice? Is there no respect of place, persons, nor time in you?",
			"'Twere as good a deed as to drink when a man's a-hungry, to challenge him the field, and then to break promise with him and make a fool of him.",
			"I will drop in his way some obscure epistles of love; wherein, by the colour of his beard, the shape of his leg, the manner of his gait, the expressure of his eye, forehead, and complexion, he shall find himself most feelingly personated. I can write very like my lady your niece: on a forgotten matter we can hardly make distinction of our hands.",
			"Come, come, I'll go burn some sack; 'tis too late to go to bed now: come, knight; come, knight.",
			"Not so, sir, I do care for something; but in my conscience, sir, I do not care for you: if that be to care for nothing, sir, I would it would make you invisible.",
			"The matter, I hope, is not great, sir, begging but a beggar: Cressida was a beggar. My lady is within, sir. I will construe to them whence you come; who you are and what you would are out of my welkin, I might say 'element,' but the word is over-worn.",
			"I am invariably late for appointments - sometimes as much as two hours. I've tried to change my ways but the things that make me late are too strong, and too pleasing."
		};
	private volatile boolean close = false;
	private List<DevPracticeTask> tasks = new ArrayList<DevPracticeTask>();
	private DevPracticeClient client;
	private int score;
	private String groupName;

	public TaskRunner(DevPracticeClient client, String groupName) {
		this.client = client;
		this.groupName = groupName;
		tasks.add(new SayHelloWorldTask());
		tasks.add(new EchoContentBackTask());
		tasks.add(new CountWordsTask());
	}
	
	public int getScore() { return this.score; }
	public String getEndpoint() { return client.getEndpoint(); }
	public String getGroupName() { return this.groupName; }
	public boolean executeTask(int iteration, int text) throws ClientProtocolException, IOException {
		return executeTask(text, tasks.get(iteration));
	}

	public void update(String groupName) {
		this.groupName = groupName;
	}

	boolean executeTask(int text, final DevPracticeTask task) throws ClientProtocolException, IOException {
		boolean pass = task.executeFor(client,texts[text]);
		if (pass) {
			this.score++;
		}
		return pass;
	}
	
	public synchronized void close() {
		this.close = true;
	}

	@Override
	public void run() {
		while (!close) {
			long startTime = System.currentTimeMillis();
			for (DevPracticeTask task : tasks) {
				try {
					if (!executeTask(random.nextInt(texts.length), task)) {
						break;
					}
				} catch (ClientProtocolException e) {
					break;
				} catch (IOException e) {
					break;
				} catch (Throwable t) {
					t.printStackTrace();
					break;
				}
			}
			while (System.currentTimeMillis() - startTime < TIME_BETWEEN_RUNS) {
				try {
					Thread.sleep(TIME_BETWEEN_RUNS + startTime - System.currentTimeMillis());
				} catch (InterruptedException e) {
					// Ignore
				}
			}
		}
	}

	public void sendStatus(String status) throws IOException {
		client.sendStatus(status);
	}
}

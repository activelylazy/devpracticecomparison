package uk.co.activelylazy.devpractice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

public class TaskRunner extends Thread {
	private List<DevPracticeTask> tasks = new ArrayList<DevPracticeTask>();
	private String[] texts = new String[] {
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
	private DevPracticeClient client;

	public TaskRunner(DevPracticeClient client) {
		this.client = client;
		tasks.add(new SayHelloWorldTask());
		tasks.add(new EchoContentBackTask());
		tasks.add(new CountWordsTask());
	}
	
	public void executeTask(int iteration, int text) throws ClientProtocolException, IOException {
		tasks.get(iteration).executeFor(client,texts[text]);
	}
	
	public synchronized void close() {
		this.close = true;
	}

	@Override
	public void run() {
		while (!close) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Ignore
			}
		}
	}

}

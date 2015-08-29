package ch.judos.generic.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.Type;
/**
 * @since 14.05.2015
 * @author Julian Schelker
 */
public class ClipSound {

	static class AudioListener implements LineListener {
		private boolean done = false;
		@Override
		public synchronized void update(LineEvent event) {
			Type eventType = event.getType();
			if (eventType == Type.STOP || eventType == Type.CLOSE) {
				this.done = true;
				notifyAll();
			}
		}
		public synchronized void waitUntilDone() throws InterruptedException {
			while (!this.done) {
				wait();
			}
		}
	}

	public static void playClipAsync(File clipFile) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					playClipSync(clipFile);
				}
				catch (IOException | UnsupportedAudioFileException | LineUnavailableException
					| InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void playClipSync(File clipFile) throws IOException,
		UnsupportedAudioFileException, LineUnavailableException, InterruptedException {

		AudioListener listener = new AudioListener();

		try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile)) {
			try (Clip clip = AudioSystem.getClip()) {
				clip.addLineListener(listener);
				clip.open(audioInputStream);
				clip.start();
				listener.waitUntilDone();
			}
		}
	}
}

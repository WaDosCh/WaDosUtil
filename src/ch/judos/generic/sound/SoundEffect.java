package ch.judos.generic.sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * @since 25.01.2012
 * @author Julian Schelker
 * @version 1.01 / 22.02.2013
 */
public class SoundEffect {
	//
	// public static void main(String[] args) {
	//
	// try {
	// SimpleSoundEffect s = new SimpleSoundEffect("pistol.wav");
	// Thread.sleep(500);
	// for (int i = 0; i < 5; i++) {
	//
	// long qs = System.nanoTime();
	// s.play();
	// qs = System.nanoTime() - qs;
	// System.out.println(qs / 1000 + " qs");
	// Thread.sleep(200);
	// }
	//
	// Thread.sleep(2500);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	private File file;

	public SoundEffect(String soundFileName) {
		this.file = new File(soundFileName);
		play(0);
	}

	/**
	 * play the sound in the background
	 * 
	 * @return true if succeeded, false otherwise
	 */
	public boolean play() {
		return play(1f);
	}

	/**
	 * play the sound in the background
	 * 
	 * @param volume
	 *            [0,1]
	 * @return true if succeeded, false otherwise
	 */
	public boolean play(final float volume) {
		Thread t = new Thread() {
			@Override
			public void run() {
				playSync(volume);
			}
		};
		t.start();
		return true;
	}

	/**
	 * plays the sound synchronous
	 * 
	 * @param volumePercentage
	 * @return when the sound could be played true, false if exception occured
	 */
	@SuppressWarnings("all")
	protected boolean playSync(float volumePercentage) {
		if (volumePercentage > 1)
			volumePercentage /= 100;
		try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.file);
			Clip clip = AudioSystem.getClip()) {
			clip.open(audioInputStream);

			FloatControl volume = (FloatControl) clip
				.getControl(FloatControl.Type.MASTER_GAIN);
			float max = volume.getMaximum();
			float min = volume.getMinimum();
			float level = (max - min) * volumePercentage + min;
			volume.setValue(level);

			clip.start(); // Start playing
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

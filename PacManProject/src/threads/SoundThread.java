package threads;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundThread extends AudioThread {
	
	private ArrayList<Boolean> eatGomme = new ArrayList<Boolean>();
	private ArrayList<Boolean> death = new ArrayList<Boolean>();
	private ArrayList<Boolean> eatGhost = new ArrayList<Boolean>();
	private ArrayList<Boolean> life = new ArrayList<Boolean>();

	

	public SoundThread(String threadName) {
		super(threadName); 
		// TODO Auto-generated constructor stub
	}
	
	public synchronized void addEatGomme() {
		eatGomme.add(true);
	}
	
	public synchronized void addDeath() {
		death.add(true);
	}
	
	public synchronized void addEatGhost() {
		eatGhost.add(true);
	}
	
	public synchronized void addLife() {
		life.add(true);
	}
	
	@Override
	protected void settings() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void doThatAtStart() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void doThat() {
		// TODO Auto-generated method stub
		if (eatGomme.size()>0) {
			eatGomme.remove(0);
			try {
				isPlaying = false;
				playAudio("chomp.wav");
				setVolume(vol);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(death.size()>0) {
			try {
				isPlaying = false;
				playAudio("death.wav");
				setVolume(vol);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(eatGhost.size()>0) {
			try {
				isPlaying = false;
				playAudio("eatghost.wav");
				setVolume(vol);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (life.size()>0) {
			try {
				isPlaying = false;
				playAudio("extrapac.wav");
				setVolume(vol);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	protected void doThatAtStop() {
		// TODO Auto-generated method stub
		
	}

}

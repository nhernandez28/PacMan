package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Main;
import resources.Tiles;
import threads.MusicThread;
import threads.RenderThread;
import threads.SoundThread;

public class AudioMenuPanel extends JPanel implements KeyListener{

	private static final long serialVersionUID = 1L;
	
	public JLabel pacManTitle;
	public JLabel audio;
	public JLabel offAudio;
	public JLabel onAudio;
	private JLabel increaseAudio;
	public JLabel decreaseAudio;
	public JLabel music;
	public JLabel offMusic;
	public JLabel onMusic;
	private JLabel increaseMusic;
	public JLabel decreaseMusic;
	public JLabel pacManIcon;
	public JLabel goBack;
	private int coordX=70;
	private int coordY= 200;
	
	private GameFrame gameFrame;
	private MusicThread musicTh;
	private SoundThread soundTh;
	Tiles t;
	
	public  AudioMenuPanel(GameFrame gameFrame,RenderThread renderTh, MusicThread musicTh, SoundThread soundTh) {
		this.musicTh = musicTh;
		this.soundTh = soundTh;
		this.gameFrame = gameFrame;
		setBackground(Color.BLACK);
		setLayout(null);
		try {
			t=new Tiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pacManTitle = new JLabel("");		
		BufferedImage p1 = Tiles.createWord(t.getTileNumber(73), t.getTileNumber(74),t.getTileNumber(75),t.getTileNumber(76),t.getTileNumber(77),t.getTileNumber(78),t.getTileNumber(79),t.getTileNumber(80));
		BufferedImage p2 = Tiles.createWord(t.getTileNumber(89),t.getTileNumber(90),t.getTileNumber(91), t.getTileNumber(92), t.getTileNumber(93), t.getTileNumber(94),t.getTileNumber(95), t.getTileNumber(96));
		BufferedImage PACMAN = Tiles.joinBelow(p1, p2);
		PACMAN = Tiles.resize(PACMAN, new Dimension(400,100));
		pacManTitle.setIcon(new ImageIcon(PACMAN));
		pacManTitle.setBounds(100, 0, 400, 100);
		
		audio = new JLabel();
		BufferedImage audioImage = Tiles.createWord(t.getTileNumber(39),t.getTileNumber(59),t.getTileNumber(42),t.getTileNumber(47),t.getTileNumber(53));
		audioImage = Tiles.resize(audioImage, new Dimension(180,50));
		audio.setIcon(new ImageIcon(audioImage));
		audio.setBounds(160, 120, 250, 50);
		
		
		onAudio= new JLabel();
		BufferedImage on = Tiles.createWord(t.getTileNumber(53),t.getTileNumber(52));
		on = Tiles.resize(on, new Dimension(60,30));
		onAudio.setIcon(new ImageIcon(on));
		onAudio.setBounds(100, 200, 300, 50);
		
		offAudio = new JLabel();
		BufferedImage off = Tiles.createWord(t.getTileNumber(53),t.getTileNumber(44),t.getTileNumber(44));
		off = Tiles.resize(off, new Dimension(90,30));
		offAudio.setIcon(new ImageIcon(off));
		offAudio.setBounds(300, 200, 300, 50);
		
		increaseAudio = new JLabel();
		BufferedImage up = Tiles.createWord(t.getTileNumber(59),t.getTileNumber(54));
		up = Tiles.resize(up, new Dimension(60,30));
		increaseAudio.setIcon(new ImageIcon(up));
		increaseAudio.setBounds(100, 270, 300, 30);
		
		decreaseAudio = new JLabel();
		BufferedImage down = Tiles.createWord(t.getTileNumber(42),t.getTileNumber(53),t.getTileNumber(61),t.getTileNumber(52));
		down = Tiles.resize(down, new Dimension(110,30));
		decreaseAudio.setIcon(new ImageIcon(down));
		decreaseAudio.setBounds(300, 260, 300, 50);
		
		music = new JLabel();
		BufferedImage musicImg = Tiles.createWord(t.getTileNumber(51),t.getTileNumber(59),t.getTileNumber(57),t.getTileNumber(47),t.getTileNumber(41));
		musicImg = Tiles.resize(musicImg, new Dimension(170,50));
		music.setIcon(new ImageIcon(musicImg));
		music.setBounds(160, 370, 400, 50);
		music.add(audio);
		
		onMusic= new JLabel();
		onMusic.setIcon(new ImageIcon(on));
		onMusic.setBounds(100, 450, 300, 50);
		
		offMusic = new JLabel();
		offMusic.setIcon(new ImageIcon(off));
		offMusic.setBounds(300, 450, 300, 50);
		
		increaseMusic = new JLabel();
		increaseMusic.setIcon(new ImageIcon(up));
		increaseMusic.setBounds(100, 520, 300, 50);
		
		decreaseMusic = new JLabel();
		decreaseMusic.setIcon(new ImageIcon(down));
		decreaseMusic.setBounds(300, 520, 300, 50);
		
		goBack = new JLabel();
		BufferedImage goBackImg = Tiles.createWord(t.getTileNumber(45),t.getTileNumber(53),t.getTileNumber(352),t.getTileNumber(40),t.getTileNumber(39),t.getTileNumber(41),t.getTileNumber(49));
		goBackImg = Tiles.resize(goBackImg, new Dimension(250,50));
		goBack.setIcon(new ImageIcon(goBackImg));
		goBack.setBounds(160, 600, 550, 50);
		
		pacManIcon= new JLabel();
		BufferedImage pacManImage = Tiles.createFullSpriteImage(t.getTileNumber(105), t.getTileNumber(106), t.getTileNumber(121), t.getTileNumber(122));
		pacManImage = Tiles.resize(pacManImage, new Dimension(30,30));
		pacManIcon.setIcon(new ImageIcon(pacManImage));
		pacManIcon.setBounds(coordX, coordY, 50, 50);
		add(pacManTitle);
		add(audio);
		add(pacManIcon);
		add(audio);
		add(onAudio);
		add(offAudio);
		add(increaseAudio);
		add(decreaseAudio);
		add(music);
		add(onMusic);
		add(offMusic);
		add(increaseMusic);
		add(decreaseMusic);
		add(goBack);
	
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key== KeyEvent.VK_DOWN) {
			if(getCoordY()<270 ) {
				if( getCoordY()==200) {
					setCoordY(260);
				}else if( getCoordY()==260) {
					setCoordY(450);
				}
				pacManIcon.setBounds(coordX, getCoordY(), 50, 50);
			}
			else if(getCoordY()>270 && getCoordY()<520) {
				if( getCoordY()==450) {
				setCoordY(520);
				}
				pacManIcon.setBounds(coordX, getCoordY(), 50, 50);
			}
			else if(getCoordY()==600) {
				
				setCoordY(200);
				setCoordX(70);
				pacManIcon.setBounds(coordX, getCoordY(), 50, 50);
			}
			else {
				setCoordY(600);
				setCoordX(120);
				pacManIcon.setBounds(coordX, getCoordY(), 50, 50);
			}	
		}
		if(key== KeyEvent.VK_UP) {
			if(getCoordY()<270 ) {
				if( getCoordY()==200) {
					setCoordY(600);
					setCoordX(120);
				}else if( getCoordY()==260) {
					setCoordY(200);
					if(getCoordX()==270) {
						setCoordX(270);
					}else {
					setCoordX(70);
					}
				}
				pacManIcon.setBounds(coordX, getCoordY(), 50, 50);
			}
			else if(getCoordY()>270 && getCoordY()<520) {
				if( getCoordY()==450) {
					setCoordY(260);
					if(getCoordX()==270) {
						setCoordX(270);
					}else {
						setCoordX(70);
					}
				
				}
				pacManIcon.setBounds(coordX, getCoordY(), 50, 50);
			}
			else if(getCoordY()==520) {
				setCoordY(450);
				if(getCoordX()==270) {
					setCoordX(270);
				}else {
					setCoordX(70);
				}
				pacManIcon.setBounds(coordX, getCoordY(), 50, 50);
			}
			else {
				setCoordY(520);
				setCoordX(70);
				pacManIcon.setBounds(coordX, getCoordY(), 50, 50);
			}	
		}
		if(key== KeyEvent.VK_RIGHT) {
			if(getCoordX()==70) {
				setCoordX(270);
				
			}
			pacManIcon.setBounds(coordX, getCoordY(), 50, 50);
			
		}
		if(key== KeyEvent.VK_LEFT) {
			if(getCoordX()==270) {
				setCoordX(70);
				
			}
			pacManIcon.setBounds(coordX, getCoordY(), 50, 50);
			
		}
		if(key== KeyEvent.VK_ENTER) {
			if(getCoordX()==70) {
				if(getCoordY()==200) {
					//add on audio event
					System.out.println("audio on");
					synchronized (soundTh) {
						soundTh.setMute(false);
					}
				}
				if(getCoordY()==260) {
					//add up audio event
					System.out.println("audio up");
					synchronized (soundTh) {
						soundTh.volumeUp();
					}
					
					
				}
				if(getCoordY()==450) {
					//add on music event
					System.out.println("music on");
					synchronized (musicTh) {
						musicTh.setMute(false);
					}
					
				}
				if(getCoordY()==520) {
					//add up music event
					System.out.println("music up");
					synchronized (musicTh) {
						musicTh.volumeUp();
					}
					
				}
			}
			else if(getCoordX()==270) {
				if(getCoordY()==200) {
					//add on audio event
					System.out.println("audio off");
					synchronized (soundTh) {
						soundTh.setMute(true);
					}
					
				}
				if(getCoordY()==260) {
					//add up audio event
					System.out.println("audio down");
					synchronized (soundTh) {
						soundTh.volumeDown();
					}
					
				}
				if(getCoordY()==450) {
					//add on music event
					System.out.println("music off");
					synchronized (musicTh) {
						musicTh.setMute(true);
					}
					
				}
				if(getCoordY()==520) {
					//add up music event
					System.out.println("music down");
					synchronized (musicTh) {
						musicTh.volumeDown();
					}
					
				}
			}else{
				System.out.println("start principal menu");	
				gameFrame.setPage("PrincipalMenu");
				System.out.println(gameFrame.getPage());
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	public int getCoordX() {
		return coordX;
	}
	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}
	public int getCoordY() {
		return coordY;
	}
	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}


}

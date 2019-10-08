package threads;

import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

import sprites.Ghost;
import sprites.MovingSpriteState;
import sprites.PacMan;
import sprites.Position;
import sprites.Sprites;
import view.StatusBarPanel;

public class PhysicsThread extends ThreadPerso {

	public List<List<Integer>> mazeValues;
	public JPanel gamePanel;
	
	public PacMan pacMan;
	private MovingSpriteState pacManWantedState;
	
	private Ghost blinky;
	private MovingSpriteState blinkyWantedState;	

	private Ghost pinky;
	private MovingSpriteState pinkyWantedState;	

	private Ghost clyde;
	private MovingSpriteState clydeWantedState;	
	
	private Ghost inky;
	private MovingSpriteState inkyWantedState;	
	
	private InvincibleThread invTh; 
	
	private Sprites pacDots; 
	private Sprites energizer;
	private static int score=0;
	private boolean ScoreBonus=false;
	public static boolean timerstarted=false;
	private static int scoreInvGhost;
	/**
	 * Management of the sounds
	 */
	private SoundThread soundTh;
	private MusicThread musicTh;
	private static boolean soundMute = false;
	private static boolean soundUp = false;
	private static boolean soundDown = false;
	private static int n = 1;
	private boolean gameOver=false;
	
	private static  int scoreLevelI=1;
	private int scoreLevel=0;
	private boolean Level=false;
	private boolean nextLevel=false;
	private static boolean collPacManGhostInv=false;
	
	
	/**
	 * The class needs the maze number matrix, the game panel size and of course the moving sprites,
	 * in order to locate them in the matrix.
	 * The location of each sprite in the matrix allow them to go or not in the wanted direction.
	 * @param mazeValues
	 * @param gamePanel
	 * @param pacMan
	 */
	public PhysicsThread(List<List<Integer>> mazeValues, JPanel gamePanel, PacMan pacMan, Ghost blinky,  Ghost pinky,  Ghost clyde,  Ghost inky, Sprites pacDots, Sprites energizer, MusicThread musicTh) {
		super("Physics");
		this.mazeValues = mazeValues;
		this.gamePanel = gamePanel;
		this.pacMan = pacMan;
		this.blinky = blinky;
		this.pinky = pinky;
		this.clyde = clyde;
		this.inky = inky;
		this.pacDots=pacDots;
		this.energizer=energizer;
		this.musicTh = musicTh;
		
	}
	public static boolean isCollPacManGhostInv() {
		return collPacManGhostInv;
	}
	public void setCollPacManGhostInv(boolean collPacManGhostInv) {
		this.collPacManGhostInv = collPacManGhostInv;
	}
	public boolean isLevel() {
		return Level;
	}

	public void setLevel(boolean level) {
		Level = level;
	}
	
	public Sprites getPacDots() {
		return pacDots;
	}



	public void setPacDots(Sprites pacDots) {
		this.pacDots = pacDots;
	}



	public Sprites getEnergizer() {
		return energizer;
	}



	public void setEnergizer(Sprites energizer) {
		this.energizer = energizer;
	}



	public static int getScore() {
		return score;
	}



	public void setScore(int score) {
		this.score = score;
	}



	public static int getScoreLevelI() {
		return scoreLevelI;
	}



	public void setScoreLevelI(int scoreLevelI) {
		this.scoreLevelI = scoreLevelI;
	}

	public static int getScoreInvGhost() {
		return scoreInvGhost;
	}
	public static void setScoreInvGhost(int scoreInvGhost) {
		PhysicsThread.scoreInvGhost = scoreInvGhost;
	}
	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	public  InvincibleThread getInvTh() {
		return invTh;
	}
	public void setInvTh(InvincibleThread invTh) {
		this.invTh = invTh;
	}

	@Override
	protected void doThatAtStart() {}

	@Override
	protected void doThat() {
		
		// wall collisions
		
		//pac-man
		if(pacMan != null) {
			synchronized(pacMan) {
				if(pacMan.getCurrentPosition() != null && pacMan.getCurrentSize() != null) {
					pacManWantedState = pacMan.getWantedState();
					if(pacManWantedState != MovingSpriteState.STOP) {
						
						// we need to use a little bit changed position 
						// so that pac-man can go a little bit farther in the maze
						int adaptedCurrentPosX;
						int adaptedCurrentPosY;
						Position currentMatrixPos; // the position of the sprite in the matrix
						int wantedBoxValue = -1; // the next box value where pac-man wants to go
						
						if(pacManWantedState == MovingSpriteState.LEFT) {
							adaptedCurrentPosX = pacMan.getCurrentPosition().getX() + pacMan.getCurrentSize().width/2;
							adaptedCurrentPosY = pacMan.getCurrentPosition().getY();
							currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
							if(currentMatrixPos != null) {
								if(currentMatrixPos.getX()-1 > 0) {
									wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(currentMatrixPos.getX()-1);
								}
								else {
									wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(mazeValues.get(0).size()-1); // opposite maze value
								}								
							}
						}
						else if(pacManWantedState == MovingSpriteState.RIGHT) {
							adaptedCurrentPosX = pacMan.getCurrentPosition().getX() - pacMan.getCurrentSize().width/2;
							adaptedCurrentPosY = pacMan.getCurrentPosition().getY();
							currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
							if(currentMatrixPos != null) {
								if(currentMatrixPos.getX()+1 < mazeValues.get(currentMatrixPos.getY()).size()) {
									wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(currentMatrixPos.getX()+1);
								}
								else {
									wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(0); // opposite maze value
								}								
							}
						}
						else if(pacManWantedState == MovingSpriteState.UP) {
							adaptedCurrentPosX = pacMan.getCurrentPosition().getX();
							adaptedCurrentPosY = pacMan.getCurrentPosition().getY() + pacMan.getCurrentSize().height/2;
							currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
							if(currentMatrixPos != null) {
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()-1).get(currentMatrixPos.getX());
							}
						}
						else if(pacManWantedState == MovingSpriteState.DOWN) {
							adaptedCurrentPosX = pacMan.getCurrentPosition().getX();
							adaptedCurrentPosY = pacMan.getCurrentPosition().getY() - pacMan.getCurrentSize().height/2;
							currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
							if(currentMatrixPos != null) {
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()+1).get(currentMatrixPos.getX());
							}
						}
						
						if(PacMan.acceptedMazeValues.contains(wantedBoxValue)) {
							if(wantedBoxValue == 352 && !pacMan.isInTunnel()) {
								pacMan.setState(pacManWantedState); // pac-man can be in that state
								pacMan.setIsInTunnel(true);
							}
							else if(wantedBoxValue != 352) {
								pacMan.setState(pacManWantedState); // pac-man can be in that state
								pacMan.setIsInTunnel(false);
							}
							
						}else if(!pacMan.isInTunnel()) {
							pacMan.setState(MovingSpriteState.STOP);
						}
					}			
				}			
			}
		}

		//blinky
		if(blinky != null) {
			synchronized(blinky) {
				if(!blinky.isInTheBox()) {
					if(blinky.getCurrentPosition() != null && blinky.getCurrentSize() != null) {
						blinkyWantedState = blinky.getWantedState();
						if(blinkyWantedState != MovingSpriteState.STOP) {

							// we need to use a little bit changed position 
							// so that pac-man can go a little bit farther in the maze
							int adaptedCurrentPosX;
							int adaptedCurrentPosY;
							Position currentMatrixPos; // the position of the sprite in the matrix
							int wantedBoxValue = -1; // the next box value where pac-man wants to go

							if(blinkyWantedState == MovingSpriteState.LEFT) {
								adaptedCurrentPosX = blinky.getCurrentPosition().getX() + blinky.getCurrentSize().width/2;
								adaptedCurrentPosY = blinky.getCurrentPosition().getY();
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								if(currentMatrixPos != null) {
									wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(currentMatrixPos.getX()-1);
								}
							}
							else if(blinkyWantedState == MovingSpriteState.RIGHT) {
								adaptedCurrentPosX = blinky.getCurrentPosition().getX() - blinky.getCurrentSize().width/2;
								adaptedCurrentPosY = blinky.getCurrentPosition().getY();
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								if(currentMatrixPos != null) {
									wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(currentMatrixPos.getX()+1);
								}
							}
							else if(blinkyWantedState == MovingSpriteState.UP) {
								adaptedCurrentPosX = blinky.getCurrentPosition().getX();
								adaptedCurrentPosY = blinky.getCurrentPosition().getY() + blinky.getCurrentSize().height/2;
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								if(currentMatrixPos != null) {
									wantedBoxValue = mazeValues.get(currentMatrixPos.getY()-1).get(currentMatrixPos.getX());
								}
							}
							else if(blinkyWantedState == MovingSpriteState.DOWN) {
								adaptedCurrentPosX = blinky.getCurrentPosition().getX();
								adaptedCurrentPosY = blinky.getCurrentPosition().getY() - blinky.getCurrentSize().height/2;
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								if(currentMatrixPos != null) {
									wantedBoxValue = mazeValues.get(currentMatrixPos.getY()+1).get(currentMatrixPos.getX());
								}
							}


							if( Ghost.acceptedMazeValues.contains(wantedBoxValue) || blinky.isInTheBox()) {
								blinky.setState(blinkyWantedState);
							}else {					
								if(blinky.getBehaviorThread() == null || !blinky.getBehaviorThread().isRunning()) {
									blinky.startBehaviorThread();
								}
								// blinky set another possible direction
								blinky.getBehaviorThread().changeDirection();
							}
						}			
					}							
				}
			}
		}

		
		//pinky
		if(pinky != null) {
			synchronized(pinky) {
				if(!pinky.isInTheBox()) {
					if(pinky.getCurrentPosition() != null && pinky.getCurrentSize() != null) {
						pinkyWantedState = pinky.getWantedState();
						if(pinkyWantedState != MovingSpriteState.STOP) {

							// we need to use a little bit changed position 
							// so that pac-man can go a little bit farther in the maze
							int adaptedCurrentPosX;
							int adaptedCurrentPosY;
							Position currentMatrixPos; // the position of the sprite in the matrix
							int wantedBoxValue = -1; // the next box value where pac-man wants to go

							if(pinkyWantedState == MovingSpriteState.LEFT) {
								adaptedCurrentPosX = pinky.getCurrentPosition().getX() + pinky.getCurrentSize().width/2;
								adaptedCurrentPosY = pinky.getCurrentPosition().getY();
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(currentMatrixPos.getX()-1);
							}
							else if(pinkyWantedState == MovingSpriteState.RIGHT) {
								adaptedCurrentPosX = pinky.getCurrentPosition().getX() - pinky.getCurrentSize().width/2;
								adaptedCurrentPosY = pinky.getCurrentPosition().getY();
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(currentMatrixPos.getX()+1);
							}
							else if(pinkyWantedState == MovingSpriteState.UP) {
								adaptedCurrentPosX = pinky.getCurrentPosition().getX();
								adaptedCurrentPosY = pinky.getCurrentPosition().getY() + pinky.getCurrentSize().height/2;
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()-1).get(currentMatrixPos.getX());
							}
							else if(pinkyWantedState == MovingSpriteState.DOWN) {
								adaptedCurrentPosX = pinky.getCurrentPosition().getX();
								adaptedCurrentPosY = pinky.getCurrentPosition().getY() - pinky.getCurrentSize().height/2;
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()+1).get(currentMatrixPos.getX());
							}

							if( Ghost.acceptedMazeValues.contains(wantedBoxValue) || pinky.isInTheBox()) {
								pinky.setState(pinkyWantedState);
							}else {					
								if(pinky.getBehaviorThread() == null || !pinky.getBehaviorThread().isRunning()) {
									pinky.startBehaviorThread();
								}
								// pinky set another possible direction
								pinky.getBehaviorThread().changeDirection();
							}
						}			
					}							
				}
			}			
		}
		
		//clyde
		if(clyde != null) {
			synchronized(clyde) {
				if(!clyde.isInTheBox()) {
					if(clyde.getCurrentPosition() != null && clyde.getCurrentSize() != null) {
						clydeWantedState = clyde.getWantedState();
						if(clydeWantedState != MovingSpriteState.STOP) {

							// we need to use a little bit changed position 
							// so that pac-man can go a little bit farther in the maze
							int adaptedCurrentPosX;
							int adaptedCurrentPosY;
							Position currentMatrixPos; // the position of the sprite in the matrix
							int wantedBoxValue = -1; // the next box value where pac-man wants to go

							if(clydeWantedState == MovingSpriteState.LEFT) {
								adaptedCurrentPosX = clyde.getCurrentPosition().getX() + clyde.getCurrentSize().width/2;
								adaptedCurrentPosY = clyde.getCurrentPosition().getY();
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(currentMatrixPos.getX()-1);
							}
							else if(clydeWantedState == MovingSpriteState.RIGHT) {
								adaptedCurrentPosX = clyde.getCurrentPosition().getX() - clyde.getCurrentSize().width/2;
								adaptedCurrentPosY = clyde.getCurrentPosition().getY();
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(currentMatrixPos.getX()+1);
							}
							else if(clydeWantedState == MovingSpriteState.UP) {
								adaptedCurrentPosX = clyde.getCurrentPosition().getX();
								adaptedCurrentPosY = clyde.getCurrentPosition().getY() + clyde.getCurrentSize().height/2;
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()-1).get(currentMatrixPos.getX());
							}
							else if(clydeWantedState == MovingSpriteState.DOWN) {
								adaptedCurrentPosX = clyde.getCurrentPosition().getX();
								adaptedCurrentPosY = clyde.getCurrentPosition().getY() - clyde.getCurrentSize().height/2;
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()+1).get(currentMatrixPos.getX());
							}

							if( Ghost.acceptedMazeValues.contains(wantedBoxValue)) {
								clyde.setState(clydeWantedState);
							}else {					
								if(clyde.getBehaviorThread() == null || !clyde.getBehaviorThread().isRunning()) {
									clyde.startBehaviorThread();
								}
								// clyde set another possible direction
								clyde.getBehaviorThread().changeDirection();
							}
						}			
					}				
				}
			}
		}

		
		//inky
		if(inky != null) {
			synchronized(inky) {
				if(!inky.isInTheBox()) {
					if(inky.getCurrentPosition() != null && inky.getCurrentSize() != null) {
						inkyWantedState = inky.getWantedState();
						if(inkyWantedState != MovingSpriteState.STOP) {

							// we need to use a little bit changed position 
							// so that pac-man can go a little bit farther in the maze
							int adaptedCurrentPosX;
							int adaptedCurrentPosY;
							Position currentMatrixPos; // the position of the sprite in the matrix
							int wantedBoxValue = -1; // the next box value where pac-man wants to go

							if(inkyWantedState == MovingSpriteState.LEFT) {
								adaptedCurrentPosX = inky.getCurrentPosition().getX() + inky.getCurrentSize().width/2;
								adaptedCurrentPosY = inky.getCurrentPosition().getY();
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(currentMatrixPos.getX()-1);
							}
							else if(inkyWantedState == MovingSpriteState.RIGHT) {
								adaptedCurrentPosX = inky.getCurrentPosition().getX() - inky.getCurrentSize().width/2;
								adaptedCurrentPosY = inky.getCurrentPosition().getY();
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()).get(currentMatrixPos.getX()+1);
							}
							else if(inkyWantedState == MovingSpriteState.UP) {
								adaptedCurrentPosX = inky.getCurrentPosition().getX();
								adaptedCurrentPosY = inky.getCurrentPosition().getY() + inky.getCurrentSize().height/2;
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()-1).get(currentMatrixPos.getX());
							}
							else if(inkyWantedState == MovingSpriteState.DOWN) {
								adaptedCurrentPosX = inky.getCurrentPosition().getX();
								adaptedCurrentPosY = inky.getCurrentPosition().getY() - inky.getCurrentSize().height/2;
								currentMatrixPos = mazeToMatrixPosition(new Position(adaptedCurrentPosX, adaptedCurrentPosY), gamePanel, mazeValues);
								wantedBoxValue = mazeValues.get(currentMatrixPos.getY()+1).get(currentMatrixPos.getX());
							}

							if( Ghost.acceptedMazeValues.contains(wantedBoxValue) || inky.isInTheBox()) {
								inky.setState(inkyWantedState); // pac-man can be in that state
							}else {					
								if(inky.getBehaviorThread() == null || !inky.getBehaviorThread().isRunning()) {
									inky.startBehaviorThread();
								}
								// inky set another possible direction
								inky.getBehaviorThread().changeDirection();
							}
						}			
					}						
				}
			}			
		}
		
		
		if(ghostCollision()) {
			pinky.setInTheBox(true);
			clyde.setInTheBox(true);
			inky.setInTheBox(true);
			GhostsExitBoxThread.clydeCanGoOut=true;
			GhostsExitBoxThread.pinkyCanGoOut=true;
			GhostsExitBoxThread.inkyCanGoOut=true;
			clyde.setState(MovingSpriteState.STOP);
			pinky.setState(MovingSpriteState.STOP);
			inky.setState(MovingSpriteState.STOP);
			
			timerstarted=true;
			if(pacMan.getLife()!=0 && pacMan.getLife()<=4) {
				int life=pacMan.getLife()-1;
				pacMan.setLife(life);
				if(life==0) {
					System.out.println("GAMEOVER");
					gameOver=true;
				}
			}
		}
		if(pacDotsCollision()){
			
			
		}
		if(energizerCollision()) {
			
		}
		if(score>=10000 && !ScoreBonus ) {
			pacMan.setLife(pacMan.getLife()+1);
			StatusBarPanel.setImageLives(pacMan.getLife()+1);
			StatusBarPanel.livesImg.setIcon(new ImageIcon(StatusBarPanel.Lives));
			ScoreBonus=true;
		}
	}
	
	
	/**
	 * Stop doing the actions defined in doThat() method.
	 */
	public synchronized void pauseThread() {
		paused = true;
		if(blinky != null) {
			synchronized(blinky) {
				if(blinky.getBehaviorThread() != null) {
					blinky.getBehaviorThread().pauseThread();
				}			
			}			
		}
		if(pinky != null) {
			synchronized(pinky) {
				if(pinky.getBehaviorThread() != null) {
					pinky.getBehaviorThread().pauseThread();
				}			
			}			
		}
		if(clyde != null) {
			synchronized(clyde) {
				if(clyde.getBehaviorThread() != null) {
					clyde.getBehaviorThread().pauseThread();
				}			
			}			
		}
		if(inky != null) {
			synchronized(inky) {
				if(inky.getBehaviorThread() != null) {
					inky.getBehaviorThread().pauseThread();
				}			
			}			
		}

	}
	
	/**
	 * Start again or continue doing the actions defined in doThat() method.
	 */
	public synchronized void resumeThread() {
		paused = false;
		if(blinky != null) {
			synchronized(blinky) {
				if(blinky.getBehaviorThread() != null) {
					blinky.getBehaviorThread().resumeThread();
				}			
			}			
		}
		if(pinky != null) {
			synchronized(pinky) {
				if(pinky.getBehaviorThread() != null) {
					pinky.getBehaviorThread().resumeThread();
				}			
			}			
		}
		if(clyde != null) {
			synchronized(clyde) {
				if(clyde.getBehaviorThread() != null) {
					clyde.getBehaviorThread().resumeThread();
				}			
			}			
		}
		if(inky != null) {
			synchronized(inky) {
				if(inky.getBehaviorThread() != null) {
					inky.getBehaviorThread().resumeThread();
				}			
			}			
		}

	}
	
	
	@Override
	protected void doThatAtStop() {
		if(blinky != null) {
			synchronized(blinky) {
				if(blinky.getBehaviorThread() != null) {
					blinky.getBehaviorThread().stopThread();
				}			
			}			
		}
		if(pinky != null) {
			synchronized(pinky) {
				if(pinky.getBehaviorThread() != null) {
					pinky.getBehaviorThread().stopThread();
				}			
			}			
		}
		if(clyde != null) {
			synchronized(clyde) {
				if(clyde.getBehaviorThread() != null) {
					clyde.getBehaviorThread().stopThread();
				}			
			}			
		}
		if(inky != null) {
			synchronized(inky) {
				if(inky.getBehaviorThread() != null) {
					inky.getBehaviorThread().stopThread();
				}			
			}			
		}

	}
	

	/**
	 * Transform a game panel position in a maze matrix position.
	 * @param panelPos is the position in the game panel.
	 * @param panel is the game panel
	 * @param mazeValues is the matrix numbers corresponding to the maze.
	 * @return the position in the maze numbers matrix.
	 */
	public static Position mazeToMatrixPosition(Position panelPos, JPanel panel, List<List<Integer>> mazeValues) {
		int matPosX = (int)Math.round((panelPos.getX() * mazeValues.get(0).size()) / (double)panel.getWidth());
		int matPosY = (int)Math.round((panelPos.getY() * mazeValues.size()) / (double)panel.getHeight());
		if(matPosX >= mazeValues.get(0).size() || matPosY >= mazeValues.size()) {
			System.out.println("matrix value out of the bounds !");
			return null;
		}
		return new Position(matPosX, matPosY);
	}
	
	/**
	 * Transform a matrix position in a game panel position.
	 * @param matrixPos is the position in the matrix mazeValues.
	 * @param panel is the game panel
	 * @param mazeValues is the matrix numbers corresponding to the csv maze file.
	 * @return the position in the game panel (in pixel).
	 */
	public static Position matrixToMazePosition(Position matrixPos, JPanel panel, List<List<Integer>> mazeValues) {
		int panelPosX = (int)Math.round((matrixPos.getX() * panel.getWidth()) / (double)mazeValues.get(0).size());
		int panelPosY = (int)Math.round((matrixPos.getY() * panel.getHeight()) / (double)mazeValues.size());
		if(panelPosX >= panel.getWidth() || panelPosY >= panel.getHeight()) {
			System.out.println("panel value out of the bounds !");
			return null;
		}
		return new Position(panelPosX, panelPosY);
	}
	
	

	public boolean ghostCollision() {
		synchronized(pacMan) {
			if(pacMan.getCurrentPosition() != null && pacMan.getCurrentSize() != null) {
				int pacman_left = pacMan.getCurrentPosition().getX();
				int pacman_right = pacMan.getCurrentPosition().getX() + pacMan.getCurrentSize().width;
				int pacman_up = pacMan.getCurrentPosition().getY();
				int pacman_down = pacMan.getCurrentPosition().getY() + pacMan.getCurrentSize().height;
				
				
				
				if(collisionWith(pacman_left, pacman_right, pacman_up, pacman_down, blinky) && pacMan.invincible()) {
					System.out.println("collision with blinky!");
					score+=200*(int)Math.pow(2,pacMan.eatenFantom());
					setScoreInvGhost(200*(int)Math.pow(2,pacMan.eatenFantom()));
					StatusBarPanel.valueScore.setText(""+score);
					pacMan.setEatenFantom(pacMan.eatenFantom()+1);
					setCollPacManGhostInv(true);
					resetOneSprite(blinky);
					return false;
				}
				
				
				else if(collisionWith(pacman_left, pacman_right, pacman_up, pacman_down, blinky)) {
					System.out.println("collision with blinky!");
					resetAllSprites();
					return true;
				}
				
				
				if(collisionWith(pacman_left, pacman_right, pacman_up, pacman_down, pinky) && pacMan.invincible()) {
					System.out.println("collision with pinky!");
					score+=200*(int)Math.pow(2,pacMan.eatenFantom());
					StatusBarPanel.valueScore.setText(""+score);
					setScoreInvGhost(200*(int)Math.pow(2,pacMan.eatenFantom()));
					pacMan.setEatenFantom(pacMan.eatenFantom()+1);
					setCollPacManGhostInv(true);
					resetOneSprite(pinky);
					return false;
				}		
				
				else if(collisionWith(pacman_left, pacman_right, pacman_up, pacman_down, pinky)) {
					System.out.println("collision with pinky!");
					resetAllSprites();
					return true;
				}
				
				if(collisionWith(pacman_left, pacman_right, pacman_up, pacman_down, clyde) && pacMan.invincible()) {
					System.out.println("collision with clyde!");
					score+=200*(int)Math.pow(2,pacMan.eatenFantom());
					StatusBarPanel.valueScore.setText(""+score);
					setScoreInvGhost(200*(int)Math.pow(2,pacMan.eatenFantom()));
					pacMan.setEatenFantom(pacMan.eatenFantom()+1);
					setCollPacManGhostInv(true);
					resetOneSprite(clyde);
					return false;
				}
				
				else if(collisionWith(pacman_left, pacman_right, pacman_up, pacman_down, clyde)) {
					System.out.println("collision with clyde!");
					resetAllSprites();
					return true;
				}
				
				if(collisionWith(pacman_left, pacman_right, pacman_up, pacman_down, inky) && pacMan.invincible()) {
					System.out.println("collision with inky!");
					score+=200*(int)Math.pow(2,pacMan.eatenFantom());
					StatusBarPanel.valueScore.setText(""+score);
					setScoreInvGhost(200*(int)Math.pow(2,pacMan.eatenFantom()));
					pacMan.setEatenFantom(pacMan.eatenFantom()+1);
					setCollPacManGhostInv(true);
					resetOneSprite(inky);
					return false;
				}
				
				else if(collisionWith(pacman_left, pacman_right, pacman_up, pacman_down, inky)) {
					System.out.println("collision with inky!");
					resetAllSprites();
					return true;
				}
			}
	
			return false;			
		}
	}
	private boolean pacDotsCollision() {
		synchronized(pacMan) {
			if(collisionWith(pacDots)) {
				System.out.println("collision pacDot ");
				return true;
			}
			return false;
				
		}
	}
	private boolean energizerCollision() {
		synchronized(pacMan) {
			
			if(collisionWithE(energizer)) {
				score=score+50;
				StatusBarPanel.valueScore.setText(""+score);
				System.out.println("collision energizer, showX : "+energizer.showX+ " showY : "+energizer.showY);
				pacMan.setInvincible(true);
				invTh=new InvincibleThread(pacMan , musicTh);
				invTh.start();
				return true;
			}
			return false;
				
		}
	}
	
	private boolean collisionWith(Sprites pacDots) {
		for(int i=0; i<pacDots.getSprites().size();i++){
			int positionX=pacDots.getSpriteNb(i).getCurrentPosition().getX();
			int positionY= pacDots.getSpriteNb(i).getCurrentPosition().getY();
			if(pacMan.getCurrentPosition().getX()<=positionX+(20/2) && pacMan.getCurrentPosition().getX()>= positionX-(20/2)  && pacMan.getCurrentPosition().getY()<=positionY+(20/2) && pacMan.getCurrentPosition().getY()>= positionY-(20/2) )  {
				if (!soundMute) {
					playSound("chomp.wav");
				}
				pacDots.showX=pacDots.getSpriteNb(i).getCurrentPosition().getX();
				pacDots.showY=pacDots.getSpriteNb(i).getCurrentPosition().getY();
				pacDots.removeSpriteNb(i);
				score=score+10;
				StatusBarPanel.valueScore.setText(""+score);
				return true;
			}	
		}
		return false;
	}
	private boolean collisionWithE(Sprites energizer) {
		for(int i=0; i<energizer.getSprites().size();i++){
			int positionX=energizer.getSpriteNb(i).getCurrentPosition().getX();
			int positionY= energizer.getSpriteNb(i).getCurrentPosition().getY();
			if(pacMan.getCurrentPosition().getX()<=positionX+(13/2) && pacMan.getCurrentPosition().getX()>= positionX-(13/2)  && pacMan.getCurrentPosition().getY()<=positionY+(12/2) && pacMan.getCurrentPosition().getY()>= positionY-(12/2) )  {
				if (!soundMute) {
					playSound("chomp.wav");
				}
				energizer.showX=energizer.getSpriteNb(i).getCurrentPosition().getX();
				energizer.showY=energizer.getSpriteNb(i).getCurrentPosition().getY();
				energizer.removeSpriteNb(i);
				return true;
			}	
		}
		return false;
	}
	
	
	
	private boolean collisionWith(int pacman_left, int pacman_right, int pacman_up, int pacman_down, Ghost ghost) {
		if(ghost != null) {
			synchronized(ghost) {
				int ghost_left = ghost.getCurrentPosition().getX();
				int ghost_right = ghost.getCurrentPosition().getX() + ghost.getCurrentSize().width;
				int ghost_up = ghost.getCurrentPosition().getY();
				int ghost_down = ghost.getCurrentPosition().getY() + ghost.getCurrentSize().height;		
			
				if( pacman_left < ghost_right && pacman_right > ghost_left && pacman_down > ghost_up && pacman_up < ghost_down ) {
					
					if (!soundMute) {
						playSound("death.wav");
					}
					return true;
				}
				return false;
			}			
		}
		return false;
	}
	
	private void playSound(String sound) {
		soundTh = new SoundThread("soundTh");
		if(soundTh != null) {
			synchronized(soundTh) {
				if (soundUp) {
					try {
						soundTh.playAudio(sound);
						soundTh.volumeUp(n);
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // play sound
				} else if (soundDown) {
					try {
						soundTh.playAudio(sound);
						soundTh.volumeDown(n);
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // play sound
				} else {
					try {
						soundTh.playAudio(sound);
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // play sound
				}
					
			}
		}
	}
	
	public synchronized static void setSoundMute(boolean Mute) {
		soundMute = Mute;
	}
	
	private void resetAllSprites() {
		
		if(pacMan != null) {
			pacMan.setCurrentPosition(matrixToMazePosition(pacMan.getMatrixPosition(), gamePanel, mazeValues));
		}
		
		// stop the ghost that are replaced in the box before replacing them
		if(blinky != null) {
			blinky.setState(MovingSpriteState.STOP);
		}		
		if(pinky != null) {
			pinky.setState(MovingSpriteState.STOP);
		}		
		if(clyde != null) {
			clyde.setState(MovingSpriteState.STOP);
		}
		if(inky != null) {
			inky.setState(MovingSpriteState.STOP);
		}
		
		if(blinky != null) {
			blinky.setCurrentPosition(matrixToMazePosition(blinky.getMatrixPosition(), gamePanel, mazeValues));
		}
		if(pinky != null) {
			pinky.setCurrentPosition(matrixToMazePosition(pinky.getMatrixPosition(), gamePanel, mazeValues));
		}
		if(clyde != null) {
			clyde.setCurrentPosition(matrixToMazePosition(clyde.getMatrixPosition(), gamePanel, mazeValues));
		}
		if(inky != null) {
			inky.setCurrentPosition(matrixToMazePosition(inky.getMatrixPosition(), gamePanel, mazeValues));
		}
		
		//finally say that they are in the box, so that ghost exit box thread does its job
		if(blinky != null) {
			blinky.setInTheBox(true);
		}
		if(pinky != null) {
			pinky.setInTheBox(true);
		}
		if(clyde != null) {
			clyde.setInTheBox(true);
		}
		if(inky != null) {
			inky.setInTheBox(true);
		}
	}
	
	private void resetOneSprite(Ghost ghost) {
		ghost.setCurrentPosition(matrixToMazePosition(ghost.getMatrixPosition(), gamePanel, mazeValues));
		ghost.setInTheBox(true);
		ghost.setState(MovingSpriteState.STOP);
	}

	public synchronized static void setVUp(int x) {
		soundUp = true;
		n = x;
	}
	
	public synchronized static void setVDown(int x) {
		soundDown = true;
		System.out.println("x setVDown ="+x);
		n = x;
		
	}
	
}

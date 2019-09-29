package sprites;

import java.awt.image.BufferedImage;

import resources.ListImages;
import resources.Tiles;

public abstract class Ghost extends MovingSprite {

	public Ghost(Position start_position, Tiles tiles) {
		super(start_position, tiles);
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	protected void createFullSpriteImages() {
		spriteFullImages = new ListImages();
		BufferedImage cornerTopLeft, cornerTopRight, cornerBottomLeft, cornerBottomRight, img;

		for (int i = 0; i < 16; i+=2) {
			cornerTopLeft = spriteImages.getImagesList().get(i);
			cornerTopRight = spriteImages.getImagesList().get(i+1);
			cornerBottomLeft = spriteImages.getImagesList().get(i+16);
			cornerBottomRight = spriteImages.getImagesList().get(i+16+1);
			img = createFullSpriteImage(cornerTopLeft, cornerTopRight, cornerBottomLeft, cornerBottomRight);
			spriteFullImages.add(img);			
		}
	}

	@Override
	protected void createNoMovementAnimation() {
		noMovementAnimation = goLeftAnimation; // no animation for no movement
	}

	@Override
	protected void createGoLeftAnimation() {
		goLeftAnimation.add(4);
		goLeftAnimation.add(5);
	}

	@Override
	protected void createGoRightAnimation() {
		goRightAnimation.add(0);
		goRightAnimation.add(1);
	}

	@Override
	protected void createGoUpAnimation() {
		goUpAnimation.add(6);
		goUpAnimation.add(7);
	}

	@Override
	protected void createGoDownAnimation() {
		goDownAnimation.add(2);
		goDownAnimation.add(3);
	}

	@Override
	protected void createDeathAnimation() {
		deathAnimation = goLeftAnimation; //no animation for death
	}
	

}

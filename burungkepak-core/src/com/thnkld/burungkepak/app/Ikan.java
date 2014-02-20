package com.thnkld.burungkepak.app;

import org.flixel.FlxG;
import org.flixel.FlxSound;
import org.flixel.FlxSprite;

public class Ikan extends FlxSprite {

	private final FlxSound soundLoncat;
	public boolean autokepak;

	public Ikan() {
	    loadGraphicLagi();

	    addAnimation("terbangsantai", new int[] {1, 2, 1, 0}, 4);
	    addAnimation("naik", new int[] {1, 2, 1, 0}, 10);
	    addAnimation("turun", new int[] {2}, 10);
	    addAnimation("belok45", new int[] {3}, 4, false);
	    addAnimation("belokdepan", new int[] {4}, 4, false);
	    addAnimation("mati", new int[] {5}, 4, false);

		soundLoncat = FlxG.loadSound("sounds/loncat.wav");

		setFacing(RIGHT);
    }

	void loadGraphicLagi() {
		loadGraphic("images/characterfly.png", true, true, 31, 16, true);
	}

	@Override
	public void update() {
		if (autokepak) {
			if (velocity.y < 0) {
				play("naik");
			} else {
				play("turun");
			}
		}

		if (alive) {
			int reverser = getFacing() == LEFT? 1: -1;
			if (velocity.y < -50) {
				angle = reverser * +10;
			} else if (velocity.y > 300) {
				angle = reverser * -60;
			} else if (velocity.y > 150) {
				angle = reverser * -40;
			} else if (velocity.y > 100) {
				angle = reverser * -20;
			} else if (velocity.y > 50) {
				angle = reverser * -10;
			} else {
				angle = reverser * 0;
			}
		}

		super.update();
	}

	public void loncat() {
		soundLoncat.play(true);
		velocity.y = -375;
	}
}
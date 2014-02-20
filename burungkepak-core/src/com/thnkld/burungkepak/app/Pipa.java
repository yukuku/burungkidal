package com.thnkld.burungkepak.app;

import org.flixel.FlxSprite;

public class Pipa extends FlxSprite {
	public static final String TAG = Pipa.class.getSimpleName();
	public final int kind;

	public Pipa(int kind) {
		this.kind = kind;

		loadGraphic("images/pipa.png", false, false, 48, 240);
		addAnimation("normal", new int[] {0});
		addAnimation("scared", new int[] {1});
	}

	public void scared() {
		play("scared");
	}
}

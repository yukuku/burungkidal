package com.thnkld.burungkepak.app;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxText;

public class LostState extends FlxState {
	FlxText tPetunjuk = new FlxText(FlxG.width - 90, FlxG.height - 20, 200, "Pencet apa aja");

	@Override
	public void create() {
		FlxG.mouse.show();

		FlxText text = new FlxText(50, FlxG.height / 2, FlxG.width, "kalah kamu! " + (FlxG.score == 0? " dan ga dapet apa-apa": (" tapi udah dapet " + FlxG.score)));
		add(text);

		tPetunjuk.setColor(0xff888888);
		add(tPetunjuk);
	}

	@Override
	public void update() {
		super.update();

		if (FlxG.keys.any() || FlxG.mouse.justReleased()) {
			FlxG.switchState(new PlayState());
		}
	}
}


package com.thnkld.burungkepak.app;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxText;

public class TitleState extends FlxState {

	FlxText tJudul = new FlxText(20, 80, 200, "Ikan Kepak");
	FlxText tSubJudul = new FlxText(20, 170, 200, "Ikan pun mau jadi terkenal");
	FlxText tPetunjuk = new FlxText(FlxG.width - 90, FlxG.height - 20, 200, "Pencet apa aja");

	@Override
	public void create() {
		FlxG.mouse.hide();

		tJudul.scale.x = tJudul.scale.y = 4;
		add(tJudul);

		add(tSubJudul);

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


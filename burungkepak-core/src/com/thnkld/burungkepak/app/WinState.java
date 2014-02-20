package com.thnkld.burungkepak.app;

import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.event.IFlxButton;

public class WinState extends FlxState {
	@Override
	public void create() {
		FlxG.mouse.show();

		FlxText text = new FlxText(0, FlxG.height / 2, FlxG.width, "kalah kamu!");
		add(text);

		FlxButton b = new FlxButton(16, FlxG.height / 2 + 32, "ulang ah", new IFlxButton() {
			@Override
			public void callback() {
				FlxG.switchState(new PlayState());
			}
		});
		add(b);
	}
}


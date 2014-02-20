package com.thnkld.burungkepak.app;

import org.flixel.FlxPoint;
import org.flixel.FlxSprite;

public class PlainButton extends FlxSprite {
	public static final String TAG = PlainButton.class.getSimpleName();

	public PlainButton() {
		makeGraphic(16, 16, 0xffff0000);
	}

	public boolean isWithin(FlxPoint p) { // 16 pixels radius max
		float x = p.x;
		float y = p.y;
		float cx = this.x + this.width * 0.5f;
		float cy = this.y + this.height * 0.5f;
		return Math.hypot(x - cx, y - cy) <= 16.f;
	}
}

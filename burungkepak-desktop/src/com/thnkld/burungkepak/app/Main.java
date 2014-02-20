package com.thnkld.burungkepak.app;

import org.flixel.FlxDesktopApplication;
import org.flixel.FlxG;

public class Main {
	public static void main(String[] args) {
		new FlxDesktopApplication(new Game(new Game.GG() {
			@Override
			public void submitLeaderboardScore(final String lbid, final int score, boolean rankingButton) {
				FlxG.log("submitLeaderboardScore on desktop [" + lbid + "] score: " + score + " rankingButton: " + rankingButton);
			}
		}), 480, 640, true);
	}
}
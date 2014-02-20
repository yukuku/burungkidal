package com.thnkld.burungkepak.app;

import org.flixel.FlxG;
import org.flixel.FlxGame;

public class Game extends FlxGame {

	public static GG gg;

	public interface GG {
		void submitLeaderboardScore(int score, boolean rankingButton);
	}

    public Game(GG gg) {
	    super(240, 320, PlayState.class, 1);
	    Game.gg = gg;
	    FlxG.setFlashFramerate(60);
	    FlxG.setFramerate(60);
    }

}


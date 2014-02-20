package com.thnkld.burungkepak.ios;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.thnkld.burungkepak.app.Game;
import org.flixel.FlxG;
import org.flixel.FlxGame;
import org.robovm.bindings.gpgs.GPGToastPlacement;
import org.robovm.bindings.gpp.GPPURLHandler;
import org.robovm.bindings.playservices.PlayServicesManager;
import org.robovm.cocoatouch.foundation.NSAutoreleasePool;
import org.robovm.cocoatouch.foundation.NSDictionary;
import org.robovm.cocoatouch.foundation.NSError;
import org.robovm.cocoatouch.foundation.NSObject;
import org.robovm.cocoatouch.foundation.NSURL;
import org.robovm.cocoatouch.uikit.UIApplication;

public class FlxIOSApplication extends IOSApplication.Delegate implements Game.GG {

	private static final String CLIENT_ID = "5749872660-d341c41gmdla0kqed2vsudfhfra1h5h3.apps.googleusercontent.com";

	private PlayServicesManager manager;
	private IOSApplication iosApp;

	int pendingScore;
	boolean pendingOpenLB;

	public static void main(String[] args) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(args, null, FlxIOSApplication.class);
		pool.drain();
	}

	@Override
	protected IOSApplication createApplication() {
		IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.allowIpod = true;
		config.orientationPortrait = true;
		config.orientationLandscape = false;
		config.preventScreenDimming = true;

		FlxGame game = new Game(this);

		this.iosApp = new IOSApplication((ApplicationListener) game.stage, config);
		return iosApp;
	}

	@Override
	public boolean didFinishLaunching(final UIApplication application, final NSDictionary launchOptions) {
		final boolean result = super.didFinishLaunching(application, launchOptions);

		manager = new PlayServicesManager();
		manager.setClientId(CLIENT_ID);
		manager.setViewController(iosApp.getUIViewController());
		manager.setToastLocation(PlayServicesManager.TOAST_BOTH, GPGToastPlacement.GPGToastPlacementTop);
		manager.setUserDataToRetrieve(true, false);
		manager.setLoginCallback(loginCallback);
		manager.didFinishLaunching();

		return result;
	}

	private PlayServicesManager.LoginCallback loginCallback = new PlayServicesManager.LoginCallback() {
		@Override
		public void success() {
			FlxG.log("@@success");
			if (pendingScore != 0) {
				submitScore(pendingScore);
			}
			pendingScore = 0;
			if (pendingOpenLB) {
				openLB();
				pendingOpenLB = false;
			}
		}

		@Override
		public void error(final NSError error) {
			FlxG.log("@@error");
//			if (connectionResult.hasResolution()) {
//				FlxG.log("5");
//				try {
//					connectionResult.startResolutionForResult(this, REQUEST_SIGN_IN);
//				} catch (Exception e) {
//					FlxG.log("7");
//				}
//			} else {
//				FlxG.log("6");
//			}
		}
	};

	@Override
	public boolean openURL(final UIApplication application, final NSURL url, final String sourceApplication, final NSObject annotation) {
		return GPPURLHandler.handleURL(url, sourceApplication, annotation);
	}

	private void submitScore(int score) {
		manager.postScore("CggIlLDgtRUQAhAG", score); // TODO add tag , "s0__");
	}

	void openLB() {
		manager.showLeaderboard("CggIlLDgtRUQAhAG");
	}

	@Override
	public void submitLeaderboardScore(final int score, final boolean rankingButton) {
		if (manager.isLoggedIn()) {
			FlxG.log("1");
			submitScore(score);
			if (rankingButton) {
				FlxG.log("2");
				openLB();
			}
		} else {
			FlxG.log("3");
			pendingScore = score;
			if (rankingButton) {
				FlxG.log("4");
				pendingOpenLB = true;
				manager.login(loginCallback);
			}
		}
	}
}
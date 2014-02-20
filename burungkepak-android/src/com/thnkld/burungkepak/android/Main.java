package com.thnkld.burungkepak.android;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.RatioResolutionStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.thnkld.burungkepak.app.Game;
import org.flixel.FlxGame;

public class Main extends AndroidApplication implements Game.GG, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	private static final String TAG = Main.class.getSimpleName();
	private static final int REQUEST_LEADERBOARD = 1;
	private static final int REQUEST_SIGN_IN = 2;

	private FlxGame _game;
	protected AndroidApplicationConfiguration cfg;

	GoogleApiClient client;
	boolean mExplicitSignOut = false;
	boolean mInSignInFlow = false; // set to true when you're in the middle of the
	// sign in flow, to know you should not attempt
	// to connect on onStart()


	int pendingScore;
	boolean pendingOpenLB;

	public Main() {
		_game = new Game(this);
		cfg = new AndroidApplicationConfiguration();
		cfg.useAccelerometer = false;
		cfg.useCompass = false;
		cfg.useGL20 = true;
		cfg.resolutionStrategy = new RatioResolutionStrategy(240, 320);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initialize((ApplicationListener)_game.stage, cfg);

		GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this, this, this);
		builder.addApi(Games.API);
		builder.addScope(Games.SCOPE_GAMES);
		client = builder.build();
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (!mInSignInFlow && !mExplicitSignOut) {
			// auto sign in
			client.connect();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	void openLB() {
		startActivityForResult(Games.Leaderboards.getLeaderboardIntent(client, "CggIlLDgtRUQAhAG"), REQUEST_LEADERBOARD);
	}

	private void submitScore(int score) {
		Games.Leaderboards.submitScore(client, "CggIlLDgtRUQAhAG", score, "s0__");
	}

	@Override
	public void submitLeaderboardScore(final int score, boolean rankingButton) {
		if (client.isConnected()) {
			Log.d(TAG, "1");
			submitScore(score);
			if (rankingButton) {
				Log.d(TAG, "2");
				openLB();
			}
		} else {
			Log.d(TAG, "3");
			pendingScore = score;
			if (rankingButton) {
				Log.d(TAG, "4");
				pendingOpenLB = true;
				client.connect();
			}
		}
	}

	@Override
	public void onConnected(final Bundle bundle) {
		Log.d(TAG, "@@onConnected");
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
	public void onConnectionSuspended(final int i) {
		Log.d(TAG, "@@onConnectionSuspended");
	}

	@Override
	public void onConnectionFailed(final ConnectionResult connectionResult) {
		Log.d(TAG, "@@onConnectionFailed");
		if (connectionResult.hasResolution()) {
			Log.d(TAG, "5");
			try {
				connectionResult.startResolutionForResult(this, REQUEST_SIGN_IN);
			} catch (Exception e) {
				Log.d(TAG, "7");
			}
		} else {
			Log.d(TAG, "6");
		}
	}
}

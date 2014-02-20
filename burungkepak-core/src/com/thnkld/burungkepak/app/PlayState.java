package com.thnkld.burungkepak.app;

import org.flixel.FlxBasic;
import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxObject;
import org.flixel.FlxPoint;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.event.IFlxButton;
import org.flixel.event.IFlxCollision;

import java.util.Random;

public class PlayState extends FlxState {

	final int VGERAK = 140;
	final int MUNCULTIAP = 100;

	FlxGroup ikans = new FlxGroup();
	Ikan ikan;
	int fasepipa = 0;
	float faseanimasi = 0;
	float fasesiap = 0;
	FlxGroup floortiles = new FlxGroup();
	FlxObject flooroffset = new FlxObject(0, 0); // dummy
	FlxGroup pipas = new FlxGroup();
	FlxText tScore = new FlxText(10, 10, 100, "0");
	FlxText tPetunjuk = new FlxText(120, 125, 80, "<- Pencet layar biar terbang");
	FlxSprite judulsplash = new FlxSprite(0, 0);
	FlxButton bUlang;
	FlxButton bLiatRanking;

	enum Stet { siap, animasi, beneran, mati };
	Stet stet = Stet.siap;

	@Override
	public void create() {
		FlxG.debug = true;

		FlxG.mouse.hide();

        FlxSprite bg = new FlxSprite(0, 0);
		bg.loadGraphic("images/bg.png");
		bg.scrollFactor = new FlxPoint();
		add(bg);

		add(pipas);

		for (int i = 0; i < FlxG.width / 26 + 3; i++) {
			final FlxSprite floortile = new FlxSprite(i * 26 - 26, FlxG.height - 32);
			floortile.loadGraphic("images/floortile.png");
			floortiles.add(floortile);
		}
		add(floortiles);

		add(flooroffset);

		add(ikans);

		tPetunjuk.setColor(0xff888888);

		tScore.setShadow(0, 1);
		tScore.setShadow(0xff000000);
		add(tScore);

		judulsplash.loadGraphic("images/judulsplash.png");

		bUlang = new FlxButton(80, 100, "Ulang", new IFlxButton() {
			@Override
			public void callback() {
				mulaisiap();
				stet = Stet.siap;
			}
		});
		add(bUlang);

		bLiatRanking = new FlxButton(80, 150, "Ranking", new IFlxButton() {
			@Override
			public void callback() {
				Game.gg.submitLeaderboardScore(FlxG.score, true);
			}
		});
		add(bLiatRanking);

		mulaisiap();
	}

	void mulaisiap() {
		FlxG.mouse.hide();

		setAndDisplayScore(0);

		fasesiap = 0;
		bUlang.visible = false;
		bLiatRanking.visible = false;

		flooroffset.velocity.x = -50;

		ikans.clear();
		ikan = new Ikan();
		ikan.x = FlxG.width / 3;
		ikan.y = 125;
		ikan.alive = true;
		ikan.autokepak = false;
		ikan.setFacing(FlxObject.RIGHT);
		ikan.play("terbangsantai");
		ikan.angle = (float) (Math.random()/1000.0);
		ikans.add(ikan);

		pipas.clear();

		add(judulsplash);
		add(tPetunjuk);
	}

	void mulaianimasi() {
		faseanimasi = 0;
		ikan.velocity.x = 200;
		ikan.acceleration.y = 1500;
		ikan.play("terbangsantai");
		remove(judulsplash);
		remove(tPetunjuk);
	}

	void mulaibeneran() {
		flooroffset.velocity.x = VGERAK;

		fasepipa = 0;
		tambahPipa();

		ikan.autokepak = true;
		ikan.setFacing(FlxObject.LEFT);
		ikan.x = FlxG.width * 2 / 3;
		ikan.velocity.x = 0;
	}

	void mulaimati() {
		FlxG.flash(0xffffffff, 0.2f);

		FlxG.mouse.show();

		Game.gg.submitLeaderboardScore(FlxG.score, false);
		FlxG.play("sounds/tabrakan.wav");

		flooroffset.velocity.x = 0;
		pipas.setAll("velocity", new FlxPoint(0, 0));
		ikan.play("mati");
		ikan.autokepak = false;
		ikan.velocity.x = 0;
		ikan.velocity.y = 0;
		ikan.acceleration.y = 0;

		bUlang.visible = true;
		bLiatRanking.visible = true;
	}

	static Random r = new Random();

	void tambahPipa() {
		int bukaan = 90;
		int offset = 20 + r.nextInt(FlxG.height - bukaan - 70);

		FlxSprite pipa1 = new FlxSprite();
		pipa1.loadGraphic("images/pipa.png", false, false);
		pipa1.x = -pipa1.width;
		pipa1.y = offset - pipa1.height;
		pipa1.velocity.x = VGERAK;
		pipa1.health = 1;
		pipa1.angle = 180;
		pipa1.scale.x = -1;
		pipa1.immovable = true;
		pipas.add(pipa1);

		FlxSprite pipa2 = new FlxSprite();
		pipa2.loadGraphic("images/pipa.png", false, false);
		pipa2.x = -pipa2.width;
		pipa2.y = offset + bukaan;
		pipa2.velocity.x = VGERAK;
		pipa2.health = 0;
		pipa2.immovable = true;
		pipas.add(pipa2);
	}

	@Override
	public void update() {
		super.update();

		boolean any = FlxG.keys.justPressedAny() || FlxG.mouse.justPressed();

		if (stet == Stet.siap) {
			fasesiap += FlxG.elapsed;
			if (any && fasesiap > 0.3f) {
				stet = Stet.animasi;
				mulaianimasi();
			}
		} else if (stet == Stet.animasi) {
			if (any) {
				ikan.loncat();
			}

			faseanimasi += FlxG.elapsed;
			if (faseanimasi < 0.6f) {
				ikan.velocity.x = 160 + 100 * (faseanimasi - 0.6f);
				flooroffset.velocity.x = -40;
			} else if (faseanimasi < 1.0f) {
				ikan.play("belok45");
				ikan.velocity.x = 100;
				flooroffset.velocity.x = -20;
			} else if (faseanimasi < 1.2f) {
				ikan.play("belokdepan");
				ikan.velocity.x = 20;
				flooroffset.velocity.x = -5;
			} else if (faseanimasi < 1.4f) {
				ikan.play("belokdepan");
				ikan.velocity.x = -20;
				flooroffset.velocity.x = +5;
			} else if (faseanimasi < 1.8f) {
				ikan.setFacing(FlxObject.LEFT);
				ikan.play("belok45");
				ikan.velocity.x = -100;
				flooroffset.velocity.x = +50;
			} else if (faseanimasi < 2.2f) {
				ikan.velocity.x = -200;
				flooroffset.velocity.x = VGERAK;
			}

			if (faseanimasi > 1.8f && ikan.x <= FlxG.width * 2 / 3) {
				stet = Stet.beneran;
				mulaibeneran();
			}
		} else if (stet == Stet.beneran) {
			if (any) {
				ikan.loncat();
			}

			fasepipa += 1;

			// remove already on the left
			for (final FlxBasic member : pipas.members) {
				FlxSprite pipa = (FlxSprite) member;
				if (pipa == null) continue;
				if (pipa.x < -pipa.width) {
					pipas.remove(pipa);
				}
			}

			// increase score for passed pipa
			for (final FlxBasic member : pipas.members) {
				FlxSprite pipa = (FlxSprite) member;
				if (pipa == null) continue;
				if (pipa.health != 0 && ikan.x + ikan.width < pipa.x) {
					FlxG.play("sounds/lewatpipa.wav");
					setAndDisplayScore((int) (FlxG.score + pipa.health));
					pipa.health = 0;
				}
			}

			// spawn more
			if (fasepipa > MUNCULTIAP) {
				tambahPipa();
				fasepipa = 0;
			}
		} else if (stet == Stet.mati) {
			// ga ngapa2in
		}

		// alive test
		if (ikan.alive) {
			FlxG.collide(ikan, pipas, new IFlxCollision() {
				@Override
				public void callback(final FlxObject ikan, final FlxObject pipa) {
					ikan.alive = false;
				}
			});

			if (ikan.y + ikan.height < 0) {
				ikan.alive = false;
			}

			if (ikan.y + ikan.height > FlxG.height - 32) {
				ikan.y = FlxG.height - 32 - ikan.height;
				ikan.alive = false;
			}
		}

		if (!ikan.alive && stet != Stet.mati) {
			stet = Stet.mati;
			mulaimati();
		}

		{ // adjust floortiles
			int x = (int) (flooroffset.x % 26);
			int c = -1;
			for (final FlxBasic member : floortiles.members) {
				FlxSprite floortile = (FlxSprite) member;
				if (floortile != null) {
					floortile.x = c++ * 26 + x;
				}
			}
		}
	}

	void setAndDisplayScore(final int newscore) {
		FlxG.score = newscore;
		tScore.setText("" + newscore);
	}
}


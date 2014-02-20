package com.thnkld.burungkepak.app;

import com.badlogic.gdx.math.Interpolation;
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
	final float MUNCULPIPATIAP = 1.66666667f;
	final int BUKAAN = 90;

	enum LB {
		NORMAL("CggIlLDgtRUQAhAG"),
		BABY("CggIlLDgtRUQAhAL"),
		KETUK("CggIlLDgtRUQAhAM"),;
		final String id;
		LB(final String id) {
			this.id = id;
		}
	}

	static class Flags {
		boolean mode_gampang_geser = false;
		boolean mode_gampang_pipa_kabur = false;
		boolean mode_latihan_ketukan = false;
	}

	LB lb;
	Flags flags;

	FlxGroup ikans = new FlxGroup();
	Ikan ikan;
	float fasepipa = 0;
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
	PlainButton bBaby;
	BouncingButton bKetuk;

	static class BouncingButton extends PlainButton {
		Interpolation interp;
		float origY;
		float elapsed;

		void bounce() {
			interp = new Interpolation.BounceIn(3);
			origY = y;
			elapsed = 0;
		}

		@Override public void update() {
			super.update();
			if (interp != null) {
				elapsed += FlxG.elapsed;
				float u = interp.apply(elapsed / 2f);
				this.y = origY + -400 * u;
				if (elapsed > 2f) {
					interp = null;
				}
			}
		}
	}

	enum Stet { siap, animasi, beneran, mati };
	Stet stet = Stet.siap;

	@Override
	public void create() {
		FlxG.debug = true;

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
				FlxG.switchState(new PlayState());
			}
		});
		add(bUlang);

		bLiatRanking = new FlxButton(80, 150, "Ranking", new IFlxButton() {
			@Override
			public void callback() {
				Game.gg.submitLeaderboardScore(lb.id, FlxG.score, true);
			}
		});
		add(bLiatRanking);

		bBaby = new PlainButton();
		bBaby.loadGraphic("images/button_baby.png");
		bBaby.x = 190;
		bBaby.y = 295;
		add(bBaby);

		bKetuk = new BouncingButton();
		bKetuk.loadGraphic("images/button_ketuk.png", true, false, 16, 16);
		bKetuk.addAnimation("normal", new int[] {0, 1}, 1);
		bKetuk.play("normal");
		bKetuk.x = 215;
		bKetuk.y = 295;
		add(bKetuk);

		mulaisiap();
	}

	void setupmode_normal() {
		lb = LB.NORMAL;
		flags = new Flags() {{
		}};
	}

	void setupmode_baby() {
		lb = LB.BABY;
		flags = new Flags() {{
			mode_gampang_pipa_kabur = true;
		}};
	}

	void setupmode_ketuk() {
		lb = LB.KETUK;
		flags = new Flags() {{
			mode_latihan_ketukan = true;
		}};
	}

	void mulaisiap() {
		FlxG.mouse.show();

		setAndDisplayScore(0);

		fasesiap = 0;
		bUlang.visible = false;
		bLiatRanking.visible = false;
		bBaby.visible = true;
		bKetuk.visible = true;

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

		setupmode_normal();
	}

	void mulaianimasi() {
		FlxG.mouse.hide();

		faseanimasi = 0;
		ikan.velocity.x = 200;
		ikan.acceleration.y = 1500;
		ikan.play("terbangsantai");
		bBaby.visible = false;
		bKetuk.visible = false;
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

		Game.gg.submitLeaderboardScore(lb.id, FlxG.score, false);
		FlxG.play("sounds/mati.wav");

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
		int offset = 20 + r.nextInt(FlxG.height - BUKAAN - 70);

		if (flags.mode_latihan_ketukan) {
			offset = 99; // tengah2
		}

		Pipa pipa1 = new Pipa(1);
		pipa1.x = -pipa1.width;
		pipa1.y = offset - pipa1.height;
		pipa1.velocity.x = VGERAK;
		pipa1.health = 1;
		pipa1.angle = 180;
		pipa1.scale.x = -1;
		pipa1.immovable = true;
		pipas.add(pipa1);

		Pipa pipa2 = new Pipa(2);
		pipa2.x = -pipa2.width;
		pipa2.y = offset + BUKAAN;
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

			if (any) {
				// check button press first
				if (bBaby.isWithin(FlxG.mouse)) {
					bBaby.angularVelocity = -400;
					bBaby.velocity.x = -170;
					bKetuk.visible = false;
					setupmode_baby();
				} else if (bKetuk.isWithin(FlxG.mouse)) {
					bKetuk.bounce();
					bBaby.visible = false;
					setupmode_ketuk();
				} else {
					// no button press
					if (fasesiap > 0.3f) {
						stet = Stet.animasi;
						mulaianimasi();
					}
				}
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

			fasepipa += FlxG.elapsed;

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

			if (flags.mode_gampang_geser) {
				for (final FlxBasic member : pipas.members) {
					Pipa pipa = (Pipa) member;
					if (pipa == null) continue;
					if (ikan.x - (pipa.x + pipa.width) < 40) {
						float y;
						if (pipa.kind == 1) {
							y = ikan.y - pipa.height - BUKAAN / 2;
						} else {
							y = ikan.y + ikan.height + BUKAAN / 2;
						}
						pipa.y = (pipa.y * 5 + y) / 6f;
					}
				}
			}

			if (flags.mode_gampang_pipa_kabur) {
				for (final FlxBasic member : pipas.members) {
					Pipa pipa = (Pipa) member;
					if (pipa == null) continue;
					if (ikan.x < (pipa.x + pipa.width + 20) && (ikan.x + ikan.width > pipa.x)) {
						if (pipa.kind == 1) {
							if (ikan.y - (pipa.y + pipa.height) < 20) {
								pipa.scared();
								float tujuY = ikan.y - pipa.height - 20;
								pipa.y = (pipa.y * 2f + tujuY) / 3f;
							}
						} else { // pipa bawah
							if (pipa.y - (ikan.y + ikan.height) < 20) {
								pipa.scared();
								float tujuY = ikan.y + ikan.height + 20;
								pipa.y = (pipa.y * 1f + tujuY * 2f) / 3f;
							}
						}
					}
				}
			}

			// spawn more
			if (flags.mode_latihan_ketukan) {
				if (fasepipa >= 0.5f) {
					tambahPipa();
					fasepipa = 0;
				}
			} else {
				if (fasepipa > MUNCULPIPATIAP) {
					tambahPipa();
					fasepipa = 0;
				}
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


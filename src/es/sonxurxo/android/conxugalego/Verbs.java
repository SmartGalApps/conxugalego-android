package es.sonxurxo.android.conxugalego;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import es.sonxurxo.android.conxugalego.model.VerbalTime;
import es.sonxurxo.android.conxugalego.util.AboutDialog;
import es.sonxurxo.android.conxugalego.util.CustomSlidingDrawer;
import es.sonxurxo.android.conxugalego.util.InstalledApplicationsUtils;
import es.sonxurxo.android.conxugalego.util.SearchConjugationTask;
import es.sonxurxo.android.conxugalego.util.VolgaChecker;

public class Verbs extends Activity {

	private static final int DEFINE_REQUEST_CODE = 1;

	protected RelativeLayout mainLayout;

	protected LinearLayout mLinearLayout;
	protected Typeface regularFont, boldFont;
	protected TextView searchInDictionaryText;
	protected ImageButton searchInDictionaryButton;
	protected TextView translateText;
	protected ImageButton translateButton;
	protected ZoomControls zoomControls;

	ArrayList<TextView> allTextViews;

	protected Animation mFadeOut;

	protected CustomSlidingDrawer drawer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.verbs);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.customtitlebar);

		this.regularFont = Typeface.createFromAsset(getAssets(), "fonts/CantarellRegular.ttf");
		this.boldFont = Typeface.createFromAsset(getAssets(), "fonts/CantarellBold.ttf");
		this.mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		this.mLinearLayout = (LinearLayout) findViewById(R.id.list);
		this.searchInDictionaryButton = (ImageButton) this.findViewById(R.id.searchInDictionary);
		this.searchInDictionaryText = (TextView) this.findViewById(R.id.searchInDictionaryText);
		this.searchInDictionaryText.setTypeface(this.boldFont);
		this.translateButton = (ImageButton) this.findViewById(R.id.translate);
		this.translateText = (TextView) this.findViewById(R.id.translateText);
		this.translateText.setTypeface(this.boldFont);

		this.zoomControls = (ZoomControls) this.findViewById(R.id.zoomControls);

		this.mFadeOut = new Animation() {
			// empty on purpose
		};
		this.mFadeOut = new AlphaAnimation(1, 0);
		this.mFadeOut.setFillAfter(true);
		this.mFadeOut.setDuration(1000);
		this.mFadeOut.setStartOffset(2000);
		Verbs.this.zoomControls.setAnimation(Verbs.this.mFadeOut);
		Verbs.this.zoomControls.setVisibility(View.INVISIBLE);

		this.mLinearLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				Verbs.this.showZoomControls();
				return false;
			}

		});

		this.zoomControls.setOnZoomInClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!Verbs.this.mFadeOut.hasEnded()) {
					Verbs.this.zoomIn();
				}
				Verbs.this.showZoomControls();
			}

		});
		this.zoomControls.setOnZoomOutClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!Verbs.this.mFadeOut.hasEnded()) {
					Verbs.this.zoomOut();
				}
				Verbs.this.showZoomControls();
			}

		});
		Bundle extras = getIntent().getExtras();
		this.allTextViews = new ArrayList<TextView>();

		if (extras.containsKey("verbalTimes")) {
			Object[] objects = (Object[]) extras.get("verbalTimes");
			this.printResult(objects);

			final String word = extras.getString("word");
			this.searchInDictionaryText.setText(getString(R.string.searchInDictionary, word));
			this.translateText.setText(getString(R.string.translate, word));
			final ImageView handle = (ImageView) this.findViewById(R.id.handle);
			this.drawer = (CustomSlidingDrawer) this.findViewById(R.id.drawer);
			this.drawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

				@Override
				public void onDrawerOpened() {
					handle.setImageResource(R.drawable.handle_on);
				}
			});
			this.drawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

				@Override
				public void onDrawerClosed() {
					handle.setImageResource(R.drawable.handle_off);
				}
			});

			this.searchInDictionaryButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					if (InstalledApplicationsUtils.isDiccionarioGalegoInstalled(Verbs.this)) {
						Intent searchInDictionaryIntent = new Intent(Intent.ACTION_VIEW);
						searchInDictionaryIntent.setComponent(new ComponentName(
								"es.galapps.android.diccionariogalego",
								"es.galapps.android.diccionariogalego.Definitions"));
						searchInDictionaryIntent.putExtra("word", word);
						startActivityForResult(searchInDictionaryIntent, DEFINE_REQUEST_CODE);
					} else {
						new AlertDialog.Builder(Verbs.this)
								.setTitle(R.string.downloadDiccionarioGalego)
								.setMessage(R.string.downloadDiccionarioGalegoMessage)
								.setCancelable(true)
								.setPositiveButton(Verbs.this.getString(android.R.string.ok),
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												Intent goToMarket = new Intent(
														Intent.ACTION_VIEW,
														Uri.parse(Verbs.this
																.getString(R.string.diccionarioMarketURL)));
												startActivity(goToMarket);
											}

										})
								.setNegativeButton(Verbs.this.getString(R.string.cancel), null)
								.create().show();
					}
				}

			});
			this.translateButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					if (InstalledApplicationsUtils.isTradutorGalegoInstalled(Verbs.this)) {
						Intent tradutorIntent = new Intent(Intent.ACTION_VIEW);
						tradutorIntent.setComponent(new ComponentName(
								"es.galapps.android.tradutorgalego",
								"es.galapps.android.tradutorgalego.TradutorGalegoActivity"));
						tradutorIntent.putExtra("word", word);
						startActivityForResult(tradutorIntent, DEFINE_REQUEST_CODE);
					} else {
						new AlertDialog.Builder(Verbs.this)
								.setTitle(R.string.downloadTradutorGalego)
								.setMessage(R.string.downloadTradutorGalegoMessage)
								.setCancelable(true)
								.setPositiveButton(Verbs.this.getString(android.R.string.ok),
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												Intent goToMarket = new Intent(
														Intent.ACTION_VIEW,
														Uri.parse(Verbs.this
																.getString(R.string.tradutorMarketURL)));
												startActivity(goToMarket);
											}

										})
								.setNegativeButton(Verbs.this.getString(R.string.cancel), null)
								.create().show();
					}
				}

			});
		} else if (extras.containsKey("infinitive")) {
			this.findViewById(R.id.drawer).setVisibility(View.GONE);
			String infinitive = extras.getString("infinitive");
			this.search(infinitive);
		}

		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());
	}

	protected void showZoomControls() {
		Verbs.this.mFadeOut.cancel();
		Verbs.this.mFadeOut.reset();
		Verbs.this.zoomControls.setVisibility(View.VISIBLE);
		Verbs.this.zoomControls.startAnimation(Verbs.this.mFadeOut);
	}

	protected void zoomIn() {
		for (TextView textView : this.allTextViews) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() + 2);
		}
	}

	protected void zoomOut() {
		for (TextView textView : this.allTextViews) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() - 2);
		}
	}

	protected void search(final String infinitive) {
		new SearchConjugationTask(this) {

			@Override
			protected void onPostExecuteNotFound() {
				Intent data = new Intent();
				data.putExtra("word", infinitive);
				setResult(-1, data);
				finish();
			}

			@Override
			protected void onPostExecuteFound(VerbalTime[] verbalTimes) {
				Verbs.this.printResult(verbalTimes);
				try {
					if (!VolgaChecker.exists(Verbs.this, infinitive)) {
						Toast.makeText(Verbs.this,
								Verbs.this.getString(R.string.verbDoesNotExist, infinitive),
								Toast.LENGTH_LONG).show();
					}
				} catch (IOException e) {
					// Erro ao comprobar se existe, co ficheiro.
					// Non se fai nada
				}
			}

			@Override
			protected void onPostExecuteConnectionError() {
				Intent data = new Intent();
				setResult(-2, data);
				finish();
			}

		}.execute(infinitive);
	}

	protected void printResult(Object[] verbalTimesAsObjectArray) {
		for (Object o : verbalTimesAsObjectArray) {
			VerbalTime verbalTime = (VerbalTime) o;
			LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = vi.inflate(R.layout.row, null);

			TextView title = (TextView) v.findViewById(R.id.title);
			this.allTextViews.add(title);
			title.setTypeface(this.boldFont);
			LinearLayout vTimes = (LinearLayout) v.findViewById(R.id.times);

			String name = verbalTime.getName();
			String text = "";
			if (name.equals("PI")) {
				text = getString(R.string.pi);
			} else if (name.equals("EI")) {
				text = getString(R.string.ei);
			} else if (name.equals("II")) {
				text = getString(R.string.ii);
			} else if (name.equals("MI")) {
				text = getString(R.string.mi);
			} else if (name.equals("FI")) {
				text = getString(R.string.fi);
			} else if (name.equals("TI")) {
				text = getString(R.string.ti);
			} else if (name.equals("PS")) {
				text = getString(R.string.ps);
			} else if (name.equals("IS")) {
				text = getString(R.string.is);
			} else if (name.equals("FS")) {
				text = getString(R.string.fs);
			} else if (name.equals("IP")) {
				text = getString(R.string.ip);
			} else if (name.equals("IA")) {
				text = getString(R.string.ia);
			} else if (name.equals("IN")) {
				text = getString(R.string.in);
			} else if (name.equals("FN")) {
				text = getString(R.string.fn);
			}
			title.setText(text);

			int size = verbalTime.getTimes().length;
			for (int i = 0; i < size; i++) {
				String verbalTimeString = verbalTime.getTimes()[i];
				View vTimeView;
				if (verbalTimeString.startsWith(VerbalTime.CHEAT_MARK)) {
					verbalTimeString = verbalTimeString.substring(1, verbalTimeString.length());
					vTimeView = vi.inflate(R.layout.cheatedsingleline, null);
				} else {
					vTimeView = vi.inflate(R.layout.singleline, null);
				}
				TextView person = (TextView) vTimeView.findViewById(R.id.person);
				this.allTextViews.add(person);
				TextView time = (TextView) vTimeView.findViewById(R.id.time);
				this.allTextViews.add(time);
				time.setSelected(true);
				String personText = "";
				if (size == 3) {
					switch (i) {
					case 0:
						personText = getString(R.string.infinitive);
						break;
					case 1:
						personText = getString(R.string.gerund);
						break;
					case 2:
						personText = getString(R.string.participe);
						break;
					}
				} else if (size == 5) {
					switch (i) {
					case 0:
						personText = getString(R.string.s2);
						break;
					case 1:
						personText = getString(R.string.ss2);
						break;
					case 2:
						personText = getString(R.string.p1);
						break;
					case 3:
						personText = getString(R.string.p2);
						break;
					case 4:
						personText = getString(R.string.pp2);
						break;
					}
				} else {
					switch (i) {
					case 0:
						personText = getString(R.string.s1);
						break;
					case 1:
						personText = getString(R.string.s2);
						break;
					case 2:
						personText = getString(R.string.s3);
						break;
					case 3:
						personText = getString(R.string.p1);
						break;
					case 4:
						personText = getString(R.string.p2);
						break;
					case 5:
						personText = getString(R.string.p3);
						break;
					}
				}
				person.setText(personText);
				person.setTypeface(this.regularFont);
				if (name.equals("IN")) {
					time.setText(Verbs.this.getString(R.string.no) + " " + verbalTimeString);
				} else {
					time.setText(verbalTimeString);
				}
				time.setTypeface(this.boldFont);
				vTimes.addView(vTimeView);
			}
			this.mLinearLayout.addView(v);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			AlertDialog builder;
			try {
				builder = AboutDialog.create(this);
				builder.show();
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		case R.id.share:
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					this.getString(R.string.shareTitle));
			shareIntent
					.putExtra(android.content.Intent.EXTRA_TEXT, this.getString(R.string.webURL));

			startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case DEFINE_REQUEST_CODE:
			switch (resultCode) {
			case -1:
				Toast.makeText(this,
						getString(R.string.wordNotFound, data.getStringExtra("infinitive")),
						Toast.LENGTH_SHORT).show();
				break;
			case -2:
				Toast.makeText(this, getString(R.string.connectionError), Toast.LENGTH_SHORT)
						.show();
				break;
			}
			break;
		}
	}

	@Override
	public void onBackPressed() {

		if (this.drawer != null && this.drawer.isOpened()) {

			this.drawer.close();

		} else {

			super.onBackPressed();
		}
	}

}

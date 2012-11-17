package es.sonxurxo.android.conxugalego;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import es.sonxurxo.android.conxugalego.model.VerbalTime;
import es.sonxurxo.android.conxugalego.util.AboutDialog;
import es.sonxurxo.android.conxugalego.util.SearchConjugationTask;
import es.sonxurxo.android.conxugalego.util.VolgaChecker;

public class ConxugaActivity extends Activity {

	private static final String VERSION = "VERSION:";

	protected static final int ABOUT_ID = 0;
	protected static final int ACKNOWELEDGES_ID = 1;

	protected LinearLayout mMainLayout;
	protected TextView mIntroText;
	protected EditText mVerb;
	private ImageButton mLaunchButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.customtitlebar);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		if (prefs.getInt(VERSION, 1) < getPackageVersion()) {
			showNews(getLayoutInflater(), this);
		}

		this.mIntroText = (TextView) findViewById(R.id.introDataText);
		this.mIntroText.setTypeface(Typeface
				.createFromAsset(getAssets(), "fonts/CantarellBold.ttf"));

		this.mVerb = (EditText) findViewById(R.id.input);
		this.mVerb.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					ConxugaActivity.this.search();
					return true;
				}
				return false;
			}
		});

		this.mLaunchButton = (ImageButton) findViewById(R.id.search);
		this.mLaunchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ConxugaActivity.this.search();
			}

		});

	}

	protected void search() {
		final String text = ConxugaActivity.this.mVerb.getText().toString();
		if (text.contains(" ")) {
			Toast.makeText(this, R.string.errorNoWhitespaces, Toast.LENGTH_SHORT).show();
			return;
		}
		// if (!text.endsWith(getString(R.string.ar)) &&
		// !text.endsWith(getString(R.string.er))
		// && !text.endsWith(getString(R.string.ir))
		// && !text.endsWith(getString(R.string.arT))
		// && !text.endsWith(getString(R.string.erT))
		// && !text.endsWith(getString(R.string.irT))
		// && !text.endsWith(getString(R.string.Ar)) &&
		// !text.endsWith(getString(R.string.Er))
		// && !text.endsWith(getString(R.string.Ir))
		// && !text.endsWith(getString(R.string.ArT))
		// && !text.endsWith(getString(R.string.ErT))
		// && !text.endsWith(getString(R.string.IrT))) {
		// Toast.makeText(this, R.string.errorTermination,
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		if (!text.trim().equals("")) {
			if (!haveInternet()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ConxugaActivity.this);
				builder.setTitle(R.string.noDataConnection);
				builder.setMessage(R.string.noDataConnectionMessage);
				builder.setCancelable(false);
				builder.setPositiveButton(R.string.configureNetwork,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dlalog, int arg1) {
								startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
							}

						});
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dlalog, int arg1) {
						Toast.makeText(ConxugaActivity.this, R.string.noDataDecided,
								Toast.LENGTH_SHORT).show();
					}

				});
				builder.create().show();
			} else {
				new SearchConjugationTask(this) {

					@Override
					protected void onPostExecuteNotFound() {
						Toast.makeText(ConxugaActivity.this,
								ConxugaActivity.this.getString(R.string.errorNotFound, text),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					protected void onPostExecuteFound(VerbalTime[] verbalTimes) {
						try {
							if (!VolgaChecker.exists(ConxugaActivity.this, text)) {
								Toast.makeText(
										ConxugaActivity.this,
										ConxugaActivity.this.getString(R.string.verbDoesNotExist,
												text), Toast.LENGTH_LONG).show();
							}
						} catch (IOException e) {
							// Erro ao comprobar se existe, co ficheiro.
							// Non se fai nada
						}
						Intent i = new Intent(ConxugaActivity.this, Verbs.class);
						i.putExtra("verbalTimes", verbalTimes);
						i.putExtra("word", text);
						startActivity(i);
					}

					@Override
					protected void onPostExecuteConnectionError() {
						Toast.makeText(ConxugaActivity.this, getString(R.string.connectionError),
								Toast.LENGTH_SHORT).show();
					}

				}.execute(text);
			}
		}
	}

	protected boolean haveInternet() {
		NetworkInfo info = ((ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (info == null || !info.isConnectedOrConnecting()) {
			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to
			// disable internet while roaming, just return false
			return true;
		}
		return true;
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
	protected void onPause() {

		super.onPause();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor edit = prefs.edit();
		edit.putInt(VERSION, getPackageVersion());
		edit.commit();

	}

	private final int getPackageVersion() {

		try {

			PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);

			return pinfo.versionCode;

		} catch (NameNotFoundException e) {
			// Empty on purpose
		}

		return 0;

	}

	private final void showNews(LayoutInflater inflater, Context context) {

		View messageView = inflater.inflate(R.layout.news, null, false);

		TextView textView = (TextView) messageView.findViewById(R.id.news_title);
		textView.setTextColor(Color.WHITE);

		TextView textView2 = (TextView) messageView.findViewById(R.id.news_text);
		textView2.setTextColor(Color.WHITE);
		
		Linkify.addLinks(textView2, Linkify.ALL);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.app_name);
		builder.setView(messageView);
		builder.setPositiveButton(R.string.ok,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						((AlertDialog) dialog).getButton(which).setVisibility(View.INVISIBLE);
					}
				});
		builder.create();
		builder.show();
	}
}
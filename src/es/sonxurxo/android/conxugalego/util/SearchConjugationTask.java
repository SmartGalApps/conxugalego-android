package es.sonxurxo.android.conxugalego.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;
import es.sonxurxo.android.conxugalego.R;
import es.sonxurxo.android.conxugalego.model.VerbalTime;

public abstract class SearchConjugationTask extends AsyncTask<String, Void, String> {

	protected static final String SERVER_URL = "http://galapps.es/Conxugalego/conshuga.pl";

	private final ProgressDialog dialog;
	private final Context context;
	private String infinitive;

	public SearchConjugationTask(Context context) {
		super();
		this.context = context;
		this.dialog = new ProgressDialog(this.context);
		this.dialog.setCancelable(true);
		this.dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				SearchConjugationTask.this.cancel(true);
			}

		});
		this.dialog.setMessage(this.context.getString(R.string.loadingData));
	}

	@Override
	protected void onPreExecute() {
		this.dialog.show();
	}

	@Override
	protected String doInBackground(String... verbs) {
		this.infinitive = verbs[0];
		try {
			this.infinitive = URLEncoder.encode(this.infinitive, "UTF-8");
			URL url;

			url = new URL(SERVER_URL + "?" + this.infinitive.toLowerCase());
			
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			try {
				InputStream is = new BufferedInputStream(urlConnection.getInputStream());
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();

				urlConnection.disconnect();
				return sb.toString();
			} finally {
				urlConnection.disconnect();
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

	}

	@Override
	protected void onPostExecute(String result) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		if (result == null) {
			this.onPostExecuteConnectionError();
		}
		VerbalTime[] verbalTimes = this.parse(result);
		if (verbalTimes == null || verbalTimes.length == 0) {
			this.onPostExecuteNotFound();
		} else {
			this.onPostExecuteFound(verbalTimes);
		}
	}

	protected VerbalTime[] parse(String result) {
		String[] split1 = result.split("\n");

		int badTimes = 0;
		for (String s : split1) {
			if (s.equalsIgnoreCase(this.context.getString(R.string.naoReconhecido))) {
				badTimes++;
			}
		}

		VerbalTime[] theResult = new VerbalTime[split1.length - badTimes];

		for (int i = 0; i < split1.length; i++) {
			String verbalTimeString = split1[i];
			String[] split2 = verbalTimeString.split(":");
			String[] vTimes = new String[split2.length - 1];
			for (int j = 1; j < split2.length; j++) {
				vTimes[j - 1] = split2[j];
			}
			if (vTimes.length != 0) {
				VerbalTime verbalTime = new VerbalTime(split2[0], vTimes);
				theResult[i] = verbalTime;
			}
		}
		Arrays.sort(theResult);
		Cheater.cheat(this.context, this.infinitive, theResult);
		return theResult;
	}

	protected abstract void onPostExecuteConnectionError();

	protected abstract void onPostExecuteNotFound();

	protected abstract void onPostExecuteFound(VerbalTime[] verbalTimes);

	@Override
	protected void onCancelled() {
		super.onCancelled();
		Toast.makeText(this.context, R.string.cancelled, Toast.LENGTH_SHORT).show();
	}

}
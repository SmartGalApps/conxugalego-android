package es.sonxurxo.android.conxugalego.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import es.sonxurxo.android.conxugalego.R;

public class AboutDialog {

	public static AlertDialog create(Context context)
			throws NameNotFoundException {
		PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                context.getPackageName(), PackageManager.GET_META_DATA);

        String versionInfo = pInfo.versionName;

        View messageView = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.about, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name) + " " + versionInfo);
        builder.setView(messageView);
        builder.setPositiveButton(context.getString(android.R.string.ok), null);
        return builder.create();

	}
}
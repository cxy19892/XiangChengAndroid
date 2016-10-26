package com.yzm.sleep.activity;

import java.io.File;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;

public class XiaoMiAutostartActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiaomi_autostart);

		findViewById(R.id.btnAllowAutoStart).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						setAutoStart();
						AppManager.getAppManager().finishActivity();
					}
				});
		
	}

	private void setAutoStart() {
		try {
			try {
				Intent localIntent1 = new Intent();
				localIntent1.setAction("miui.intent.action.OP_AUTO_START");
				localIntent1.addCategory("android.intent.category.DEFAULT");
				this.startActivity(localIntent1);

			} catch (ActivityNotFoundException localActivityNotFoundException) {

				Intent localIntent = new Intent(
						"miui.intent.action.APP_PERM_EDITOR");
				localIntent
						.setClassName("com.miui.securitycenter",
								"com.miui.permcenter.autostart.AutoStartManagementActivity");
				localIntent.addCategory("android.intent.category.DEFAULT");
				localIntent.putExtra("extra_pkgname", getApplication()
						.getPackageName());
				this.startActivity(localIntent);

			}

		} catch (ActivityNotFoundException localActivityNotFoundException) {
			Intent intent = new Intent(
					Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts("package", getApplication()
					.getPackageName(), null);
			intent.setData(uri);
			startActivity(intent);
		}
	}

}

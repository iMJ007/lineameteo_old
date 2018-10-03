package com.robotemplates.webviewapp;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.onesignal.OneSignal;
import com.robotemplates.kozuza.BaseApplication;
import com.robotemplates.kozuza.Kozuza;
import com.robotemplates.webviewapp.listener.OneSignalNotificationOpenedHandler;

import org.alfonz.utility.Logcat;


public class WebViewAppApplication extends BaseApplication
{
	private Tracker mTracker;


	@Override
	public void onCreate()
	{
		super.onCreate();

		// initialize logcat
		Logcat.init(WebViewAppConfig.LOGS, "WEBVIEWAPP");

		// initialize OneSignal
		OneSignal.startInit(this).setNotificationOpenedHandler(new OneSignalNotificationOpenedHandler()).init();
		OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
	}


	@Override
	public String getPurchaseCode()
	{
		return WebViewAppConfig.PURCHASE_CODE;
	}


	@Override
	public String getProduct()
	{
		return Kozuza.PRODUCT_WEBVIEWAPP;
	}


	public synchronized Tracker getTracker()
	{
		if(mTracker == null)
		{
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			analytics.setDryRun(WebViewAppConfig.ANALYTICS_TRACKING_ID == null || WebViewAppConfig.ANALYTICS_TRACKING_ID.equals(""));
			mTracker = analytics.newTracker(R.xml.analytics_app_tracker);
			mTracker.set("&tid", WebViewAppConfig.ANALYTICS_TRACKING_ID);
		}
		return mTracker;
	}
}

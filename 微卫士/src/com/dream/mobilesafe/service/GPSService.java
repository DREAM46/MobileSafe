package com.dream.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.dream.mobilesafe.R;

public class GPSService extends Service {

	private MyLocationListener listener;
	private LocationManager lm;

	@Override
	public void onCreate() {
		super.onCreate();

		lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(criteria, true);

		listener = new MyLocationListener();
		lm.requestLocationUpdates(provider, 60 * 1000, 50, listener);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(listener);
		listener = null;
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {

			String longitude = location.getLongitude() + "";
			String latitude = location.getLatitude() + "";

			Context context = getApplicationContext();
			Resources resources = context.getResources();

			Editor editor = context.getSharedPreferences(
					resources.getString(R.string.pre_name),
					Context.MODE_PRIVATE).edit();
			editor.putString("address", longitude + "\t" + latitude);
			editor.commit();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}

}

package com.wugas.imaginecup;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceFragment;


/**
 * Created by suhon_000 on 12/17/2014.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String KEY_PREF_SERVICE = "service_pref";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREF_SERVICE)) {
            // if Service turned on
            if (sharedPreferences.getBoolean(key, false)) {
                getActivity().stopService(new Intent(getActivity(), LocationService.class));
            }
            else {
                getActivity().startService(new Intent(getActivity(), LocationService.class));
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

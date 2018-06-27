package com.google.developer.bugmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.google.developer.bugmaster.reminders.AlarmReceiver;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(changeListener);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(changeListener);
        super.onPause();
    }

    private SharedPreferences.OnSharedPreferenceChangeListener changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (getActivity() != null && (key.equals(getString(R.string.pref_key_reminder)) || key.equals(getString(R.string.pref_key_alarm)))) {
                AlarmReceiver receiver = new AlarmReceiver();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_BOOT_COMPLETED);
                receiver.onReceive(getActivity(), intent);
            }
        }
    };

}

package com.example.android.cookies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_visualizer);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        int preferenceCatergoryCount = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < preferenceCatergoryCount; i++) {
            PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.getPreference(i);
            int preferenceCount = preferenceCategory.getPreferenceCount();
            for (int j = 0; i < preferenceCount; i++) {
                Preference preference = preferenceCategory.getPreference(j);
                if (!(preference instanceof CheckBoxPreference)) {
                    String summary = sharedPreferences.getString(preference.getKey(), "");
                    setPreferenceSummary(preference, summary);
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_name_key))) {
            String summary = sharedPreferences.getString(getString(R.string.pref_name_key),"");
            Preference preference = findPreference(getString(R.string.pref_name_key));
            setPreferenceSummary(preference, summary);
        }

    }

    private void setPreferenceSummary(Preference preference, String summary) {
        if (preference instanceof EditTextPreference) {
            preference.setSummary(summary);
        }
    }
}

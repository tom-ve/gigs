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
        int preferenceCategoryCount = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < preferenceCategoryCount; i++) {
            PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.getPreference(i);
            int preferenceCount = preferenceCategory.getPreferenceCount();
            for (int j = 0; j < preferenceCount; j++) {
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
        Preference preference = findPreference(key);
        if (null != preference) {
            if (!(preference instanceof CheckBoxPreference)) {
                setPreferenceSummary(preference, sharedPreferences.getString(key, ""));
            }
        }
    }

    private void setPreferenceSummary(Preference preference, String summary) {
        if (preference instanceof EditTextPreference) {
            preference.setSummary(summary);
        } else if (preference instanceof ListPreference) {
            ListPreference lp = (ListPreference) preference;
            int prefIndex = lp.findIndexOfValue(summary);
            if (prefIndex >= 0) {
                lp.setSummary(lp.getEntries()[prefIndex]);
            }
        }
    }

}

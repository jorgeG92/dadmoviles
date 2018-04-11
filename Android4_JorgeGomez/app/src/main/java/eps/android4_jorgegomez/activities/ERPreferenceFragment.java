package eps.android4_jorgegomez.activities;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import eps.android4_jorgegomez.R;


public class ERPreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

    }


}

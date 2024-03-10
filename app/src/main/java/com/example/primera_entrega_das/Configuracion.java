package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
public class Configuracion extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_config, rootKey);

        // Configura el listener para detectar cambios en las preferencias
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Limpia el listener al destruir la actividad
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Maneja cambios en las preferencias y ajusta dinámicamente el tema
        if ("theme_preference".equals(key)) {
            String themeValue = sharedPreferences.getString(key, "light");
            int themeResourceId = "light".equals(themeValue) ? R.style.AppTheme_Light : R.style.AppTheme_Dark;

            getActivity().setTheme(themeResourceId);
            // Reinicia la actividad para aplicar el nuevo tema
            getActivity().recreate();
        }
        // Maneja otros cambios en las preferencias según sea necesario
    }
}
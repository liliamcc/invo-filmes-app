package com.androidpro.invo_filmes_app.Utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class Utils {
    public static void setLocaleLanguaje(Context context) {
        String languageToLoad = "pt";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }
}

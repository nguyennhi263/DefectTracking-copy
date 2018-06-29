package vn.com.ifca.defecttracking.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;


public class LanguagePf {
    private SharedPreferences langPreferences;
    private String sharedPrefFile = "vn.com.ifca.reportdashboard";
    Locale myLocale;
    public void initialize(Context context, Resources res){
        langPreferences = context.getSharedPreferences(sharedPrefFile, context.MODE_PRIVATE);
        String setLang = langPreferences.getString("language", "toast");
        if (!(setLang.equals("toast"))){
            changeLanguage(res, setLang);
        }
        else{
            changeLanguage(res,"en");
            updatePreferences("en");
        }
    }

    public void changeLanguage (Resources res, String locale) {
        Configuration config = new Configuration(res.getConfiguration());
        switch (locale) {
            case "en":
                config.locale = new Locale("en");
                break;
            case "vi":
                config.locale = new Locale("vi");
                break;
        }
        //update
        updatePreferences(locale);
        //display
        Locale myLocale = new Locale(locale);
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }
    public void updatePreferences (String locale)
    {
        SharedPreferences.Editor preferencesEditor = langPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.putString("language", locale);
        preferencesEditor.apply();
    }
    public String getPreferences() {
        return langPreferences.getString("language", "toast");
    }
}

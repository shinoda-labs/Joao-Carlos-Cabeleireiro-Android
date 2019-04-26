package com.shinodalabs.joaocarloscabeleireiro.Activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.shinodalabs.joaocarloscabeleireiro.BuildConfig;
import com.shinodalabs.joaocarloscabeleireiro.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.JOAO_EMAIL;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.JOAO_FACEBOOK;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.JOAO_INSTAGRAM;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.JOAO_PLAY_STORE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.JOAO_WHATS;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.SHINODA_LABS_SITE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.SHINODA_LABS_WHATS;

public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View aboutView = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logo)
                .setDescription(getString(R.string.about_desc))
                .addGroup(getString(R.string.contact))
                .addItem(new Element().setTitle(getString(R.string.talk_whats)).setIconDrawable(R.drawable.whatsapp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(JOAO_WHATS));
                        startActivity(intent);
                    }
                }))
                .addGroup(getString(R.string.conect_with_us))
                .addEmail(JOAO_EMAIL)
                .addWebsite(SHINODA_LABS_SITE)
                .addFacebook(JOAO_FACEBOOK)
                //.addYoutube("#########")
                .addPlayStore(JOAO_PLAY_STORE)
                .addInstagram(JOAO_INSTAGRAM)
                .addGroup(getString(R.string.version))
                .addItem(new Element().setTitle(versionName + " - " + String.valueOf(versionCode)).setIconDrawable(R.drawable.version))
                .addGroup(getString(R.string.developed))
                .addItem(new Element().setTitle(getString(R.string.shinoda_labs)).setIconDrawable(R.drawable.dev).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(SHINODA_LABS_WHATS));
                        startActivity(intent);
                    }
                }))
                .create();

        setContentView(R.layout.activity_about);
        LinearLayout mActivityRoot = ((LinearLayout) findViewById(R.id.main_view));
        mActivityRoot.addView(aboutView, 1);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setSubtitle(getString(R.string.about));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}

package com.shinodalabs.joaocarloscabeleireiro.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

public class AboutFragment extends Fragment {

    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        View aboutView = new AboutPage(getContext())
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

        FrameLayout mActivityRoot = v.findViewById(R.id.main_view);
        mActivityRoot.addView(aboutView, 1);

        return v;
    }

}

package com.shinodalabs.joaocarloscabeleireiro.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.shinodalabs.joaocarloscabeleireiro.R;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_SCHEDULE;

public class ViewScheduleClientActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule_client);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(R.string.schedule_subtitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getBundleIntent();

    }

    private void getBundleIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(ID_SCHEDULE);
            getSupportActionBar().setSubtitle(String.format("%s%s", getString(R.string.schedule_subtitle), id));
        }
    }
}

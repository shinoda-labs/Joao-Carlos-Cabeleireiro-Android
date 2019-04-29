package com.shinodalabs.joaocarloscabeleireiro.Activitys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shinodalabs.joaocarloscabeleireiro.Model.ScheduleUser;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Toasts;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.CANCELED;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.CONFIRMED;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.CONFIRMED_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DATE_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DONE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.NAME_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.NOT_CONFIRMED;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.PRICE_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.SCHEDULED;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.STATUS_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.TIME_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Url.URL_VIEW_CLIENT_SCHEDULE;

public class ViewScheduleClientActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView tvDetailSchedule, tvOptionsSchedule;
    private TextView tvServiceTitle, tvPriceTitle, tvDateTitle, tvTimeTitle, tvStatusTitle, tvConfirmTitle;
    private TextView tvService, tvPrice, tvDate, tvTime, tvStatus, tvConfirm;
    private Button btnCancelSchedule;

    private String id;

    private KProgressHUD dialog;

    private ScheduleUser scheduleUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule_client);

        castWidgets();
        setTypeface();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(R.string.schedule_subtitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getBundleIntent();

        getScheduleData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelSchedule:
                switch (scheduleUser.getStatus()) {
                    case SCHEDULED:
                        Toasts.toastSuccess(getApplicationContext(), getString(R.string.soon));
                        break;
                    case DONE:
                        Toasts.toastInfo(getApplicationContext(), getString(R.string.schedule_done_cancel_msg));
                        break;
                    case CANCELED:
                        Toasts.toastInfo(getApplicationContext(), getString(R.string.schedule_canceled_cancel_msg));
                        break;
                }
                break;
        }
    }

    private void getScheduleData() {
        createDialog(getString(R.string.please_wait), getString(R.string.loading));

        Ion.with(getApplicationContext())
                .load(URL_VIEW_CLIENT_SCHEDULE)
                .setBodyParameter(ID_SCHEDULE, id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {

                            scheduleUser = new ScheduleUser();
                            scheduleUser.setStatus(result.get(STATUS_SCHEDULE).getAsString());
                            scheduleUser.setDate(result.get(DATE_SCHEDULE).getAsString());
                            scheduleUser.setConfirmed(result.get(CONFIRMED_SCHEDULE).getAsString());
                            scheduleUser.setName(result.get(NAME_SERVICE).getAsString());
                            scheduleUser.setPrice(result.get(PRICE_SERVICE).getAsString());
                            scheduleUser.setTime(result.get(TIME_SCHEDULE).getAsString());

                            tvService.setText(scheduleUser.getName());
                            tvPrice.setText(String.format("R$%.2f", Double.parseDouble(scheduleUser.getPrice())));
                            tvDate.setText(scheduleUser.getDate());
                            tvTime.setText(scheduleUser.getTime());

                            switch (scheduleUser.getConfirmed()) {
                                case CONFIRMED:
                                    tvConfirm.setText(R.string.yes);
                                    break;
                                case NOT_CONFIRMED:
                                    tvConfirm.setText(R.string.no);
                                    break;
                            }

                            switch (scheduleUser.getStatus()) {
                                case SCHEDULED:
                                    tvStatus.setText(R.string.scheduled);
                                    break;
                                case CANCELED:
                                    tvStatus.setText(R.string.canceled);
                                    break;
                                case DONE:
                                    tvStatus.setText(R.string.done);
                                    break;
                            }

                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }

                        } catch (Exception x) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toasts.toastError(getApplicationContext(), getString(R.string.error_load_data) + "\n" + x.getMessage());
                        }
                    }
                });

    }

    private void getBundleIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(ID_SCHEDULE);
            getSupportActionBar().setSubtitle(String.format("%s%s", getString(R.string.schedule_subtitle), id));
        }
    }

    private void castWidgets() {
        toolbar = findViewById(R.id.toolbar);
        tvDetailSchedule = findViewById(R.id.tvDetailSchedule);
        tvOptionsSchedule = findViewById(R.id.tvOptionsSchedule);
        tvServiceTitle = findViewById(R.id.tvServiceTitle);
        tvPriceTitle = findViewById(R.id.tvPriceTitle);
        tvDateTitle = findViewById(R.id.tvDateTitle);
        tvTimeTitle = findViewById(R.id.tvTimeTitle);
        tvStatusTitle = findViewById(R.id.tvStatusTitle);
        tvConfirmTitle = findViewById(R.id.tvConfirmTitle);
        tvService = findViewById(R.id.tvService);
        tvPrice = findViewById(R.id.tvPrice);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvStatus = findViewById(R.id.tvStatus);
        tvConfirm = findViewById(R.id.tvConfirm);
        btnCancelSchedule = findViewById(R.id.btnCancelSchedule);
        btnCancelSchedule.setOnClickListener(this);
    }

    private void setTypeface() {
        tvDetailSchedule.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvOptionsSchedule.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvServiceTitle.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvPriceTitle.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvDateTitle.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvTimeTitle.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvStatusTitle.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvConfirmTitle.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvService.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvPrice.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvDate.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvTime.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvStatus.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvConfirm.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        btnCancelSchedule.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
    }

    private void createDialog(String title, String message) {
        dialog = KProgressHUD.create(ViewScheduleClientActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(title)
                .setDetailsLabel(message)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        dialog.show();
    }

}

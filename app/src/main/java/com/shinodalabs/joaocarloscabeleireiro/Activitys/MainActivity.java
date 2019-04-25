package com.shinodalabs.joaocarloscabeleireiro.Activitys;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shinodalabs.joaocarloscabeleireiro.BottomSheetDialogs.BottomSheetDialogService;
import com.shinodalabs.joaocarloscabeleireiro.BottomSheetDialogs.BottomSheetDialogTime;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Toasts;

import java.util.Calendar;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.AVAILABLE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DATE_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DATE_SCHEDULED;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_USER;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.RESULT;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.UNAVAILABLE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Url.URL_ADD_SCHEDULED_TIME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomSheetDialogService.BottomSheetListener, BottomSheetDialogTime.BottomSheetListener {

    private OptRoundCardView cvService, cvDate, cvTime;
    private LinearLayout llService, llDate, llTime;
    private ConstraintLayout tileImageService, tileImageDate, tileImageTime;
    private View sideService, sideDate, sideTime;
    private ImageView ivService, ivDate, ivTime;
    private TextView tvService, tvServiceSelected, tvDate, tvDateSelected, tvTime, tvTimeSelected;
    private Button btnSchedule;

    private String service = "";
    private String serviceName = "";
    private String date = "";
    private String dateName = "";
    private String time = "";
    private String timeName = "";

    private DatePickerDialog pickerDialog;
    private Calendar calendar;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private KProgressHUD dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        castWidgets();
        setTypeface();

    }

    private void setTypeface() {
        tvService.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvServiceSelected.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvDate.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvDateSelected.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvTime.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        tvTimeSelected.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
        btnSchedule.setTypeface(Fonts.TypefaceLight(getApplicationContext()));
    }

    private void castWidgets() {
        cvService = findViewById(R.id.cvService);
        cvDate = findViewById(R.id.cvDate);
        cvTime = findViewById(R.id.cvTime);
        llService = findViewById(R.id.llService);
        llDate = findViewById(R.id.llDate);
        llTime = findViewById(R.id.llTime);
        tileImageService = findViewById(R.id.tileImageService);
        tileImageDate = findViewById(R.id.tileImageDate);
        tileImageTime = findViewById(R.id.tileImageTime);
        sideService = findViewById(R.id.sideService);
        sideDate = findViewById(R.id.sideDate);
        sideTime = findViewById(R.id.sideTime);
        ivService = findViewById(R.id.ivService);
        ivDate = findViewById(R.id.ivDate);
        ivTime = findViewById(R.id.ivTime);
        tvService = findViewById(R.id.tvService);
        tvServiceSelected = findViewById(R.id.tvServiceSelected);
        tvDate = findViewById(R.id.tvDate);
        tvDateSelected = findViewById(R.id.tvDateSelected);
        tvTime = findViewById(R.id.tvTime);
        tvTimeSelected = findViewById(R.id.tvTimeSelected);
        btnSchedule = findViewById(R.id.btnSchedule);
        cvService.setOnClickListener(this);
        cvDate.setOnClickListener(this);
        cvTime.setOnClickListener(this);
        btnSchedule.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvService:
                BottomSheetDialogService bs = new BottomSheetDialogService();
                bs.show(getSupportFragmentManager(), "BottomSheetService");
                break;
            case R.id.cvDate:
                if (!service.equals("")) {
                    calendar = Calendar.getInstance();
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH + 1);
                    int year = calendar.get(Calendar.YEAR);

                    pickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            date = formatDateBackEnd(String.valueOf(year), String.valueOf(month + 1), String.valueOf(dayOfMonth));
                            dateName = formatDate(String.valueOf(year), String.valueOf(month + 1), String.valueOf(dayOfMonth));
                            showTileState(dateName, tileImageDate, sideDate, tvDateSelected);
                            SharedPreferences.Editor editor = getSharedPreferences(DATE_SCHEDULE, MODE_PRIVATE).edit();
                            editor.putString(DATE_SCHEDULE, date);
                            editor.apply();
                            hideTileState(tileImageTime, sideTime, tvTimeSelected);
                            time = "";
                        }
                    }, year, month, day);

                    pickerDialog.show();
                } else {
                    Toasts.toastInfo(getApplicationContext(), getString(R.string.select_date));
                }
                break;
            case R.id.cvTime:
                if (!date.equals("")) {
                    BottomSheetDialogTime bt = new BottomSheetDialogTime();
                    bt.show(getSupportFragmentManager(), "BottomSheetTime");
                } else {
                    Toasts.toastInfo(getApplicationContext(), getString(R.string.select_time));
                }
                break;
            case R.id.btnSchedule:
                if (service.equals("")) {
                    Toasts.toastInfo(getApplicationContext(), getString(R.string.choose_service));
                } else if (date.equals("")) {
                    Toasts.toastInfo(getApplicationContext(), getString(R.string.choose_date));
                } else if (time.equals("")) {
                    Toasts.toastInfo(getApplicationContext(), getString(R.string.choose_time));
                } else {
                    if (mUser != null) {
                        new MaterialStyledDialog.Builder(this)
                                .setTitle(getString(R.string.schedule_time))
                                .setIcon(R.drawable.cut)
                                .setDescription(String.format("%s\n\n%s %s\n%s %s\n%s %s\n%s %s\n\n%s", getString(R.string.verify_correct_data), getString(R.string.client_show), mUser.getDisplayName(), getString(R.string.service_show), serviceName, getString(R.string.date_show), dateName, getString(R.string.time_show), timeName, getString(R.string.data_correct)))
                                .setPositiveText(getString(R.string.confirm))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        scheduleTime(mAuth.getCurrentUser().getUid(), service, date, time);
                                    }
                                })
                                .setNegativeText(getString(R.string.cancel))
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    } else {
                        new MaterialStyledDialog.Builder(this)
                                .setTitle(getString(R.string.not_loged))
                                .setIcon(R.drawable.login)
                                .setDescription(getString(R.string.not_loged_msg))
                                .setPositiveText(getString(R.string.ok_log_in))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    private void scheduleTime(String uid, String service_selected, String date_selected, String time_selected) {
        createDialog(getString(R.string.please_wait), getString(R.string.saving_data));

        Ion.with(getBaseContext())
                .load(URL_ADD_SCHEDULED_TIME)
                .setBodyParameter(ID_USER, uid)
                .setBodyParameter(ID_SERVICE, service_selected)
                .setBodyParameter(DATE_SCHEDULED, date_selected)
                .setBodyParameter(ID_SCHEDULE, time_selected)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject r) {
                        try {
                            String result = r.get(RESULT).getAsString();

                            if (result.equals(AVAILABLE)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                hideTileState(tileImageService, sideService, tvServiceSelected);
                                hideTileState(tileImageDate, sideDate, tvDateSelected);
                                hideTileState(tileImageTime, sideTime, tvTimeSelected);
                                service = "";
                                serviceName = "";
                                date = "";
                                dateName = "";
                                time = "";
                                timeName = "";
                                Toasts.toastSuccess(getApplicationContext(), mUser.getDisplayName() + getString(R.string.schedule_success));
                            } else {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Toasts.toastError(getApplicationContext(), getString(R.string.error_save_data));
                            }

                        } catch (Exception x) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toasts.toastError(getApplicationContext(), x.getMessage());
                        }
                    }
                });

    }

    @Override
    public void onServiceSelected(String id, String name) {
        service = id;
        serviceName = name;
        showTileState(name, tileImageService, sideService, tvServiceSelected);
        hideTileState(tileImageDate, sideDate, tvDateSelected);
        hideTileState(tileImageTime, sideTime, tvTimeSelected);
        date = "";
        time = "";
    }

    @Override
    public void onTimeSelected(String id, String time, String result) {
        if (result.equals(AVAILABLE)) {
            this.time = id;
            timeName = time;
            showTileState(time, tileImageTime, sideTime, tvTimeSelected);
        } else if (result.equals(UNAVAILABLE)) {
            Toasts.toastInfo(getApplicationContext(), getString(R.string.unavailable_time));
        }
    }

    private String formatDateBackEnd(String year, String month, String day) {
        if (Integer.parseInt(day) < 10) {
            day = "0" + day;
        }
        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }
        return year + "-" + month + "-" + day;
    }

    private String formatDate(String year, String month, String day) {
        if (Integer.parseInt(day) < 10) {
            day = "0" + day;
        }
        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }
        return day + "/" + month + "/" + year;
    }

    private void showTileState(String name, ConstraintLayout tileImage, View side, TextView text) {
        tileImage.setBackgroundColor(getColor(R.color.colorGreen));
        side.setBackgroundColor(getColor(R.color.colorYellow));
        side.setVisibility(View.VISIBLE);
        text.setText(name);
        text.setVisibility(View.VISIBLE);
    }

    private void hideTileState(ConstraintLayout tileImage, View side, TextView text) {
        tileImage.setBackgroundColor(getColor(R.color.colorYellow));
        side.setVisibility(View.GONE);
        text.setText(null);
        text.setVisibility(View.GONE);
    }

    private void createDialog(String title, String message) {
        dialog = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(title)
                .setDetailsLabel(message)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        dialog.show();
    }

}





package com.shinodalabs.joaocarloscabeleireiro.Fragments.UserFragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shinodalabs.joaocarloscabeleireiro.Adapter.ServicesAdapter;
import com.shinodalabs.joaocarloscabeleireiro.Adapter.TimeAdapter;
import com.shinodalabs.joaocarloscabeleireiro.Model.Services;
import com.shinodalabs.joaocarloscabeleireiro.Model.Time;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Const;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Toasts;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Url;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DATE_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DATE_SCHEDULED;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_USER;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.RESULT;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.TIME;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.URL_200;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.URL_400;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.URL_404;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Url.URL_ADD_SCHEDULED_TIME;

public class ScheduleTimeFragment extends Fragment implements View.OnClickListener {

    private View v;

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

    private FragmentActivity myContext;
    private FragmentManager fragManager;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private KProgressHUD dialog;

    private ServicesAdapter adapterService;
    private List<Services> servicesList;

    private TimeAdapter adapterTime;
    private List<Time> timeList;

    public ScheduleTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_schedule_time, container, false);

        mAuth = FirebaseAuth.getInstance();

        fragManager = myContext.getSupportFragmentManager();

        castWidgets();
        setTypeface();

        return v;
    }

    private void setTypeface() {
        tvService.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tvServiceSelected.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tvDate.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tvDateSelected.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tvTime.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tvTimeSelected.setTypeface(Fonts.TypefaceLight(v.getContext()));
        btnSchedule.setTypeface(Fonts.TypefaceLight(v.getContext()));
    }

    private void castWidgets() {
        cvService = v.findViewById(R.id.cvService);
        cvDate = v.findViewById(R.id.cvDate);
        cvTime = v.findViewById(R.id.cvTime);
        llService = v.findViewById(R.id.llService);
        llDate = v.findViewById(R.id.llDate);
        llTime = v.findViewById(R.id.llTime);
        tileImageService = v.findViewById(R.id.tileImageService);
        tileImageDate = v.findViewById(R.id.tileImageDate);
        tileImageTime = v.findViewById(R.id.tileImageTime);
        sideService = v.findViewById(R.id.sideService);
        sideDate = v.findViewById(R.id.sideDate);
        sideTime = v.findViewById(R.id.sideTime);
        ivService = v.findViewById(R.id.ivService);
        ivDate = v.findViewById(R.id.ivDate);
        ivTime = v.findViewById(R.id.ivTime);
        tvService = v.findViewById(R.id.tvService);
        tvServiceSelected = v.findViewById(R.id.tvServiceSelected);
        tvDate = v.findViewById(R.id.tvPrice);
        tvDateSelected = v.findViewById(R.id.tvDateSelected);
        tvTime = v.findViewById(R.id.tvTime);
        tvTimeSelected = v.findViewById(R.id.tvTimeSelected);
        btnSchedule = v.findViewById(R.id.btnSchedule);
        cvService.setOnClickListener(this);
        cvDate.setOnClickListener(this);
        cvTime.setOnClickListener(this);
        btnSchedule.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvService:
                openService();
                break;
            case R.id.cvDate:
                openDate();
                break;
            case R.id.cvTime:
                if (!date.equals("")) {
                    openTime();
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
                    new MaterialStyledDialog.Builder(getContext())
                            .setTitle(getString(R.string.scedule_time))
                            .setIcon(R.drawable.cut)
                            .setDescription(String.format("%s\n\n%s %s\n%s %s\n%s %s\n%s %s\n\n%s", getString(R.string.verify_correct_data), getString(R.string.client_show), mUser.getDisplayName(), getString(R.string.service_show), serviceName, getString(R.string.date_show), dateName, getString(R.string.time_show), timeName, getString(R.string.data_correct)))
                            .setPositiveText(getString(R.string.confirm))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    scheduleTime(mUser.getUid(), service, date, time);
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
                }
                break;
        }
    }

    @Override
    public void onResume() {
        mUser = mAuth.getCurrentUser();
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    private void openDate() {
        if (!service.equals("")) {
            calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH + 1);
            int year = calendar.get(Calendar.YEAR);

            pickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    date = formatDateBackEnd(String.valueOf(year), String.valueOf(month + 1), String.valueOf(dayOfMonth));
                    dateName = formatDate(String.valueOf(year), String.valueOf(month + 1), String.valueOf(dayOfMonth));
                    showTileState(dateName, tileImageDate, sideDate, tvDateSelected);
                    hideTileState(tileImageTime, sideTime, tvTimeSelected);
                    time = "";
                }
            }, year - 1, month - 3, day);

            pickerDialog.show();
        } else {
            Toasts.toastInfo(getContext(), getString(R.string.select_date));
        }
    }

    private void openService() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_service, null);

        ListView lstService = v.findViewById(R.id.lstService);
        TextView tvServices = v.findViewById(R.id.tvServices);
        final ProgressBar pbLoading = v.findViewById(R.id.pbLoaging);

        tvServices.setTypeface(Fonts.TypefaceLight(v.getContext()));

        servicesList = new ArrayList<Services>();
        adapterService = new ServicesAdapter(getActivity(), servicesList);

        lstService.setAdapter(adapterService);

        servicesList.clear();

        lstService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Services services = (Services) parent.getAdapter().getItem(position);

                service = services.getId();
                serviceName = services.getName();
                showTileState(serviceName, tileImageService, sideService, tvServiceSelected);
                hideTileState(tileImageDate, sideDate, tvDateSelected);
                hideTileState(tileImageTime, sideTime, tvTimeSelected);
                date = "";
                time = "";

                dialogBuilder.dismiss();
            }
        });

        Ion.with(v.getContext())
                .load(Url.URL_SERVICES)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            pbLoading.setVisibility(View.GONE);
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject jsonObject = result.get(i).getAsJsonObject();

                                Services services = new Services();
                                services.setName(jsonObject.get(Const.NAME).getAsString());
                                services.setImage(jsonObject.get(Const.IMAGE).getAsString());
                                services.setPrice(jsonObject.get(Const.PRICE).getAsString());
                                services.setTime(jsonObject.get(Const.TIME).getAsString());
                                services.setId(jsonObject.get(Const.ID).getAsString());
                                services.setResult(jsonObject.get(Const.RESULT).getAsString());

                                servicesList.add(services);
                            }

                            adapterService.notifyDataSetChanged();
                        } catch (Exception x) {
                            Log.e("Error", x.getMessage());
                        }
                    }
                });

        dialogBuilder.setView(v);
        dialogBuilder.show();

    }

    private void openTime() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_time, null);

        TextView tvTime = v.findViewById(R.id.tvTime);
        GridView gvTime = v.findViewById(R.id.gvTime);
        final ProgressBar pbLoading = v.findViewById(R.id.pbLoaging);
        tvTime.setTypeface(Fonts.TypefaceLight(v.getContext()));

        timeList = new ArrayList<Time>();
        adapterTime = new TimeAdapter(getActivity(), timeList);

        gvTime.setAdapter(adapterTime);

        timeList.clear();

        gvTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Time times = (Time) parent.getAdapter().getItem(position);

                if (times.getResult().equals(URL_200)) {
                    time = times.getId();
                    timeName = times.getTime();
                    showTileState(timeName, tileImageTime, sideTime, tvTimeSelected);
                } else if (times.getResult().equals(URL_404)) {
                    Toasts.toastInfo(getContext(), getString(R.string.unavailable_time));
                }

                dialogBuilder.dismiss();
            }
        });

        Ion.with(getContext())
                .load(Url.URL_PICK_SCHEDULE)
                .setBodyParameter(DATE_SCHEDULE, date)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            pbLoading.setVisibility(View.GONE);
                            for (int i = 0; i < result.size(); i++) {

                                JsonObject jsonObject = result.get(i).getAsJsonObject();

                                Time time = new Time();
                                time.setId(jsonObject.get(ID).getAsString());
                                time.setResult(jsonObject.get(RESULT).getAsString());
                                time.setTime(jsonObject.get(TIME).getAsString());

                                timeList.add(time);
                            }
                            adapterTime.notifyDataSetChanged();
                        } catch (Exception x) {
                            Log.e("Error", x.getMessage());
                        }
                    }
                });

        dialogBuilder.setView(v);
        dialogBuilder.show();

    }

    private void scheduleTime(String uid, String service_selected, String date_selected, String time_selected) {
        createDialog(getString(R.string.please_wait), getString(R.string.saving_data));

        Ion.with(getContext())
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

                            if (result.equals(URL_200)) {
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
                                Toasts.toastSuccess(getContext(), mUser.getDisplayName() + getString(R.string.schedule_success));
                            } else if (result.equals(URL_400)) {
                                new MaterialStyledDialog.Builder(v.getContext())
                                        .setTitle(getString(R.string.ops))
                                        .setIcon(R.drawable.fast)
                                        .setDescription(R.string.ops_msg)
                                        .setPositiveText(getString(R.string.undertand))
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog d, @NonNull DialogAction which) {
                                                d.dismiss();
                                                dialog.dismiss();
                                                hideTileState(tileImageTime, sideTime, tvTimeSelected);
                                                time = "";
                                                timeName = "";
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            } else {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Toasts.toastError(getContext(), getString(R.string.error_save_data));
                            }

                        } catch (Exception x) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toasts.toastError(getContext(), x.getMessage());
                        }
                    }
                });

    }

    private void showTileState(String name, ConstraintLayout tileImage, View side, TextView text) {
        tileImage.setBackgroundColor(getActivity().getColor(R.color.colorGreen));
        side.setBackgroundColor(getActivity().getColor(R.color.colorYellow));
        side.setVisibility(View.VISIBLE);
        text.setText(name);
        text.setVisibility(View.VISIBLE);
    }

    private void hideTileState(ConstraintLayout tileImage, View side, TextView text) {
        tileImage.setBackgroundColor(getActivity().getColor(R.color.colorYellow));
        side.setVisibility(View.GONE);
        text.setText(null);
        text.setVisibility(View.GONE);
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

    private void createDialog(String title, String message) {
        dialog = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(title)
                .setDetailsLabel(message)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        dialog.show();
    }

}

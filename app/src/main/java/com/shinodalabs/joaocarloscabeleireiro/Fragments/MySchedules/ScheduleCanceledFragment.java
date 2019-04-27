package com.shinodalabs.joaocarloscabeleireiro.Fragments.MySchedules;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shinodalabs.joaocarloscabeleireiro.Adapter.AllScheduleUserAdapter;
import com.shinodalabs.joaocarloscabeleireiro.Model.AllScheduleUser;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Toasts;

import java.util.ArrayList;
import java.util.List;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DATE_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_USER;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.IMAGE_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.NAME_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.PRICE_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.STATUS_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.TIME_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Url.URL_PREVIEW_ALL_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Url.URL_PREVIEW_CANCELED_SCHEDULE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleCanceledFragment extends Fragment {

    private ListView lstSchedule;
    private ProgressBar pbLoaging;
    private TextView tvNoData;
    private AllScheduleUserAdapter adapter;
    private List<AllScheduleUser> allScheduleUserList;
    private FirebaseAuth mAuth;

    public ScheduleCanceledFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_schedule_canceled, container, false);

        mAuth = FirebaseAuth.getInstance();

        lstSchedule = v.findViewById(R.id.lstSchedule);
        pbLoaging = v.findViewById(R.id.pbLoading);
        tvNoData = v.findViewById(R.id.tvNoData);
        tvNoData.setTypeface(Fonts.TypefaceLight(v.getContext()));

        allScheduleUserList = new ArrayList<AllScheduleUser>();
        adapter = new AllScheduleUserAdapter(getActivity(), allScheduleUserList);

        lstSchedule.setAdapter(adapter);

        allScheduleUserList.clear();

        Ion.with(v.getContext())
                .load(URL_PREVIEW_CANCELED_SCHEDULE)
                .setBodyParameter(ID_USER, mAuth.getCurrentUser().getUid())
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (result.size() == 0) {
                                tvNoData.setVisibility(View.VISIBLE);
                            } else {
                                for (int i = 0; i < result.size(); i++) {
                                    JsonObject jsonObject = result.get(i).getAsJsonObject();

                                    AllScheduleUser all = new AllScheduleUser();
                                    all.setId(jsonObject.get(ID_SCHEDULE).getAsString());
                                    all.setImage(jsonObject.get(IMAGE_SERVICE).getAsString());
                                    all.setName(jsonObject.get(NAME_SERVICE).getAsString());
                                    all.setPrice(jsonObject.get(PRICE_SERVICE).getAsString());
                                    all.setDate(jsonObject.get(DATE_SCHEDULE).getAsString());
                                    all.setTime(jsonObject.get(TIME_SCHEDULE).getAsString());
                                    all.setStatus(jsonObject.get(STATUS_SCHEDULE).getAsString());

                                    allScheduleUserList.add(all);
                                }
                            }

                            pbLoaging.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } catch (Exception x) {
                            pbLoaging.setVisibility(View.GONE);
                            Toasts.toastError(v.getContext(), x.getMessage());
                        }
                    }
                });

        return v;
    }

}

package com.shinodalabs.joaocarloscabeleireiro.Fragments.MySchedules;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shinodalabs.joaocarloscabeleireiro.Activitys.ViewScheduleClientActivity;
import com.shinodalabs.joaocarloscabeleireiro.Adapter.ScheduleUserAdapter;
import com.shinodalabs.joaocarloscabeleireiro.Model.ScheduleUser;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Toasts;

import java.util.ArrayList;
import java.util.List;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DATE_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DONE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_USER;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.IMAGE_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.NAME_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.PRICE_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.STATUS_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.TIME_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Url.URL_PREVIEW_CLIENT_SCHEDULE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleDoneFragment extends Fragment {

    private ListView lstSchedule;
    private ProgressBar pbLoaging;
    private TextView tvNoData;
    private ScheduleUserAdapter adapter;
    private List<ScheduleUser> scheduleUserList;
    private FirebaseAuth mAuth;

    public ScheduleDoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =  inflater.inflate(R.layout.fragment_schedule_done, container, false);

        mAuth = FirebaseAuth.getInstance();

        lstSchedule = v.findViewById(R.id.lstSchedule);
        pbLoaging = v.findViewById(R.id.pbLoading);
        tvNoData = v.findViewById(R.id.tvNoData);
        tvNoData.setTypeface(Fonts.TypefaceLight(v.getContext()));

        scheduleUserList = new ArrayList<ScheduleUser>();
        adapter = new ScheduleUserAdapter(getActivity(), scheduleUserList);

        lstSchedule.setAdapter(adapter);

        scheduleUserList.clear();

        Ion.with(v.getContext())
                .load(URL_PREVIEW_CLIENT_SCHEDULE)
                .setBodyParameter(ID_USER, mAuth.getCurrentUser().getUid())
                .setBodyParameter(STATUS_SCHEDULE, DONE)
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

                                    ScheduleUser all = new ScheduleUser();
                                    all.setId(jsonObject.get(ID_SCHEDULE).getAsString());
                                    all.setImage(jsonObject.get(IMAGE_SERVICE).getAsString());
                                    all.setName(jsonObject.get(NAME_SERVICE).getAsString());
                                    all.setPrice(jsonObject.get(PRICE_SERVICE).getAsString());
                                    all.setDate(jsonObject.get(DATE_SCHEDULE).getAsString());
                                    all.setTime(jsonObject.get(TIME_SCHEDULE).getAsString());
                                    all.setStatus(jsonObject.get(STATUS_SCHEDULE).getAsString());

                                    scheduleUserList.add(all);
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

        lstSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScheduleUser user = (ScheduleUser) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getContext(), ViewScheduleClientActivity.class);
                intent.putExtra(ID_SCHEDULE, user.getId());
                startActivity(intent);
            }
        });

        return v;
    }

}

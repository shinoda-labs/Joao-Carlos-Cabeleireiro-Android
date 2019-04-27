package com.shinodalabs.joaocarloscabeleireiro.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shinodalabs.joaocarloscabeleireiro.Adapter.AllScheduleUserAdapter;
import com.shinodalabs.joaocarloscabeleireiro.Model.AllScheduleUser;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Toasts;

import java.util.ArrayList;
import java.util.List;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DATE_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_USER;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.IMAGE_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.NAME;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.NAME_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.PRICE_SERVICE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.TIME_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Url.URL_PREVIEW_SCHEDULE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleAllFragment extends Fragment {

    private ListView lstSchedule;
    private ProgressBar pbLoaging;
    private AllScheduleUserAdapter adapter;
    private List<AllScheduleUser> allScheduleUserList;
    private FirebaseAuth mAuth;
    private KProgressHUD dialog;

    public ScheduleAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_schedule_all, container, false);

        mAuth = FirebaseAuth.getInstance();

        lstSchedule = v.findViewById(R.id.lstSchedule);
        pbLoaging = v.findViewById(R.id.pbLoading);

        allScheduleUserList = new ArrayList<AllScheduleUser>();
        adapter = new AllScheduleUserAdapter(getActivity(), allScheduleUserList);

        lstSchedule.setAdapter(adapter);

        allScheduleUserList.clear();

        Ion.with(v.getContext())
                .load(URL_PREVIEW_SCHEDULE)
                .setBodyParameter(ID_USER, mAuth.getCurrentUser().getUid())
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject jsonObject = result.get(i).getAsJsonObject();

                                AllScheduleUser all = new AllScheduleUser();
                                all.setId(jsonObject.get(ID_SCHEDULE).getAsString());
                                all.setImage(jsonObject.get(IMAGE_SERVICE).getAsString());
                                all.setName(jsonObject.get(NAME_SERVICE).getAsString());
                                all.setPrice(jsonObject.get(PRICE_SERVICE).getAsString());
                                all.setDate(jsonObject.get(DATE_SCHEDULE).getAsString());
                                all.setTime(jsonObject.get(TIME_SCHEDULE).getAsString());

                                allScheduleUserList.add(all);
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

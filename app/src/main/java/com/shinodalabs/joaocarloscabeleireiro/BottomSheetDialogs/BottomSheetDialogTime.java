package com.shinodalabs.joaocarloscabeleireiro.BottomSheetDialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shinodalabs.joaocarloscabeleireiro.Adapter.TimeAdapter;
import com.shinodalabs.joaocarloscabeleireiro.Model.Time;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Url;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DATE_SCHEDULE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.RESULT;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.TIME;

public class BottomSheetDialogTime extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;
    private TimeAdapter adapter;
    private List<Time> timeList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_time, container, false);

        TextView tvTime = v.findViewById(R.id.tvTime);
        GridView gvTime = v.findViewById(R.id.gvTime);
        tvTime.setTypeface(Fonts.TypefaceLight(v.getContext()));

        timeList = new ArrayList<Time>();
        adapter = new TimeAdapter(getActivity(), timeList);

        gvTime.setAdapter(adapter);

        gvTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Time time = (Time) parent.getAdapter().getItem(position);
                mListener.onTimeSelected(time.getId(), time.getTime(), time.getResult());
                dismiss();
            }
        });

        SharedPreferences prefs = getContext().getSharedPreferences(DATE_SCHEDULE, MODE_PRIVATE);

        Ion.with(getContext())
                .load(Url.URL_PICK_SCHEDULE)
                .setBodyParameter(DATE_SCHEDULE, prefs.getString(DATE_SCHEDULE, null))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            for (int i = 0; i < result.size(); i++) {

                                JsonObject jsonObject = result.get(i).getAsJsonObject();

                                Time time = new Time();
                                time.setId(jsonObject.get(ID).getAsString());
                                time.setResult(jsonObject.get(RESULT).getAsString());
                                time.setTime(jsonObject.get(TIME).getAsString());

                                timeList.add(time);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception x) {
                            Log.e("Error", x.getMessage());
                        }
                    }
                });

        return v;
    }

    public interface BottomSheetListener {
        void onTimeSelected(String id, String time, String result);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetDialogTime.BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement BottomSheetListener");
        }
    }
}

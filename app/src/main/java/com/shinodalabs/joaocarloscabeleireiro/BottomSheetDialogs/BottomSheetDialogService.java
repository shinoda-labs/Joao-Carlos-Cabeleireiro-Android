package com.shinodalabs.joaocarloscabeleireiro.BottomSheetDialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shinodalabs.joaocarloscabeleireiro.Adapter.ServicesAdapter;
import com.shinodalabs.joaocarloscabeleireiro.Model.Services;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Const;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Url;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetDialogService extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;
    private ServicesAdapter adapter;
    private List<Services> servicesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_service, container, false);

        ListView lstService = v.findViewById(R.id.lstService);
        TextView tvServices = v.findViewById(R.id.tvServices);

        tvServices.setTypeface(Fonts.TypefaceLight(v.getContext()));

        servicesList = new ArrayList<Services>();
        adapter = new ServicesAdapter(getActivity(), servicesList);

        lstService.setAdapter(adapter);

        lstService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Services services = (Services) parent.getAdapter().getItem(position);
                mListener.onServiceSelected(services.getId(), services.getName());
                dismiss();
            }
        });

        Ion.with(v.getContext())
                .load(Url.URL_SERVICES)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
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

                            adapter.notifyDataSetChanged();
                        } catch (Exception x) {
                            Log.e("Error", x.getMessage());
                        }
                    }
                });

        return v;
    }

    public interface BottomSheetListener {
        void onServiceSelected(String id, String name);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement BottomSheetListener");
        }
    }
}

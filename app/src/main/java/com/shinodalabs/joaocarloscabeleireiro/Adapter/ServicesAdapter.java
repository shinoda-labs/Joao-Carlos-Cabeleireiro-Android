package com.shinodalabs.joaocarloscabeleireiro.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinodalabs.joaocarloscabeleireiro.Model.Services;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ServicesAdapter extends BaseAdapter {

    private Context context;
    private List<Services> servicesList;

    public ServicesAdapter(Context c, List<Services> l) {
        this.context = c;
        this.servicesList = l;
    }

    @Override
    public int getCount() {
        return servicesList.size();
    }

    @Override
    public Object getItem(int position) {
        return servicesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(R.layout.services_tile, null);
        } else {
            v = convertView;
        }

        Services services = (Services) getItem(position);

        ImageView ivIcon = v.findViewById(R.id.ivIcon);
        TextView tvName = v.findViewById(R.id.tvName);
        TextView tvTime = v.findViewById(R.id.tvTime);
        TextView tvPrice = v.findViewById(R.id.tvPrice);

        tvName.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tvPrice.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tvTime.setTypeface(Fonts.TypefaceLight(v.getContext()));

        Picasso.get().load(services.getImage()).into(ivIcon);
        tvName.setText(services.getName());
        tvPrice.setText(String.format("R$ %.2f", Double.parseDouble(services.getPrice())));
        tvTime.setText(String.format("%s Min", services.getTime()));

        return v;
    }
}

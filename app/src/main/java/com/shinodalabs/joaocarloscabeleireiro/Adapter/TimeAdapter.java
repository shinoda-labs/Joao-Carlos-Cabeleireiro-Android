package com.shinodalabs.joaocarloscabeleireiro.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shinodalabs.joaocarloscabeleireiro.Model.Time;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;

import java.util.List;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.AVAILABLE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.UNAVAILABLE;

public class TimeAdapter extends BaseAdapter {

    private Context context;
    private List<Time> timeList;

    public TimeAdapter(Context c, List<Time> l) {
        this.context = c;
        this.timeList = l;
    }

    @Override
    public int getCount() {
        return timeList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        View v = null;

        final Time time = (Time) getItem(position);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if (convertView == null) {

            if (time.getResult().equals(AVAILABLE)) {
                v = inflater.inflate(R.layout.time_tile_green, null);
            } else {
                v = inflater.inflate(R.layout.time_tile_red, null);
            }

            TextView tvTitle = v.findViewById(R.id.tvTitle);

            tvTitle.setTypeface(Fonts.TypefaceLight(v.getContext()));
            tvTitle.setText(time.getTime());

        } else {
            v = convertView;
        }

        return v;
    }

}

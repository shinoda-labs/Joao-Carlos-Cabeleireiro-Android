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

import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.shinodalabs.joaocarloscabeleireiro.Model.ScheduleUser;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.DONE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.SCHEDULED;

public class ScheduleUserAdapter extends BaseAdapter {

    private Context context;
    private List<ScheduleUser> scheduleUsersList;

    public ScheduleUserAdapter(Context c, List<ScheduleUser> l) {
        this.context = c;
        this.scheduleUsersList = l;
    }

    @Override
    public int getCount() {
        return scheduleUsersList.size();
    }

    @Override
    public Object getItem(int position) {
        return scheduleUsersList.get(position);
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
            v = inflater.inflate(R.layout.all_schedule_user_tile, null);
        } else {
            v = convertView;
        }

        ScheduleUser all = (ScheduleUser) getItem(position);

        ImageView tvImage = v.findViewById(R.id.ivImage);
        TextView tvService = v.findViewById(R.id.tvService);
        TextView tvPrice = v.findViewById(R.id.tvPrice);
        TextView tvDate = v.findViewById(R.id.tvDate);
        TextView tvTime = v.findViewById(R.id.tvTime);
        View vStatus = v.findViewById(R.id.vStatus);

        tvService.setTypeface(Fonts.TypefaceBold(v.getContext()));
        tvPrice.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tvDate.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tvTime.setTypeface(Fonts.TypefaceLight(v.getContext()));

        switch (all.getStatus()) {
            case SCHEDULED:
                vStatus.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
                break;
            case DONE:
                vStatus.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                break;
        }

        Picasso.get().load(all.getImage()).into(tvImage);
        tvService.setText(all.getName());
        tvPrice.setText(String.format("Valor: R$%.2f", Double.parseDouble(all.getPrice())));
        tvDate.setText(String.format("Dia: %s", DateTimeUtils.formatWithPattern(all.getDate(), "dd/MM/yyyy")));
        tvTime.setText(String.format("Horário: %s", all.getTime()));

        return v;
    }
}

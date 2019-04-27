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

import com.shinodalabs.joaocarloscabeleireiro.Model.AllScheduleUser;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllScheduleUserAdapter extends BaseAdapter {

    private Context context;
    private List<AllScheduleUser> allScheduleUsersList;

    public AllScheduleUserAdapter(Context c, List<AllScheduleUser> l) {
        this.context = c;
        this.allScheduleUsersList = l;
    }

    @Override
    public int getCount() {
        return allScheduleUsersList.size();
    }

    @Override
    public Object getItem(int position) {
        return allScheduleUsersList.get(position);
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

        AllScheduleUser all = (AllScheduleUser) getItem(position);

        ImageView tvImage = v.findViewById(R.id.ivImage);
        TextView tvService = v.findViewById(R.id.tvService);
        TextView tvPrice = v.findViewById(R.id.tvPrice);
        TextView tvDate = v.findViewById(R.id.tvDate);
        TextView tvTime = v.findViewById(R.id.tvTime);

        tvService.setTypeface(Fonts.TypefaceBold(v.getContext()));
        tvPrice.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tvDate.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tvTime.setTypeface(Fonts.TypefaceLight(v.getContext()));

        Picasso.get().load(all.getImage()).into(tvImage);
        tvService.setText(all.getName());
        tvPrice.setText(String.format("Valor: R$%.2f", Double.parseDouble(all.getPrice())));
        tvDate.setText(String.format("Dia: %s", all.getDate()));
        tvTime.setText(String.format("Horário: %s", all.getTime()));

        return v;
    }
}

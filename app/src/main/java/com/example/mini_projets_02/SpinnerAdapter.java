package com.example.mini_projets_02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mini_projets_02.models.BgColor;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<BgColor> bgColors;


    public SpinnerAdapter(Context context, ArrayList<BgColor> bgColors) {
        this.context = context;
        this.bgColors = bgColors;
    }

    @Override
    public int getCount() {
        return bgColors.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public int getBgColorItemPostion(String bgColorName) {
        int position = 0;
        for (int i = 0; i < bgColors.size(); i++) {
            if (bgColorName.equals(bgColors.get(i).getName())) {
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item_bg_color, viewGroup, false);
        }

        BgColor bgColor = bgColors.get(i);

        TextView tv_spinnerItemColorName = view.findViewById(R.id.tv_spinnerItemColorName);
        TextView tv_spinnerItemColorCode = view.findViewById(R.id.tv_spinnerItemColorCode);

        tv_spinnerItemColorName.setText(bgColor.getName());
        tv_spinnerItemColorCode.setText(bgColor.getCode());

        try (SettingsDbOpenHelper settingDb = new SettingsDbOpenHelper(context)) {
            tv_spinnerItemColorCode.setBackgroundColor(settingDb.getBgColorCode(bgColor.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}

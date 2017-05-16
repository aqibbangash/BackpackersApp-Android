package com.kit.backpackers.project_kit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kit.backpackers.project_kit.Models.Expedition;
import com.kit.backpackers.project_kit.R;

import java.util.List;

/**
 * Created by Rehan on 4/24/2017.
 */

public class ExpeditionListAdapter extends ArrayAdapter<Expedition> {
    private Context mContext;
    private int mResource;
    public ExpeditionListAdapter(Context context, int resource, List<Expedition> expeditions){
        super(context,resource,expeditions);
        mContext=context;
        mResource=resource;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        String idExpedition =getItem(position).getIdExpedition();
        String name=getItem(position).getName();
        String description=getItem(position).getDescription();
        String type=getItem(position).getType();
        String status=getItem(position).getStatus();
        String userId=getItem(position).getUserId();
        String startDate=getItem(position).getStartDate();
        String startTime=getItem(position).getStartTime();
        String endDate=getItem(position).getEndDate();
        String endTime=getItem(position).getEndTime();
        String place =getItem(position).getPlace();
        Expedition exp = new Expedition(idExpedition,name,description,type,status,userId,startDate,startTime,endDate,endTime,place);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView =inflater.inflate(mResource,parent,false);
        TextView viewExp = (TextView)convertView.findViewById(R.id.txt_expedition);
        TextView viewPlace=(TextView)convertView.findViewById(R.id.txt_sub_expedition);
        viewExp.setText(name);
        return convertView;
    }

}


package com.kit.backpackers.project_kit.Home.HomeFragments.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kit.backpackers.project_kit.Home.HomeFragments.Myexpfragments.JoinedExpDetailActivity;
import com.kit.backpackers.project_kit.Home.HomeFragments.Myexpfragments.MyExpFragmentActivity;
import com.kit.backpackers.project_kit.R;

/**
 * Created by Naila Kayani on 5/1/2017.
 */
public class JoinedExpeditionAdapter extends ArrayAdapter<String> {

    private final Activity context;
    String[] exp_id;
    String[] exp_location;
    String[] exp_name;
    String[] created_by;


    public JoinedExpeditionAdapter(Activity context, String[] exp_id, String[] exp_location, String[] exp_name, String[] created_by) {
        super(context, R.layout.join_expedition, exp_id);
        this.context = context;
        this.exp_id = exp_id;
        this.exp_location = exp_location;
        this.exp_name = exp_name;
        this.created_by=created_by;


    }

    public class LoadViews
    {
        TextView expName , expLoc ,creator;
        Button btn_exp;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LoadViews viewHolder;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.join_expedition, null);
            viewHolder = new LoadViews();
            viewHolder.expName = (TextView) convertView.findViewById(R.id.exp_name);
            viewHolder.expLoc = (TextView) convertView.findViewById(R.id.exp_location);
            viewHolder.creator = (TextView) convertView.findViewById(R.id.exp_creator);
            viewHolder.btn_exp = (Button) convertView.findViewById(R.id.explore_button);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (LoadViews) convertView.getTag();
        }
        viewHolder.expName.setText(exp_name[position]);
        viewHolder.expLoc.setText(exp_location[position]);
        viewHolder.creator.setText(created_by[position]);


        viewHolder.btn_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, JoinedExpDetailActivity.class);
                intent.putExtra("expid" , exp_id[position]);
                context.startActivity(intent);
            }
        });

        return  convertView;
    }
}

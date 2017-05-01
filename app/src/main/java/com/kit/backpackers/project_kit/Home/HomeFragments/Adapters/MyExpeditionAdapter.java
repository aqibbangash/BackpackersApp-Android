package com.kit.backpackers.project_kit.Home.HomeFragments.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.text.style.IconMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kit.backpackers.project_kit.Home.HomeFragments.Myexpfragments.MyExpFragmentActivity;
import com.kit.backpackers.project_kit.R;

/**
 * Created by Naila on 01/05/2017.
 */

public class MyExpeditionAdapter extends ArrayAdapter<String> {

    private final Activity context;
    String[] exp_id;
    String[] exp_location;
    String[] exp_name;


    public MyExpeditionAdapter(Activity context, String[] exp_id, String[] exp_location, String[] exp_name) {
        super(context, R.layout.my_expediition_ly, exp_id);
        this.context = context;
        this.exp_id = exp_id;
        this.exp_location = exp_location;
        this.exp_name = exp_name;


    }

    public class LoadViews
    {
        TextView expName , expLoc;
        Button btn_exp;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LoadViews viewHolder;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.my_expediition_ly, null);
            viewHolder = new LoadViews();
            viewHolder.expName = (TextView) convertView.findViewById(R.id.expeditionName);
            viewHolder.expLoc = (TextView) convertView.findViewById(R.id.expeditionLocation);
            viewHolder.btn_exp = (Button) convertView.findViewById(R.id.exploreExp);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (LoadViews) convertView.getTag();
        }
        viewHolder.expName.setText(exp_name[position]);
        viewHolder.expLoc.setText(exp_location[position]);


        viewHolder.btn_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context , exp_id[position] , Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context, MyExpFragmentActivity.class);
                intent.putExtra("expid" , exp_id[position]);
                context.startActivity(intent);
            }
        });

        return  convertView;
    }
}

package com.kit.backpackers.project_kit.Home.HomeFragments.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kit.backpackers.project_kit.Home.HomeFragments.Myexpfragments.MyExpFragmentActivity;
import com.kit.backpackers.project_kit.R;


/**
 * Created by Naila Kayani on 5/2/2017.
 */
public class myExpDetailAdapter extends ArrayAdapter<String> {

    private final Activity context;
    String[] exp_id;
    String[] exploc;
    String[] expdes;


    public myExpDetailAdapter(Activity context, String[] exp_id, String[] exploc, String[] expdes) {
        super(context, R.layout.my_exp_detail_adap, exp_id);
        this.context = context;
        this.exp_id = exp_id;
        this.exploc = exploc;
        this.expdes = expdes;


    }

    public class LoadViews
    {
        TextView expName , expLoc;
        Button btn_add_poi;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LoadViews viewHolder;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.my_expediition_ly, null);
            viewHolder = new LoadViews();
            viewHolder.expName = (TextView) convertView.findViewById(R.id.expdes);
            viewHolder.expLoc = (TextView) convertView.findViewById(R.id.exploc);
            viewHolder.btn_add_poi = (Button) convertView.findViewById(R.id.btn_add_poi);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (LoadViews) convertView.getTag();
        }
        viewHolder.expName.setText(expdes[position]);
        viewHolder.expLoc.setText(exploc[position]);


        viewHolder.btn_add_poi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context , exp_id[position] , Toast.LENGTH_LONG).show();
               // Intent intent=new Intent(context, poiActivity.class);
                //intent.putExtra("expid" , exp_id[position]);
               // context.startActivity(intent);
            }
        });

        return  convertView;
    }
}

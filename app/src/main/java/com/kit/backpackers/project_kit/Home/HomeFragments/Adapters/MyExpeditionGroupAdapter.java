package com.kit.backpackers.project_kit.Home.HomeFragments.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kit.backpackers.project_kit.R;

/**
 * Created by Naila Kayani on 5/1/2017.
 */
public class MyExpeditionGroupAdapter extends ArrayAdapter<String> {

    private final Activity context;
    String[] firstname;
    String[] phone;
    String[] gender;
    String[] picture;


    public MyExpeditionGroupAdapter(Activity context, String[] firstname, String[] phone, String[] gender,String[] picture ) {
        super(context, R.layout.my_exp_group_adap_ly, firstname);
        this.context = context;
        this.firstname = firstname;
        this.phone = phone;
        this.gender = gender;
        this.picture = picture;


    }

    public class LoadViews
    {
        TextView firstName , phone,gender;
        ImageView picture;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LoadViews viewHolder;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.my_expediition_ly, null);
            viewHolder = new LoadViews();
            viewHolder.firstName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.phone);
            viewHolder.gender = (TextView) convertView.findViewById(R.id.gender);
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.picture);

            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (LoadViews) convertView.getTag();
        }
        viewHolder.firstName.setText(firstname[position]);
        viewHolder.phone.setText(phone[position]);
        viewHolder.gender.setText(gender[position]);
        //viewHolder.picture.setText(picture[position]);




        return  convertView;
    }
}
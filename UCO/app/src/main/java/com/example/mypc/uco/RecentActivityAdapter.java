package com.example.mypc.uco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecentActivityAdapter extends BaseAdapter {

    ArrayList<RecentActivityObj> objects;
    private LayoutInflater inflator;


    private class ViewHolder{
        TextView txt_message;
    }

    public RecentActivityAdapter(Context context,ArrayList<RecentActivityObj> objects){
        inflator=LayoutInflater.from(context);
        this.objects=objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public RecentActivityObj getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflator.inflate(R.layout.list_item_recent_activity,null);
            holder.txt_message=(TextView)convertView.findViewById(R.id.lbl_recent_acivity_message);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.txt_message.setText(objects.get(position).getMessage());
        return convertView;
    }
}
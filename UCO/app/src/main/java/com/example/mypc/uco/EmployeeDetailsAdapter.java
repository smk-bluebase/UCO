package com.example.mypc.uco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class EmployeeDetailsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    ArrayList<EmployeeDetailsObj> objects;
    ArrayList<EmployeeDetailsObj> all_objects=new ArrayList<EmployeeDetailsObj>();

    private class ViewHolder{
        TextView memberName;
    }

    public EmployeeDetailsAdapter(Context context,ArrayList<EmployeeDetailsObj> objects){
        inflater=LayoutInflater.from(context);
        this.objects=objects;
    }

    public void copyData(){
        all_objects.addAll(objects);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public EmployeeDetailsObj getItem(int position) {
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
            convertView=inflater.inflate(R.layout.list_item,null);
            holder.memberName=(TextView)convertView.findViewById(R.id.lbl_member_list_name);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.memberName.setText(objects.get(position).getmemberName());
        return convertView;
    }
    public void filter(String charText){
        charText=charText.toLowerCase(Locale.getDefault());
        objects.clear();
        if (charText.length()==0){
            objects.addAll(all_objects);
        } else {
                for (EmployeeDetailsObj empobj:all_objects){
                    if (charText.length()!=0 && empobj.getmemberNo().toLowerCase(Locale.getDefault()).contains(charText)){
                        objects.add(empobj);
                    }else if (charText.length()!=0 && empobj.getmemberName().toLowerCase(Locale.getDefault()).contains(charText)){
                        objects.add(empobj);
                    }else if (charText.length()!=0 && empobj.getEmail_id().toLowerCase(Locale.getDefault()).contains(charText)){
                        objects.add(empobj);
                    }else if (charText.length()!=0 && empobj.getMobile_no().toLowerCase(Locale.getDefault()).contains(charText)){
                        objects.add(empobj);
                    }
                }
        }
        notifyDataSetChanged();
    }
}
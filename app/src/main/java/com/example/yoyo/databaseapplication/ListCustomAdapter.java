package com.example.yoyo.databaseapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoyo on 4/28/2016.
 */
public class ListCustomAdapter  extends ArrayAdapter<Notes>
{
     private final Context context;
     private  List<Notes> array_list;
     DBHelper help;

    public ListCustomAdapter(Context context, List<Notes> notes,DBHelper help)
    {
        super(context, R.layout.simple_list_item_1,notes);
        this.context = context;
        array_list = notes;
        this.help = help;
    }

    public void updateReceiptsList(List<Notes> newlist) {
        array_list = newlist;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
       // return super.getCount();
        return array_list.size();
    }

    @Override
    public Notes getItem(int position)
    {
       // return super.getItem(position);
        return array_list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
       // return super.getItemId(position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.simple_list_item_1, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        TextView hiddenText = (TextView) rowView.findViewById(R.id.hiddentext);

        textView.setText(array_list.get(position).getTopic());
        int id_number_from_database =  array_list.get(position).get_id();
        String string_id_number_from_database = Integer.toString(id_number_from_database);
        hiddenText.setText(string_id_number_from_database);
        String path = array_list.get(position).getImage();
        Bitmap bit = help.getNotesPicture(path);
        imageView.setImageBitmap(bit);


        // Change icon based on name



        return rowView;
    }
}

package com.example.yoyo.databaseapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by yoyo on 4/22/2016.
 */
public class ImageAdapter extends BaseAdapter
{

     private Context mContext;

    public ImageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() //broi duljinata na arraya s kartinki
    {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {//the actual object at the specified index
        return null;
    }

    @Override
    public long getItemId(int position) { //returns the row id of the item
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        if (convertView == null)
        {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
          //  imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else
        {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds =
            {
            R.drawable.but, R.drawable.images,
            R.drawable.me, R.drawable.spiderman

            };
    }


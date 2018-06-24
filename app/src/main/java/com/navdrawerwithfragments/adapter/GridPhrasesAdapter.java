package com.navdrawerwithfragments.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufo.learnchinese2.R;

import java.util.ArrayList;

public class GridPhrasesAdapter extends BaseAdapter {
    int itemLayoutId;
    Context mContext = null;
    ArrayList<CategoryItem> mListCategoryItems;

    static class ViewHolder {
        ImageView img;
        TextView txt;

        ViewHolder() {
        }
    }

    public long getItemId(int i) {
        return 0;
    }

    public GridPhrasesAdapter(Context context, ArrayList<CategoryItem> arrayList, int i) {
        this.mContext = context;
        this.itemLayoutId = i;
        this.mListCategoryItems = arrayList;
    }

    public int getCount() {
        return this.mListCategoryItems.size();
    }

    public CategoryItem getItem(int i) {
        return (CategoryItem) this.mListCategoryItems.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_grid_phrase, viewGroup, false);
            viewHolder.img = (ImageView) view.findViewById(R.id.img_grid_item);
            viewHolder.txt = (TextView) view.findViewById(R.id.txt_grid_item);
            viewGroup.setTag(viewHolder);
        } else {
            viewGroup = (ViewGroup) view;
            viewHolder = (ViewHolder) view.getTag();
        }
        int identifier = this.mContext.getResources().getIdentifier(((CategoryItem) this.mListCategoryItems.get(i)).thumbnail, "drawable", this.mContext.getPackageName());
        if (identifier > 0) {
            viewHolder.img.setImageResource(identifier);
        } else {
            viewHolder.img.setImageResource(R.drawable.ic_general_conversation);
        }
        viewHolder.txt.setText(((CategoryItem) this.mListCategoryItems.get(i)).categoryName);
        viewHolder.txt.setHorizontalFadingEdgeEnabled(true);
        viewGroup.invalidate();
        return viewGroup;
    }
}

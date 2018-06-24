package com.navdrawerwithfragments.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufo.learnchinese2.R;

import java.util.ArrayList;

/**
 * Created by adeeb on 6/23/2018.
 */

public class AdapterPhares extends RecyclerView.Adapter<AdapterPhares.ViewHoldermy> {
ArrayList<CategoryItem> mCategoryItems;
Context context;
OnRecyclerItemClick onRecyclerItemClick;
    @Override
    public ViewHoldermy onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_phrase,parent,false);
context=parent.getContext();
       ViewHoldermy viewHoldermy=new ViewHoldermy(view);
        return viewHoldermy;
    }

    public AdapterPhares(ArrayList<CategoryItem> mCategoryItems) {
        this.mCategoryItems=mCategoryItems;
    }

    @Override
    public void onBindViewHolder(ViewHoldermy holder, final int position) {

        holder.textView.setText(mCategoryItems.get(position).categoryName);

        int identifier = context.getResources().getIdentifier(((CategoryItem) mCategoryItems.get(position)).thumbnail, "drawable", context.getPackageName());
        if (identifier > 0) {
            holder.image.setBackgroundResource(identifier);
        } else {
            holder.image.setImageResource(R.drawable.ic_general_conversation);
        }
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onRecyclerItemClick.onClick(v,position);
    }
});
      //  holder.image.setBackgroundResource(mCategoryItems.get(position).thumbnail);
    }

    @Override
    public int getItemCount() {
        return mCategoryItems!=null?mCategoryItems.size():0;
    }



    public class ViewHoldermy extends RecyclerView.ViewHolder{

        ImageView image;
        TextView  textView;

        public ViewHoldermy(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.img_grid_item);
            textView=(TextView)itemView.findViewById(R.id.txt_grid_item);

        }

    }

    public void setOnItemcLickListner(OnRecyclerItemClick oncLickListner){
        this.onRecyclerItemClick=oncLickListner;
    }

    public CategoryItem getItem(int position){
        CategoryItem categoryItem=null;
        try {
         categoryItem=mCategoryItems.get(position);
        }catch (Exception e){
            e.printStackTrace();
        }
        return categoryItem;
    }

    public interface OnRecyclerItemClick {
        public void onClick(View view, int position);
    }

}

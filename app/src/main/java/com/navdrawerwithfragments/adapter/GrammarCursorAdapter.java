package com.navdrawerwithfragments.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ufo.learnchinese2.R;

public class GrammarCursorAdapter extends CursorAdapter {
    public GrammarCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.item_text)).setText(cursor.getString(cursor.getColumnIndex("title")));
        ((TextView) view.findViewById(R.id.item_image)).setText(String.valueOf(cursor.getPosition() + 1));
    }

    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_view, viewGroup, false);
    }
}

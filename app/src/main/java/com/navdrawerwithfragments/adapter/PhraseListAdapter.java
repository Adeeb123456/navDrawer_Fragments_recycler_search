package com.navdrawerwithfragments.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.internal.view.SupportMenu;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufo.learnchinese2.R;
import com.ufo.learnchinese2.database.Database;

import java.util.ArrayList;
import java.util.Locale;

public class PhraseListAdapter extends BaseAdapter {
    ArrayList<PhraseItem> arrayList;
    int currentPosition = -1;
    View hiddenLayout;
    int itemLayoutId;
    String keySearch = "";
    ArrayList<PhraseItem> listPhraseItems;
    Context mContext;
    Database mDatabase;
    String phraseColumnName = "";

    static class ViewHolder {
        View hiddenLayout;
        ImageView imgStar;
        TextView txtKorean;
        TextView txtPinyin;
        TextView txtVietnamese;

        ViewHolder() {
        }
    }

    public long getItemId(int i) {
        return 0;
    }

    public PhraseListAdapter(Context context, ArrayList<PhraseItem> arrayList, int i, Database database) {
        this.mContext = context;
        this.listPhraseItems = new ArrayList();
        this.listPhraseItems.addAll(arrayList);
        this.arrayList = new ArrayList();
        this.arrayList.addAll(arrayList);
        this.itemLayoutId = i;
        this.mDatabase = database;
    }

    public int getCount() {
        return this.listPhraseItems.size();
    }

    public void setKeySearch(String str) {
        this.keySearch = str;
    }

    public PhraseItem getItem(int i) {
        return (PhraseItem) this.listPhraseItems.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        View inflate;
        PhraseListAdapter phraseListAdapter = this;
        int i2 = i;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(phraseListAdapter.mContext).inflate(phraseListAdapter.itemLayoutId, viewGroup, false);
            viewHolder.txtKorean = (TextView) view.findViewById(R.id.txt_korea);
            viewHolder.txtPinyin = (TextView) view.findViewById(R.id.txt_pinpyn);
            viewHolder.txtVietnamese = (TextView) view.findViewById(R.id.txt_vietnam);
            viewHolder.imgStar = (ImageView) view.findViewById(R.id.imgVoice);
            viewHolder.hiddenLayout = view.findViewById(R.id.item_hide);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            inflate = view;
        }
        final PhraseItem phraseItem = (PhraseItem) phraseListAdapter.listPhraseItems.get(i2);
        if (i2 != phraseListAdapter.currentPosition) {
            viewHolder.hiddenLayout.setVisibility(View.GONE);
        } else {
            viewHolder.hiddenLayout.setVisibility(0);
        }
        final ImageView imageView = viewHolder.imgStar;
        CharSequence txtKorean = phraseItem.getTxtKorean();
        CharSequence txtVietnamese = phraseItem.getTxtVietnamese();
        CharSequence txtPinpyn = phraseItem.getTxtPinpyn();
        int indexOf = txtVietnamese.toString().toLowerCase(Locale.US).indexOf(phraseListAdapter.keySearch.toLowerCase(Locale.US));
        int length = phraseListAdapter.keySearch.length() + indexOf;
        if (indexOf != -1) {
            SpannableString spannableString = new SpannableString(txtVietnamese);
        //  TextAppearanceSpan textAppearanceSpan = r13;
            TextAppearanceSpan textAppearanceSpan2 = new TextAppearanceSpan(null, 1, -1, new ColorStateList(new int[][]{new int[0]}, new int[]{SupportMenu.CATEGORY_MASK}), null);
            spannableString.setSpan(textAppearanceSpan2, indexOf, length, 33);
            viewHolder.txtVietnamese.setText(spannableString);
        } else {
            viewHolder.txtVietnamese.setText(txtVietnamese);
        }
        viewHolder.txtKorean.setText(txtKorean);
        viewHolder.txtPinyin.setText(txtPinpyn);
        int favorite = phraseItem.getFavorite();
        final int id = phraseItem.getId();
        if (favorite == 1) {
            imageView.setSelected(true);
        } else {
            imageView.setSelected(false);
        }
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (imageView.isSelected()) {
                    PhraseListAdapter.this.mDatabase.updateFavorite(id, 0);
                    phraseItem.setFavorite(0);

                    imageView.setSelected(false);
                    return;
                }else {
                    PhraseListAdapter.this.mDatabase.updateFavorite(id, 1);
                    phraseItem.setFavorite(1);
                    imageView.setSelected(true);
                }

            }
        });
        return view;
    }

    public void filter(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PhraseListAdapter.filter inputText = ");
        stringBuilder.append(str);
        Log.d("ufo", stringBuilder.toString());
        str = str.toLowerCase();
        this.listPhraseItems.clear();
        if (str.length() == 0) {
            this.listPhraseItems.addAll(this.arrayList);
            this.keySearch = "";
        } else {
            this.keySearch = str;
            int i = 0;
            for (int i2 = 0; i2 < this.arrayList.size(); i2++) {
                PhraseItem phraseItem = (PhraseItem) this.arrayList.get(i2);
                if (phraseItem.getTxtVietnamese() != null && phraseItem.getTxtVietnamese().toLowerCase().startsWith(str)) {
                    this.listPhraseItems.add(phraseItem);
                }
            }
            while (i < this.arrayList.size()) {
                PhraseItem phraseItem2 = (PhraseItem) this.arrayList.get(i);
                if (!(phraseItem2.getTxtVietnamese() == null || phraseItem2.getTxtVietnamese().toLowerCase().startsWith(str) || !phraseItem2.getTxtVietnamese().toLowerCase().contains(str))) {
                    this.listPhraseItems.add(phraseItem2);
                }
                i++;
            }
        }
        notifyDataSetChanged();
    }

    public void setPositionSelected(int i) {
        this.currentPosition = i;
    }

    public void updateListPhraseItems(ArrayList<PhraseItem> arrayList) {
        this.listPhraseItems = arrayList;
        notifyDataSetChanged();
    }
}

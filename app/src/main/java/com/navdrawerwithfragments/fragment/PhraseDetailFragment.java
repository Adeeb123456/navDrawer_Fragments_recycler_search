package com.navdrawerwithfragments.fragment;

import android.os.Bundle;

import com.ufo.learnchinese2.R;
import com.ufo.learnchinese2.adapter.PhraseListAdapter;
import com.ufo.learnchinese2.utils.Utils;


public class PhraseDetailFragment extends ExpandableFragment {
    int categoryId;
    String mTitle = "";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.categoryId = getArguments().getInt(Utils.CATEGORY_ID);
        this.mTitle = getArguments().getString(Utils.TITLE);
       liPhraseItems = this.mDatabase.getListPhraseItem(this.categoryId);
        this.mPhraseListAdapter = new PhraseListAdapter(this.mActivity, this.liPhraseItems, R.layout.phrase_detail_item_2, this.mDatabase);
    }

    public void onResume() {
        super.onResume();
        this.mActivity.hideBannerAd();
        this.mActivity.setTitle(this.mTitle);
    }

    public void onDetach() {
        super.onDetach();
    }
}

package com.navdrawerwithfragments.fragment;


import com.ufo.learnchinese2.MainActivity;

public class SearchFragment extends ExpandableFragment {
    public static final String KEYWORD_KEY = "key_string";
    String mKeyword = "";
    String mTitle = "";
    String strNoresult = "";



    public void updateResult(String str) {
        this.mPhraseListAdapter.filter(str);
        this.mPhraseListAdapter.setPositionSelected(-1);
        this.mListView.setItemChecked(this.currentPosition, false);
        this.currentPosition = -1;
        this.previousView = null;
        MainActivity mainActivity = this.mActivity;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Search for: ");
        stringBuilder.append(str);
        mainActivity.setTitle(stringBuilder.toString());
    }

    public void onResume() {
        this.mActivity.hideBannerAd();
        super.onResume();
        MainActivity mainActivity = this.mActivity;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Search for: ");
        stringBuilder.append(this.mKeyword);

        (mainActivity).lockDrawer();
        mainActivity.setTitle(stringBuilder.toString());

    }

    public void onDetach() {
        super.onDetach();
        this.mActivity.clearSearchView();
    }
}

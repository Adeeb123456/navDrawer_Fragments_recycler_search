package com.navdrawerwithfragments.fragment;

public class FavoritesFragment extends ExpandableFragment {
    int categoryId;
    String mTitle = "";


    public void onResume() {
        super.onResume();
        this.mActivity.showBannerAd();
        this.mActivity.setTitle(this.mTitle);
    }

    public void onDetach() {
        super.onDetach();
    }
}

package com.navdrawerwithfragments.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.ufo.learnchinese2.AdapterPhares;
import com.ufo.learnchinese2.MainActivity;
import com.ufo.learnchinese2.R;
import com.ufo.learnchinese2.activity.PhraseDetailActivity;
import com.ufo.learnchinese2.adapter.CategoryItem;
import com.ufo.learnchinese2.adapter.GridPhrasesAdapter;
import com.ufo.learnchinese2.adapter.PhraseItem;
import com.ufo.learnchinese2.database.Database;
import com.ufo.learnchinese2.utils.ItemDecorationAlbumColumns;
import com.ufo.learnchinese2.utils.Utils;

import java.util.ArrayList;

public class PhraseFragment extends Fragment implements AdapterPhares.OnRecyclerItemClick{
    ArrayList<CategoryItem> listCategoryItems;
    ArrayList<PhraseItem> listPhraseItems;
    MainActivity mActivity;
    GridPhrasesAdapter mAdapter;
    Cursor mCursor;
    Database mDatabase;
    FragmentManager mFragmentManager;
    GridView mGridView;
    String mTitle = "";
    FragmentTransaction mTransaction;

    AdapterPhares adapterPhares;
    RecyclerView recyclerView;

    @Override
    public void onClick(View view, int position) {
      CategoryItem  categoryItem=adapterPhares.getItem(position);
        if (categoryItem.categoryId == 100) {
            PhraseFragment.this.mActivity.seeMoreApps();
            return;
        }

        Intent intent = new Intent(PhraseFragment.this.mActivity, PhraseDetailActivity.class);
        intent.putExtra(Utils.CATEGORY_ID, categoryItem.categoryId);
        intent.putExtra(Utils.TITLE, categoryItem.categoryName);
       startActivity(intent);
        PhraseFragment.this.mActivity.checkShowPopupAds();
    }

    class C04681 implements OnItemClickListener {
        C04681() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            CategoryItem categoryItem = (CategoryItem) PhraseFragment.this.listCategoryItems.get(i);
            if (((CategoryItem) PhraseFragment.this.listCategoryItems.get(i)).categoryId == 100) {
                PhraseFragment.this.mActivity.seeMoreApps();
                return;
            }
            Intent intent = new Intent(PhraseFragment.this.mActivity, PhraseDetailActivity.class);
            intent.putExtra(Utils.CATEGORY_ID, categoryItem.categoryId);
            intent.putExtra(Utils.TITLE, categoryItem.categoryName);
            PhraseFragment.this.mActivity.startActivity(intent);
            PhraseFragment.this.mActivity.checkShowPopupAds();
        }
    }
GridLayoutManager gridLayoutManager;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mActivity = (MainActivity) getActivity();
//        this.mTitle = getArguments().getString(Utils.TITLE);
        this.mActivity.setTitle(this.mTitle);
//        this.mActivity.getDrawerToogle().setDrawerIndicatorEnabled(false);

//setRetainInstance(true);
       // adapterPhares=new AdapterPhares()
    }
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.phrase_fragment_recycler, viewGroup, false);
        /*this.mGridView = (GridView) view.findViewById(R.id.grid_view_phrase);
        this.mGridView.setAdapter(this.mAdapter);
        this.mGridView.setOnItemClickListener(new C04681());*/
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_phares) ;
        mActivity= (MainActivity) getActivity();





        return view;
    }
    ArrayList<CategoryItem> categoryItems;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabase = Database.newInstance(mActivity, Utils.GRAMMAR_DATABASE_NAME);

                mDatabase.open();

                if(categoryItems==null) {
                    categoryItems = mDatabase.getListCategoryItems();
                }
                // mAdapter = new GridPhrasesAdapter(mActivity, categoryItems,0);
                adapterPhares=new AdapterPhares(categoryItems);
                recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
                        getResources().getDimensionPixelSize(R.dimen.photos_list_spacing),
                        getResources().getInteger(R.integer.photo_list_preview_columns)));
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
                adapterPhares.setOnItemcLickListner(PhraseFragment.this);
                recyclerView.setAdapter(adapterPhares);



            }
        },100);
    }

    public void onResume() {
        super.onResume();
        this.mActivity.setToggle();
        this.mActivity.setTitle(getResources().getString(R.string.app_name));
        this.mActivity.showBannerAd();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    public void onDetach() {
        super.onDetach();
     //   mDatabase.close();
    }
}

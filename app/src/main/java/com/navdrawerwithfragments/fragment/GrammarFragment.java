package com.navdrawerwithfragments.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ufo.learnchinese2.MainActivity;
import com.ufo.learnchinese2.R;
import com.ufo.learnchinese2.adapter.GrammarCursorAdapter;
import com.ufo.learnchinese2.database.Database;
import com.ufo.learnchinese2.utils.Utils;

public class GrammarFragment extends Fragment {
    MainActivity mActivity;
    GrammarCursorAdapter mAdapter = null;
    Cursor mCursor = null;
    Database mDatabase = null;
    ListView mListView = null;
    String mTitle = "";
    String mtableName = null;

    class C04671 implements OnItemClickListener {
        C04671() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            GrammarFragment.this.mCursor.moveToPosition(i);
           int index= GrammarFragment.this.mCursor.getInt(GrammarFragment.this.mCursor.getColumnIndex(Utils.GRAMMAR_COLUMN_ID));
            Fragment fragment = new WebviewFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Utils.GRAMMAR_COLUMN_ID, index);
            bundle.putString("TITLE", GrammarFragment.this.mCursor.getString(1));
            fragment.setArguments(bundle);
          FragmentTransaction fragmentTransaction = GrammarFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mActivity = (MainActivity) getActivity();
//        this.mTitle = getArguments().getString(Utils.TITLE);
        this.mActivity.setTitle(this.mTitle);
        this.mActivity.getDrawerToogle().setDrawerIndicatorEnabled(false);
        this.mDatabase = Database.newInstance(this.mActivity, Utils.GRAMMAR_DATABASE_NAME);
        this.mCursor = this.mDatabase.getListGrammar();
        this.mAdapter = new GrammarCursorAdapter(this.mActivity, this.mCursor);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.my_fragment, viewGroup, false);
        this.mListView = (ListView) view.findViewById(R.id.list_fragment);
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(new C04671());
        return view;
    }

    public void onResume() {
        super.onResume();
        this.mActivity.setTitle(this.mTitle);
        this.mActivity.hideBannerAd();
    }

    public void onDetach() {
        super.onDetach();
    }
}

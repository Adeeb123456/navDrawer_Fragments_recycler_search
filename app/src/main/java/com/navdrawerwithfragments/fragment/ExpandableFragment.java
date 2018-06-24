package com.navdrawerwithfragments.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ufo.learnchinese2.MainActivity;
import com.ufo.learnchinese2.R;
import com.ufo.learnchinese2.adapter.PhraseItem;
import com.ufo.learnchinese2.adapter.PhraseListAdapter;
import com.ufo.learnchinese2.database.Database;
import com.ufo.learnchinese2.utils.Utils;

import java.util.ArrayList;

public class ExpandableFragment extends Fragment {
    int currentPosition = -1;
    ArrayList<PhraseItem> liPhraseItems=new ArrayList<>();
    MainActivity mActivity;
    Cursor mCursor;
    Database mDatabase;
    ListView mListView;
    PhraseListAdapter mPhraseListAdapter;
    MediaPlayer mPlayer;
    View previousView = null;
    String strNoresult = "";
    String searchQuery="";
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d("ufo", "ExpandableFragment.onCreate");
        this.mActivity = (MainActivity) getActivity();
        this.mActivity.showButtonBack();
        this.mDatabase = Database.newInstance(this.mActivity, Utils.PHRASE_DATABASE_NAME);
        if(getArguments()!=null){
            searchQuery=getArguments().getString(SearchFragment.KEYWORD_KEY);

        }

    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.phrase_detail_fragment, viewGroup, false);
        this.mListView = (ListView) view.findViewById(R.id.list_phrase_detail);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getTag().equalsIgnoreCase(Utils.KEY_FAV_FRAGMENT)){

                    liPhraseItems=mDatabase.getListPhraseFavorite2();
                }else {
                    liPhraseItems=mDatabase.searchPhrase2(searchQuery);
                }

                if (mPhraseListAdapter==null){
                    mPhraseListAdapter=new PhraseListAdapter(getContext(),liPhraseItems,R.layout.phrase_detail_item_2
                            ,mDatabase);

                }
                mListView.setAdapter(mPhraseListAdapter);
                mPhraseListAdapter.notifyDataSetChanged();
            }
        },100);

        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onItemClick position = ");
                stringBuilder.append(i);
                Log.d("ufo", stringBuilder.toString());
                TextView textView = view.findViewById(R.id.txt_no_result);
                if (adapterView != null) {
                    adapterView.setVisibility(View.VISIBLE);
                }
                if (ExpandableFragment.this.currentPosition != i) {
                    ExpandableFragment.this.mListView.setItemChecked(i, true);
                    ExpandableFragment.this.playSound(i);
                    if (ExpandableFragment.this.previousView != null) {
                        ExpandableFragment.this.collapseView(ExpandableFragment.this.previousView);
                    }
                    ExpandableFragment.this.expandView(view);
                    ExpandableFragment.this.currentPosition = i;
                    ExpandableFragment.this.mPhraseListAdapter.setPositionSelected(i);
                    ExpandableFragment.this.previousView = view;
                } else if (ExpandableFragment.this.previousView != null) {
                    ExpandableFragment.this.playSound(i);
                }
            }
        });
        this.mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long j) {
                Utils.log("onitem long click");
                TextView textView = view.findViewById(R.id.copy_view
                );
                textView.setVisibility(View.VISIBLE);
                j = (long) ExpandableFragment.this.mActivity.getResources().getDisplayMetrics().density;
            float    viewfloat = view.getY() - ((float) textView.getHeight());
                if (viewfloat < 0) {
                    viewfloat = 0;
                }
                adapterView.setY(viewfloat);
                adapterView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        ClipboardManager clipboardManager = (ClipboardManager) ExpandableFragment.this.mActivity.getSystemService("clipboard");
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(((PhraseItem) ExpandableFragment.this.liPhraseItems.get(i)).getTxtKorean());
                        stringBuilder.append("\n");
                        stringBuilder.append(((PhraseItem) ExpandableFragment.this.liPhraseItems.get(i)).getTxtPinpyn());
                        clipboardManager.setPrimaryClip(ClipData.newPlainText("copy", stringBuilder.toString()));
                        view.setVisibility(8);
                        Toast.makeText(ExpandableFragment.this.mActivity, "text is copied to clipboard", 0).show();
                    }
                });
                return true;
            }
        });
        view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = view.findViewById(R.id.copy_view);
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
                return false;
            }
        });
        return view;
    }

    public void playSound(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mPhraseListAdapter.getItem(i).getVoice());
        stringBuilder.append("_f");
      String  iStr = stringBuilder.toString();
        releasePlayer();
        i = this.mActivity.getResources().getIdentifier(iStr, "raw", this.mActivity.getPackageName());
        if (i != 0) {
            this.mPlayer = MediaPlayer.create(this.mActivity, i);
            this.mPlayer.start();
        }
    }

    private void expandView(View view) {
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.item_hide);
        Animation loadAnimation = AnimationUtils.loadAnimation(this.mActivity, R.anim.slide_down);
        loadAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(0);
            }
        });
        linearLayout.startAnimation(loadAnimation);
    }

    public void collapseView(View view) {
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.item_hide);
        Animation loadAnimation = AnimationUtils.loadAnimation(this.mActivity, R.anim.slide_up);
        loadAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.GONE);
            }
        });
        linearLayout.startAnimation(loadAnimation);
    }

    public void releasePlayer() {
        try {
            if (this.mPlayer != null) {
                if (this.mPlayer.isPlaying()) {
                    this.mPlayer.stop();
                }
                this.mPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }
}

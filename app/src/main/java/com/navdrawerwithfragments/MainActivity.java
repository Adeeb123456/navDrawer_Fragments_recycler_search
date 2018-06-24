package com.navdrawerwithfragments;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ufo.learnchinese2.database.Database;
import com.ufo.learnchinese2.fragment.FavoritesFragment;
import com.ufo.learnchinese2.fragment.PhraseFragment;
import com.ufo.learnchinese2.fragment.SearchFragment;
import com.ufo.learnchinese2.inAppPurchase.IabHelper;
import com.ufo.learnchinese2.inAppPurchase.IabResult;
import com.ufo.learnchinese2.inAppPurchase.Inventory;
import com.ufo.learnchinese2.inAppPurchase.Purchase;
import com.ufo.learnchinese2.utils.Utils;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, OnClickListener {
    static final int RC_REQUEST = 10001;
    static final String SKU_REMOVE_ADS = "remove_ads";
    Builder builder;
    AlertDialog dialog;
    Fragment firstFragment = null;
    private boolean isBannerLoaded;
    boolean isNoAds = false;
    boolean isSetupInAppDone = false;
    int[] listImgResource;
    String[] listNameNav;
    private AdRequest mAdRequest;
    private AdView mAdView;
    Cursor mCursor = null;
    Database mDatabase = null;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    private boolean mExpandedInActionView = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    FragmentManager mFragmentManager = null;
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new C06338();
    private IabHelper mHelper;
    private InterstitialAd mInterstitialAd;
    NavigationView mNavigationView;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new C06349();
    SearchView mSearchView;
 public Toolbar mToolbar = null;
    FragmentTransaction mTransaction = null;
    private int popupAdCounter = 0;
    private int popupTimer = 3;
    MenuItem searchMenuItem;
    Fragment fragmentCurr;

    class navMenuIconClickListner implements OnClickListener {
        navMenuIconClickListner() {
        }

        public void onClick(View view) {
            Utils.hideSoftKeyboard(MainActivity.this);
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                onBackPressed();
            } else if (MainActivity.this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START) !=false) {
                MainActivity.this.mDrawerLayout.closeDrawer((int) GravityCompat.START);
            } else {
                MainActivity.this.mDrawerLayout.openDrawer((int) GravityCompat.START);
            }
        }
    }

    class C04515 implements OnClickListener {
        C04515() {
        }

        public void onClick(View view) {
            MainActivity.this.mExpandedInActionView = true;
            if (MainActivity.this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START) != false) {
                MainActivity.this.mDrawerLayout.closeDrawer((int) GravityCompat.START);
            }
        }
    }

    class C06293 extends AdListener {
        C06293() {
        }

        public void onAdClosed() {
            MainActivity.this.requestNewInterstitialAd();
        }

        public void onAdLoaded() {
            Utils.log("popup ad is loaded");
        }

        public void onAdFailedToLoad(int i) {
            super.onAdFailedToLoad(i);
        }
    }

    class C06304 extends AdListener {
        C06304() {
        }

        public void onAdLoaded() {
            super.onAdLoaded();
            MainActivity.this.isBannerLoaded = true;
            MainActivity.this.showBannerAd();
        }

        public void onAdFailedToLoad(int i) {
            super.onAdFailedToLoad(i);
        }
    }

    class C06316 implements OnQueryTextListener {
        C06316() {
        }

        public boolean onQueryTextSubmit(String str) {
            Log.d("ufo", "MainActivity.onQueryTextSubmit ");
            MainActivity.this.mExpandedInActionView = false;
            MainActivity.this.mSearchView.getQuery().toString().trim().toLowerCase();
            MainActivity.this.mSearchView.clearFocus();
            MainActivity.this.mSearchView.onActionViewCollapsed();
            return false;
        }

        public boolean onQueryTextChange(String str) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MainActivity.onQueryTextChange mSearchView.isIconified() = ");
            stringBuilder.append(MainActivity.this.mSearchView.isIconified());
            stringBuilder.append("android ");
            Log.d("ufo", stringBuilder.toString());
            str = Utils.validSQL(MainActivity.this.mSearchView.getQuery().toString());
            if (MainActivity.this.mExpandedInActionView) {
                MainActivity.this.suggest(str);
            }
            return true;
        }
    }

    class C06327 implements IabHelper.OnIabSetupFinishedListener {
        C06327() {
        }

        public void onIabSetupFinished(IabResult iabResult) {
            if (iabResult.isSuccess()) {
                Utils.log("setup InApp failed");
            } else if (MainActivity.this.mHelper != null) {
                try {
                    MainActivity.this.mHelper.queryInventoryAsync(MainActivity.this.mGotInventoryListener);
                } catch (Exception iabResult2) {
                    iabResult2.printStackTrace();
                }
                MainActivity.this.isSetupInAppDone = true;
            }
        }
    }

    class C06338 implements IabHelper.QueryInventoryFinishedListener {
        C06338() {
        }

        public void onQueryInventoryFinished(IabResult iabResult, Inventory inventory) {
            Utils.log("Query inventory finished.");
            if (MainActivity.this.mHelper != null) {
                if (iabResult.isFailure()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to query inventory: ");
                    stringBuilder.append(iabResult);
                    Utils.log(inventory.toString());
                    return;
                }
                Utils.log("Query inventory was successful.");
                Purchase purchase = inventory.getPurchase(MainActivity.SKU_REMOVE_ADS);
                if (!(iabResult == null || MainActivity.this.verifyDeveloperPayload(purchase) == true)) {
                    MainActivity.this.setPremiumState(true);
                    MainActivity.this.hideBannerAd();
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("User is ");
                stringBuilder.append(MainActivity.this.isNoAds  ? "PREMIUM" : "NOT PREMIUM");
                Utils.log(iabResult.toString());
                Utils.log("Initial inventory query finished; enabling main UI.");
            }
        }
    }

    class C06349 implements IabHelper.OnIabPurchaseFinishedListener {
        C06349() {
        }

        public void onIabPurchaseFinished(IabResult iabResult, Purchase purchase) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Purchase finished: ");
            stringBuilder.append(iabResult);
            stringBuilder.append(", purchase: ");
            stringBuilder.append(purchase);
            Utils.log(stringBuilder.toString());
            if (MainActivity.this.mHelper != null) {
                if (iabResult.isFailure()) {
                    StringBuilder stringBuilder1 = new StringBuilder();
                    stringBuilder1.append("Error purchasing: ");
                    stringBuilder1.append(iabResult);
                    Utils.log(purchase.toString());
                } else if (MainActivity.this.verifyDeveloperPayload(purchase)) {
                    Utils.log("Error purchasing. Authenticity verification failed.");
                } else {
                    Utils.log("Purchase successful.");
                    if (purchase.getSku().equals(MainActivity.SKU_REMOVE_ADS)) {
                        Utils.log("Purchase is premium upgrade. Congratulating user.");
                        Utils.log("Thank you for upgrading to premium!");
                        MainActivity.this.setPremiumState(true);
                        MainActivity.this.hideBannerAd();
                        MainActivity.this.invalidateOptionsMenu();
                        MainActivity.this.hideItemRemoveAds();
                    }
                }
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView( R.layout.activity_main2_drawer);
        setTitle(getResources().getString(R.string.app_name));
        setUpActionbar();
        initNavigation();
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);*/
        Utils.DEVICE_LANGUAGE = Locale.getDefault().getDisplayLanguage();
        this.mDatabase = Database.newInstance(this, Utils.PHRASE_DATABASE_NAME);

        this.mFragmentManager = getSupportFragmentManager();
        if (this.mFragmentManager.getBackStackEntryCount() == 0 && bundle == null) {
            this.firstFragment = new PhraseFragment();
            fragmentCurr=firstFragment;
            this.mTransaction = this.mFragmentManager.beginTransaction();
            this.mTransaction.add(R.id.main_content, this.firstFragment).commit();
        }
        if (this.isNoAds == true) {
            this.isNoAds = Utils.getPremiumState(this);
        }
        if (this.isNoAds == false) {
//            initAds();
        }
        initExitDialog();
        try {
            setUpInAppPurchase();
            setUpFirebase();
        } catch (Exception bundle2) {
            bundle2.printStackTrace();
        }
    }

    public void refreshView() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void setUpFirebase() {
       // FirebaseApp.c
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.LOCATION, Locale.getDefault().toString());
        this.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
    }

    public void setUpActionbar() {
        try {
            this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (this.mToolbar != null) {
                setSupportActionBar(this.mToolbar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showButtonBack() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    public void setToggle() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            if (this.mToolbar != null) {

               this.mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.menu));
            }
        }
    }

    public void initNavigation() {
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, this.mToolbar, R.string.menu_navigation_left, R.string.app_name) {
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                MainActivity.this.invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                MainActivity.this.invalidateOptionsMenu();
            }
        };
        this.mDrawerLayout.setDrawerListener(this.mDrawerToggle);
        this.mDrawerToggle.syncState();
        this.mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        this.mNavigationView.setNavigationItemSelectedListener(this);
        this.mNavigationView.setCheckedItem(R.id.nav_phrase);
        this.mNavigationView.setItemIconTintList(null);
        if (this.isNoAds) {
            hideItemRemoveAds();
        }
        this.mToolbar.setNavigationOnClickListener(new navMenuIconClickListner());
    }

    public void initAds() {
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId(getResources().getString(R.string.popup_ad_unit_id));
        this.mInterstitialAd.setAdListener(new C06293());
        requestNewInterstitialAd();
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setAdListener(new C06304());
        requestNewBannerAd();
    }

    public void rateApp() {

    String apstoreUrl="https://play.google.com/store/apps/details?id="+getPackageName();
    try{
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(apstoreUrl));
        startActivity(intent);
    }catch (Exception e){
        e.printStackTrace();
    }


    }

    public void seeMoreApps() {
        String apstoreUrl= "https://play.google.com/store/apps/developer?id=Fog+Revolution+2";
        try{
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(apstoreUrl));
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void seeApps(int r5) {

    }

    public void requestNewInterstitialAd() {
        this.mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(getString(R.string.ads_test_device_id)).build());
    }

    public void requestNewBannerAd() {
        this.mAdRequest = new AdRequest.Builder().addTestDevice(getString(R.string.ads_test_device_id)).build();
        this.mAdView.loadAd(this.mAdRequest);
    }

    private void showInterstitialAs() {
        if (this.mInterstitialAd != null && !this.isNoAds) {
            if (this.mInterstitialAd.isLoaded()) {
                this.mInterstitialAd.show();
            } else {
                requestNewInterstitialAd();
            }
        }
    }

    public void hideBannerAd() {
        if (this.mAdView != null) {
            this.mAdView.setVisibility(View.GONE);
        }
    }

    public void showBannerAd() {
        if (this.mAdView != null && !this.isNoAds) {
            if (this.isBannerLoaded) {
                this.mAdView.setVisibility(View.VISIBLE);
            } else {
                requestNewBannerAd();
            }
        }
    }

    public void checkShowPopupAds() {
        if ((this.popupAdCounter - 1) % this.popupTimer == 0) {
            showInterstitialAs();
        }
        this.popupAdCounter++;
    }

    public ActionBarDrawerToggle getDrawerToogle() {
        return this.mDrawerToggle;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        //this.mDrawerToggle.syncState();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.searchMenuItem = menu.findItem(R.id.menu_search);
        this.mSearchView = (SearchView) this.searchMenuItem.getActionView();
        if (this.mSearchView == null) {
            return true;
        }
        this.mSearchView.setImeOptions(3);
        this.mSearchView.setQueryHint(getResources().getString(R.string.search_hint));
        this.mSearchView.setOnSearchClickListener(new C04515());
        this.mSearchView.setOnQueryTextListener(new C06316());
        return true;
    }

    public void suggest(String str) {
        Fragment findFragmentById = this.mFragmentManager.findFragmentById(R.id.main_content);
        if (findFragmentById instanceof SearchFragment) {
            ((SearchFragment) findFragmentById).updateResult(str);
        } else {
            searchPhrase(str);
        }
    }

    public void searchPhrase(String str) {
        Fragment searchFragment = new SearchFragment();
        fragmentCurr=searchFragment;
        Bundle bundle = new Bundle();
        bundle.putString(SearchFragment.KEYWORD_KEY, str);
        searchFragment.setArguments(bundle);
        this.mTransaction = this.mFragmentManager.beginTransaction();
        this.mTransaction.replace(R.id.main_content, searchFragment, Utils.KEY_SEARCH_FRAGMENT);
        if ((this.mFragmentManager.findFragmentByTag(Utils.KEY_SEARCH_FRAGMENT) instanceof SearchFragment) != false) {
            this.mFragmentManager.popBackStack();
        }
        this.mTransaction.addToBackStack(null);
        this.mTransaction.commit();
    }

    public void clearSearchView() {
        this.mSearchView.setQuery("", false);
        this.mSearchView.clearFocus();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        if(this.mSearchView != null)
        if ((this.mSearchView.isIconified())) {
            this.mSearchView.onActionViewCollapsed();
        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            onBackPressed();
        } else {
            this.mDrawerToggle.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    public void initExitDialog() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.custom_dialog_exit, null);
        this.builder = new Builder(this, R.style.Dialog_No_Tittle);
        this.builder.setView(inflate);
        this.dialog = this.builder.create();
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.btn_dialog_cancel);
        LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.btn_dialog_quit);
        LinearLayout linearLayout3 = (LinearLayout) inflate.findViewById(R.id.btn_dialog_rate);
        linearLayout.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        linearLayout3.setOnClickListener(this);
    }

    public void showExitDialog() {
        this.dialog.show();
    }

protected void onActivityRezasult(int i, int i2, Intent intent) {
        if (!this.mHelper.handleActivityResult(i, i2, intent)) {
            super.onActivityResult(i, i2, intent);
        }
    }
    public void onBackPressed() {

   if(fragmentCurr instanceof PhraseFragment){

       super.onBackPressed();
   }else {
       mNavigationView.setCheckedItem(R.id.nav_phrase);

       getSupportFragmentManager().popBackStackImmediate();
       fragmentCurr=getSupportFragmentManager().findFragmentById(R.id.main_content);

        }

    }

    public void clearBackStack(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount()!=0){
            fragmentManager.popBackStackImmediate();
        }
    }


    public void clearBackstackTillTag(String tag){
        getSupportFragmentManager().popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }


    public void hideDrawer(){
if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
    mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void lockDrawer() {
        if (isDrawerLocked) {
            isDrawerLocked = false;
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
    boolean isDrawerLocked;
    private void unlockDrawer() {

        if (!isDrawerLocked) {
            isDrawerLocked = true;
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }


    public void openDrawer(){
      //  if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.openDrawer(GravityCompat.START);
       // }
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        this.mDatabase.close();
        super.onDestroy();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
       Fragment fragment=null;
       // this.mDrawerLayout.closeDrawer((int) GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.nav_favorites:
                if(!(fragmentCurr instanceof FavoritesFragment)){
                    fragment = new FavoritesFragment();
                    fragmentCurr=fragment;
                }

                break;
            case R.id.nav_phrase:
                setHomeFrag();

                break;
            case R.id.nav_rate:
                rateApp();
                break;
            case R.id.nav_removeAds:
                removeAds();
                break;
            case R.id.nav_send:
                seeMoreApps();
                break;
            default:
                break;
        }

        Fragment findFragmentById = this.mFragmentManager.findFragmentById(R.id.main_content);
        if(fragment!=null){
            fragmentCurr=fragment;
            clearBackStack();
            this.mTransaction = this.mFragmentManager.beginTransaction();
            this.mTransaction.replace(R.id.main_content, fragment, Utils.KEY_FAV_FRAGMENT);

            this.mTransaction.addToBackStack(null);
            this.mTransaction.commit();
        }


        mNavigationView.setCheckedItem(menuItem.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
public void setHomeFrag(){
    if(!(fragmentCurr instanceof PhraseFragment)){
        mFragmentManager.popBackStackImmediate();
        fragmentCurr=new PhraseFragment();
        mNavigationView.setCheckedItem(R.id.nav_phrase);
    }
}
    public void setUpInAppPurchase() {
        this.mHelper = new IabHelper(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjgE2vXxSoHvD9THYalcWUG50TQFQsFeolg3pEPBlJ+WRDdTJFVjmpPghH9KHvMo5P6CjYq1VH7E9sn9G9Xhw+P4nV4XQpVgpkE5bUr6E67henxAYqX7wNpamlO8c8oAaB1gOrjsr0YMAC+jcu3Y65ih0MS3uR7jt253XdhpjsbqylBFGCvQJpZLdzCzWsvy3rTjkqw2fKuAqtj3/m9p9zy/puSVJCy7KwyFgxQ6S8nbS3rBQaNdRy1Xqpvi6UowfSoNXvZzAl2ZBvNhNTgu8PTR/Re7sqh/P5CIBfSR12zHoNm9Fsgp5860ZPWDO5iwKWi5Ad7nFZ7oP3tR66VtL1wIDAQAB");
        this.mHelper.enableDebugLogging(false);
        this.mHelper.startSetup(new C06327());
    }

    private void setPremiumState(boolean z) {
        this.isNoAds = true;
        Editor edit = getSharedPreferences(Utils.MY_PREFS_NAME, 0).edit();
        edit.putBoolean(Utils.PREF_PREMIUM_KEY, z);
        edit.apply();
    }

    boolean verifyDeveloperPayload(Purchase purchase) {
        purchase.getDeveloperPayload();
        return true;
    }

    public void hideItemRemoveAds() {
        if (this.mNavigationView != null && this.mNavigationView.getMenu().findItem(R.id.nav_removeAds) != null) {
            this.mNavigationView.getMenu().findItem(R.id.nav_removeAds).setVisible(false);
        }
    }

    public void removeAds() {
        if (this.isSetupInAppDone) {
            try {
                this.mHelper.launchPurchaseFlow(this, SKU_REMOVE_ADS, RC_REQUEST, this.mPurchaseFinishedListener, "");
            } catch (IabHelper.IabAsyncInProgressException e) {
                Utils.log("Error launching purchase flow. Another async operation in progress.");
                e.printStackTrace();
            } catch (NullPointerException e2) {
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                e3.printStackTrace();
            }
            return;
        }
        Toast.makeText(this, "Remove Ads isn't available right now, please try later", 1).show();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_cancel:
                this.dialog.dismiss();
                return;
            case R.id.btn_dialog_quit:
                this.dialog.dismiss();
                finish();
                return;
            case R.id.btn_dialog_rate:
                this.dialog.dismiss();
                rateApp();
                return;
            default:
                return;
        }
    }
}

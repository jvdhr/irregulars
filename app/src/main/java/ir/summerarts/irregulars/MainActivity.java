package ir.summerarts.irregulars;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;

public class MainActivity extends NavigationLiveo implements OnItemClickListener {
    private boolean doubleBackToExitPressedOnce = false;
    private HelpLiveo mHelpLiveo;

    private DrawerLayout drawer;

    @Override
    public void onInt(Bundle savedInstanceState) {

        // User Information
//        this.userName.setText("Rudson Lima");
//        this.userEmail.setText("rudsonlive@gmail.com");
//        this.userPhoto.setImageResource(R.drawable.ic_rudsonlive);
        this.userBackground.setImageResource(R.drawable.header);

        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(getString(R.string.irregulars), R.drawable.ic_book_black_24dp);
        mHelpLiveo.add(getString(R.string.alphabet), R.drawable.ic_sort_by_alpha_black_24dp);
        mHelpLiveo.addSeparator(); // Item separator
//        mHelpLiveo.addSubHeader(getString(R.string.categories)); //Item subHeader
        mHelpLiveo.add(getString(R.string.share), R.drawable.ic_share_black_24dp);
        mHelpLiveo.add(getString(R.string.rate), R.drawable.ic_star_rate_black_18dp);
//        mHelpLiveo.addSeparator(); // Item separator
        mHelpLiveo.add(getString(R.string.feedback), R.drawable.ic_mail_black_24dp);
        mHelpLiveo.add(getString(R.string.about_us), R.drawable.ic_info_black_24dp);




        //{optional} - Header Customization - method customHeader
//        View mCustomHeader = getLayoutInflater().inflate(R.layout.custom_header_user, this.getListView(), false);
//        ImageView imageView = (ImageView) mCustomHeader.findViewById(R.id.imageView);

        with(this).startingPosition(0) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())

                        //{optional} - List Customization "If you remove these methods and the list will take his white standard color"
                        //.selectorCheck(R.drawable.selector_check) //Inform the background of the selected item color
                        //.colorItemDefault(R.color.nliveo_blue_colorPrimary) //Inform the standard color name, icon and counter
                        //.colorItemSelected(R.color.nliveo_purple_colorPrimary) //State the name of the color, icon and meter when it is selected
                        //.backgroundList(R.color.nliveo_black_light) //Inform the list of background color
                        //.colorLineSeparator(R.color.nliveo_transparent) //Inform the color of the subheader line

                        //{optional} - SubHeader Customization
                .colorItemSelected(R.color.colorPrimary)
                .colorNameSubHeader(R.color.colorPrimaryDark)
                        //.colorLineSeparator(R.color.nliveo_blue_colorPrimary)

                .footerItem(R.string.settings, R.drawable.ic_settings_black_24dp)
                        //.footerSecondItem(R.string.settings, R.mipmap.ic_settings_black_24dp)

                        //{optional} - Header Customization
                        //.customHeader(mCustomHeader)

                        //{optional} - Footer Customization
                        //.footerNameColor(R.color.nliveo_blue_colorPrimary)
                        //.footerIconColor(R.color.nliveo_blue_colorPrimary)
                        //.footerBackground(R.color.nliveo_white)

                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                        //.setOnClickFooterSecond(onClickFooter)
                .build();

        int position = this.getCurrentPosition();
        this.setElevationToolBar(position != 2 ? 15 : 0);




//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//
//        setupDrawerContent(navigationView);

        String DB_PATH = "";
        String DB_NAME ="irreg";// Database name

        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + getPackageName() + "/databases/";
        }

        String mPath = DB_PATH + DB_NAME;

        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(mPath, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
            dbcreator(); // calls the method to create or open database from vcf file in asset folder
        }


    }


    @Override
    public void onItemClick(int position) {
        Fragment mFragment = null;
        FragmentManager mFragmentManager = getSupportFragmentManager();

        switch (position){
            case 0:
                mFragment = new IrregularsFragment();
                break;
            case 1:
                mFragment = new AlphabetListFragment();
                break;
            case 3:
                shareApp();
                break;
            case 4:
                rateApp();
                break;
            case 5:
                feedback();
                break;
            case 6:
                mFragment = new AboutFragment();
                break;

            default:
                mFragment = new IrregularsFragment();
                break;
        }

        if (mFragment != null){
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }

        setElevationToolBar(position != 2 ? 15 : 0);
    }



//    public void selectDrawerItem(MenuItem menuItem) {
//        // Create a new fragment and specify the planet to show based on
//        // position
//        Fragment fragment = null;
//
//        Class fragmentClass = null;
//        switch(menuItem.getItemId()) {
//            case R.id.nav_irreg:
//                fragmentClass = IrregularsFragment.class;
//                break;
//            case R.id.nav_word_list:
//                fragmentClass = AlphabetListFragment.class;
//                break;
//            case R.id.nav_pref:
//                Intent i = new Intent(this,SettingsActivity.class);
//                startActivity(i);
//                break;
//            case R.id.nav_share:
//                shareApp();
//                break;
//            case R.id.nav_rate:
//                rateApp();
//                break;
//            case R.id.nav_feedback:
//                feedback();
//                break;
//            case R.id.nav_about:
//                fragmentClass = AboutFragment.class;
//                break;
//            default:
//                fragmentClass = IrregularsFragment.class;
//        }
//
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
//
//        // Highlight the selected item, update the title, and close the drawer
//        menuItem.setChecked(true);
//        setTitle(menuItem.getTitle());
//        drawer.closeDrawers();
//    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
        }
    };

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "onClickPhoto :D", Toast.LENGTH_SHORT).show();
            closeDrawer();
        }
    };

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), 1);
            closeDrawer();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                finish();
                startActivity(getIntent());
            }
        }
    }//onActivityResult

    //////////////******** STARTS INTENT WHICH CONTAINS A TEXT TO SHARE APP LINK ***********///////////////
    private void feedback(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"summer.arts.ir@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback - Irregulars");
//        intent.putExtra(Intent.EXTRA_TEXT, "I've found a problem in your app!");
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        }
        catch
                (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, R.string.no_email, Toast.LENGTH_SHORT).show();
        }
    }


    //////////////******** CREATES DATABASE AND IMPORTS DATA FROM CSV FILE (IN ASSETS FOLDER)***********///////////////
    private void dbcreator(){



        SQLiteDatabase db = openOrCreateDatabase("irreg", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS irregulars (verb TEXT, pt TEXT, ptp TEXT, pp TEXT, ppp TEXT, trans TEXT);");

        String mCSVfile = "irregulars.csv";
        AssetManager manager = this.getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));

        String line = "";
        db.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(",");
                ContentValues cv = new ContentValues(6);
                cv.put("verb", colums[0].trim());
                cv.put("pt", colums[1].trim());
                cv.put("ptp", colums[2].trim());
                cv.put("pp", colums[3].trim());
                cv.put("ppp", colums[4].trim());
                cv.put("trans", colums[5].trim());
                db.insert("irregulars", null, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }


    //////////////******** DOUBLE PRESS BACK BUTTON TO EXIT ***********///////////////
    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    public  void rateApp(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.web)));
        startActivity(browserIntent);
    }

    public void shareApp(){
        String shareBody = R.string.try_app + "\n" + R.string.web;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Irregulars");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share using..."));
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}
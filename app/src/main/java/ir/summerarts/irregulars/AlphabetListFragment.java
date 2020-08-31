package ir.summerarts.irregulars;


import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AlphabetListFragment extends android.support.v4.app.ListFragment/* implements OnItemClickListener*/ {

    private ArrayList<String> results = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_list, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        openAndQueryDatabase();

        displayResultList();
    }

    private void displayResultList() {
        String[] resultArray = new String[results.size()];
        resultArray = results.toArray(resultArray);
        setListAdapter(new AlphabetListAdapter(getActivity(), resultArray));
        getListView().setTextFilterEnabled(true);

    }

    private void openAndQueryDatabase() {
        try {
            String DB_PATH = "";
            String DB_NAME ="irreg";// Database name

            if(android.os.Build.VERSION.SDK_INT >= 17){
                DB_PATH = getContext().getApplicationInfo().dataDir + "/databases/";
            }
            else
            {
                DB_PATH = "/data/data/" + getContext().getPackageName() + "/databases/";
            }

            String mPath = DB_PATH + DB_NAME;

            SQLiteDatabase db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            Cursor c = db.rawQuery("SELECT * FROM irregulars ORDER BY verb ASC", null);

            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                        String verb = c.getString(c.getColumnIndex("verb"));
                        String pt = c.getString(c.getColumnIndex("pt"));
                        results.add(verb);
                    }while (c.moveToNext());
                }
            }
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }

    }
}

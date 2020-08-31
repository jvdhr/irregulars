package ir.summerarts.irregulars;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Javad on 10/30/2015.
 */
public class AlphabetListAdapter extends BaseAdapter implements SectionIndexer {

    String [] verb;
    Context context;
    private static LayoutInflater inflater=null;
    HashMap<String, Integer> mapIndex;
    String[] sections;

    private TextView ptense;
    private TextView ppart;
    private TextView ptp;
    private TextView ppp;
    private TextView trans;
    private TextView verbview;

    public AlphabetListAdapter (Activity a, String[] result) {
        mapIndex = new LinkedHashMap<>();
        verb = result;
        context = a;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int x = 0; x < verb.length; x++) {
            String v = verb[x];
            String ch = v.substring(0, 1);
            ch = ch.toUpperCase(Locale.US);

            // HashMap will prevent duplicates
            mapIndex.put(ch, x);
        }
        Set<String> sectionLetters = mapIndex.keySet();

        // create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

        Log.d("sectionList", sectionList.toString());
        Collections.sort(sectionList);

        sections = new String[sectionList.size()];

        sectionList.toArray(sections);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return verb.length;
    }


    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }

    public int getPositionForSection(int section) {
        Log.d("section", "" + section);
        return mapIndex.get(sections[section]);
    }

    public int getSectionForPosition(int position) {
        Log.d("position", "" + position);
        return 0;
    }

    public Object[] getSections() {
        return sections;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.listitemrow, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(verb[position]);
//        holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                displayResults(verb[position]);
            }
        });
        return rowView;
    }

    public void displayResults (String verb) {
        SQLiteDatabase db = context.openOrCreateDatabase("irreg", android.content.Context.MODE_PRIVATE, null);
        String sql = "SELECT * FROM irregulars WHERE verb='" + verb + "';";
        Cursor c;
        c = db.rawQuery(sql,null);
        c.moveToFirst();

        final Dialog rootView = new Dialog(context);

        rootView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView.setContentView(R.layout.dialog_custom);

        ptense = (TextView) rootView.findViewById(R.id.pt);
        ppart = (TextView) rootView.findViewById(R.id.pp);
        ptp = (TextView) rootView.findViewById(R.id.ptp);
        ppp = (TextView) rootView.findViewById(R.id.ppp);
        trans = (TextView) rootView.findViewById(R.id.trans);
        verbview = (TextView) rootView.findViewById(R.id.verb);;


        if (c.moveToFirst()) {
            do {
                final String pt = c.getString(c.getColumnIndex("pt"));
                final String pp = c.getString(c.getColumnIndex("pp"));
                final String ptphn = c.getString(c.getColumnIndex("ptp"));
                final String ppphn = c.getString(c.getColumnIndex("ppp"));
                final String translation = c.getString(c.getColumnIndex("trans"));

                ptense.setText("P.T: " + pt);
                ppart.setText("P.P: " + pp);

                trans.setText(translation);

                verbview.setText(verb);

                ptp.setText(ptphn);
                ppp.setText(ppphn);

            } while (c.moveToNext());
        }
        rootView.show();
    }

}

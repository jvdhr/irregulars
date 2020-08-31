package ir.summerarts.irregulars;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Javad on 10/26/2015.
 */
public class IrregularsFragment extends Fragment implements TextToSpeech.OnInitListener {

    public IrregularsFragment(){

    }

    private EditText txt;
    private TextView ptense;
    private TextView ppart;
    private TextView ptp;
    private TextView ppp;
    private TextView trans;
    private TextToSpeech tts;
    private ImageButton pptalk;
    private ImageButton pttalk;
    private TextView verbview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_irregulars, container, false);



        tts = new TextToSpeech(getContext(), this);

        txt = (EditText) rootView.findViewById(R.id.inputSearchEditText);
        ptense = (TextView) rootView.findViewById(R.id.pt);
        ppart = (TextView) rootView.findViewById(R.id.pp);
        ptp = (TextView) rootView.findViewById(R.id.ptp);
        ppp = (TextView) rootView.findViewById(R.id.ppp);
        trans = (TextView) rootView.findViewById(R.id.trans);
        Button btn = (Button) rootView.findViewById(R.id.button);
        pptalk = (ImageButton) rootView.findViewById(R.id.pptalk);
        pptalk.setVisibility(View.INVISIBLE);
        pttalk = (ImageButton) rootView.findViewById(R.id.pttalk);
        pttalk.setVisibility(View.INVISIBLE);
        verbview = (TextView) rootView.findViewById(R.id.verb);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

//        final int textColor = settings.getInt("textColor", Color.BLACK);
//        final int textSize = settings.getInt("textSize", 20);
        final boolean phn = settings.getBoolean("phn", true);
        final boolean trn = settings.getBoolean("trn", true);

//        ((MainActivity)getActivity()).getPrefs();

//        ptense.setTextSize(textSize);
//        ppart.setTextColor(textColor);
//        ppart.setTextSize(textSize);
//        ptense.setTextColor(textColor);
//        ppp.setTextSize(textSize - 2);
//        ppp.setTextColor(textColor);
//        ptp.setTextSize(textSize - 2);
//        ptp.setTextColor(textColor);
//        trans.setTextColor(textColor);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///hide keyboard on button press
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                ///hide keyboard on button press

                String verb = txt.getText().toString();
                SQLiteDatabase db = getActivity().openOrCreateDatabase("irreg", android.content.Context.MODE_PRIVATE, null);
                String sql = "SELECT * FROM irregulars WHERE verb='" + verb + "';";
                Cursor c;
                c = db.rawQuery(sql, null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    if (c.moveToFirst()) {
                        do {
                            final String pt = c.getString(c.getColumnIndex("pt"));
                            final String pp = c.getString(c.getColumnIndex("pp"));
                            final String ptphn = c.getString(c.getColumnIndex("ptp"));
                            final String ppphn = c.getString(c.getColumnIndex("ppp"));
                            final String translation = c.getString(c.getColumnIndex("trans"));

                            ptense.setText("P.T: " + pt);
                            ppart.setText("P.P: " + pp);
                            if (trn) {
                                trans.setText(translation);
                            }
                            verbview.setText(verb);
                            txt.setText(null);
                            if (phn) {
                                ptp.setText(ptphn);
                                ppp.setText(ppphn);
                            }
                            pptalk.setVisibility(View.VISIBLE);
                            pttalk.setVisibility(View.VISIBLE);
                            pttalk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tts.speak(pt, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            });
                            pptalk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tts.speak(pp, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            });
                        } while (c.moveToNext());
                    }
                } else {
                    trans.setText(verb + " " + getString(R.string.notfound));
                    ptense.setText("");
                    ppart.setText("");
                    ppp.setText("");
                    ptp.setText("");
                    verbview.setText("");
                    pptalk.setVisibility(View.INVISIBLE);
                    pttalk.setVisibility(View.INVISIBLE);
                }

            }
        });

            return rootView;
        }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
//                spk.setEnabled(true);
//                speakOut();
            }

        } else {
            Log.e("TTS", getString(R.string.ttsfailure));
            Toast.makeText(getActivity(), getString(R.string.ttsfailure), Toast.LENGTH_SHORT).show();
        }

    }
}

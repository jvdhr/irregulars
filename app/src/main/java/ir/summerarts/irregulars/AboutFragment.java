package ir.summerarts.irregulars;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends android.support.v4.app.Fragment {

	public AboutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        TextView tv = (TextView) rootView.findViewById(R.id.textView2);
        tv.setClickable(true);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://cafebazaar.ir/app/ir.summerarts.irregulars/?l=fa'> Irregulars on CafeBazaar </a>";
        tv.setText(Html.fromHtml(text));

		return rootView;
	}

}

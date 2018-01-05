package cm.isma.hangman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.Locale;

/**
 * Created by isma-ilou on 05.01.2018.
 */

public class LetterAdapter extends BaseAdapter {
    private String[] mLetters;
    private LayoutInflater mLetterInf;

    public LetterAdapter(Context c) {

        if( Locale.getDefault().getLanguage().contentEquals("tr")){
            mLetters=new String[32];
            for (int a = 0; a < 26; a++) {
                mLetters[a] = "" + (char)(a+'A');
            }
            mLetters[26] = "" + (char)('Ğ');
            mLetters[27] = "" + (char)('Ü');
            mLetters[28] = "" + (char)('Ş');
            mLetters[29] = "" + (char)('İ');
            mLetters[30] = "" + (char)('Ö');
            mLetters[31] = "" + (char)('Ç');
            mLetterInf = LayoutInflater.from(c);
        }
        else
        {
            mLetters=new String[26];
            for (int a = 0; a < mLetters.length; a++) {
                mLetters[a] = "" + (char)(a+'A');
                mLetterInf = LayoutInflater.from(c);
            }
        }
    }

    @Override
    public int getCount() {
        return mLetters.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button letterBtn;
        if (convertView == null) {
            letterBtn = (Button)mLetterInf.inflate(R.layout.letter, parent, false);
        } else {
            letterBtn = (Button) convertView;
        }
        letterBtn.setText(mLetters[position]);
        return letterBtn;
    }
}

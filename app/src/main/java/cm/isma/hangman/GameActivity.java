package cm.isma.hangman;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity {
    private String[] mWords;
    private Random mRand;
    private String mCurrWord;
    private LinearLayout mWordLayout;
    private TextView[] mCharViews;
    private LetterAdapter mLetterAdapter;
    @BindView(R.id.letters)
    GridView mLetters;
    private AlertDialog helpAlert;

    private ImageView[] mBodyParts;
    private final int mNumParts=6;
    private int mCurrPart;
    private int mNumChars;
    private int mNumCorr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        Resources res = getResources();
        mWords = res.getStringArray(R.array.words);
        mRand = new Random();
        mCurrWord = "";
        mWordLayout = (LinearLayout)findViewById(R.id.word);

        mBodyParts = new ImageView[mNumParts];
        mBodyParts[0] = (ImageView)findViewById(R.id.head);
        mBodyParts[1] = (ImageView)findViewById(R.id.body);
        mBodyParts[2] = (ImageView)findViewById(R.id.arm1);
        mBodyParts[3] = (ImageView)findViewById(R.id.arm2);
        mBodyParts[4] = (ImageView)findViewById(R.id.leg1);
        mBodyParts[5] = (ImageView)findViewById(R.id.leg2);

        playGame();
    }

    private void playGame(){
        String newWord = mWords[mRand.nextInt(mWords.length)];
        while(newWord.equals(mCurrWord)) newWord = mWords[mRand.nextInt(mWords.length)];
        mCurrWord = newWord;
        mCurrPart = 0;
        mNumChars = mCurrWord.length();
        mNumCorr = 0;

        for(int p = 0; p < mNumParts; p++) {
            mBodyParts[p].setVisibility(View.INVISIBLE);
        }

        mCharViews = new TextView[mCurrWord.length()];
        mWordLayout.removeAllViews();

        for (int c = 0; c < mCurrWord.length(); c++) {
            mCharViews[c] = new TextView(this);
            mCharViews[c].setText(""+mCurrWord.charAt(c));
            mCharViews[c].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mCharViews[c].setGravity(Gravity.CENTER);
            mCharViews[c].setTextColor(Color.WHITE);
            mCharViews[c].setBackgroundResource(R.drawable.letter_bg);
            mCharViews[c].setId(c);

            mWordLayout.addView(mCharViews[c]);
        }

        mLetterAdapter = new LetterAdapter(this);
        mLetters.setAdapter(mLetterAdapter);
    }

    public void letterClicked(View view) {
        String letter=((TextView)view).getText().toString();
        char letterChar = letter.charAt(0);
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_down);

        boolean correct = false;
        for(int k = 0; k < mCurrWord.length(); k++) {
            if(mCurrWord.charAt(k)==letterChar){
                correct = true;
                mNumCorr++;
                TextView charView = (TextView) mWordLayout.getChildAt(k);
                charView.setTextColor(Color.BLACK);
            }
        }

        if (correct){
            if (mNumCorr == mNumChars) {
                disableBtns();
                AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
                winBuild.setTitle(R.string.congrats);
                winBuild.setMessage(getResources().getString(R.string.win_message) + mCurrWord);
                winBuild.setPositiveButton(R.string.play_again,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.playGame();
                            }});

                winBuild.setNegativeButton(R.string.exit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.finish();
                            }});

                winBuild.show();
            }
        }
        else{
            if (mCurrPart < mNumParts) {
                mBodyParts[mCurrPart].setVisibility(View.VISIBLE);
                mCurrPart++;
            }
            if (mCurrPart == mNumParts)
            {
                disableBtns();
                AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
                loseBuild.setTitle(R.string.lost);
                loseBuild.setMessage(getResources().getString(R.string.lost_message) + mCurrWord);
                loseBuild.setPositiveButton(R.string.play_again,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.playGame();
                            }});

                loseBuild.setNegativeButton(R.string.exit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.finish();
                            }});

                loseBuild.show();
            }
        }
    }

    public void disableBtns() {
        int numLetters = mLetters.getChildCount();
        for (int l = 0; l < numLetters; l++) {
            mLetters.getChildAt(l).setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_help:
                showHelp();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showHelp() {
        AlertDialog.Builder helpBuild = new AlertDialog.Builder(this);

        helpBuild.setTitle(R.string.help);
        helpBuild.setMessage(R.string.help_message);
        helpBuild.setPositiveButton(R.string.positive_message,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        helpAlert.dismiss();
                    }});
        helpAlert = helpBuild.create();

        helpBuild.show();
    }
}

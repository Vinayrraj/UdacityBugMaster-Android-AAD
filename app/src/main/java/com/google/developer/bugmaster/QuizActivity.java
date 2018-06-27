package com.google.developer.bugmaster;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.developer.bugmaster.data.Insect;
import com.google.developer.bugmaster.views.AnswerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements
        AnswerView.OnAnswerSelectedListener {
    private static final String TAG = QuizActivity.class.getSimpleName();

    //Number of quiz answers
    public static final int ANSWER_COUNT = 5;

    public static final String EXTRA_INSECTS = "insectList";
    public static final String EXTRA_ANSWER = "selectedInsect";
    public static final String EXTRA_USER_SELECTION = "user_selection";

    private TextView mQuestionText;
    private TextView mCorrectText;
    private AnswerView mAnswerSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionText = (TextView) findViewById(R.id.text_question);
        mCorrectText = (TextView) findViewById(R.id.text_correct);
        mAnswerSelect = (AnswerView) findViewById(R.id.answer_select);

        mAnswerSelect.setOnAnswerSelectedListener(this);

        List<Insect> insects = getIntent().getParcelableArrayListExtra(EXTRA_INSECTS);
        Insect selected = getIntent().getParcelableExtra(EXTRA_ANSWER);

        List<Insect> questionSet = insects.subList(0, ANSWER_COUNT - 1);
        questionSet.add(selected);
        Collections.shuffle(questionSet);
        buildQuestion(questionSet, selected);

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_USER_SELECTION)) {
            Integer selectedIndex = savedInstanceState.getInt(EXTRA_USER_SELECTION);
            mAnswerSelect.setCheckedIndex(selectedIndex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_USER_SELECTION, mAnswerSelect.getCheckedIndex());
        super.onSaveInstanceState(outState);
    }

    private void buildQuestion(List<Insect> insects, Insect selected) {
        String question = getString(R.string.question_text, selected.name);
        mQuestionText.setText(question);

        //Load answer strings
        ArrayList<String> options = new ArrayList<>();
        for (Insect item : insects) {
            options.add(item.scientificName);
        }

        mAnswerSelect.loadAnswers(options, selected.scientificName);
    }

    /* Answer Selection Callbacks */

    @Override
    public void onCorrectAnswerSelected() {
        updateResultText();
    }

    @Override
    public void onWrongAnswerSelected() {
        updateResultText();
    }

    private void updateResultText() {
        mCorrectText.setTextColor(mAnswerSelect.isCorrectAnswerSelected() ?
                ContextCompat.getColor(getBaseContext(), R.color.colorCorrect) : ContextCompat.getColor(getBaseContext(), R.color.colorWrong)
        );
        mCorrectText.setText(mAnswerSelect.isCorrectAnswerSelected() ?
                R.string.answer_correct : R.string.answer_wrong
        );
    }
}

package wekinator.epsi.com.wekinator;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wekinator.epsi.com.wekinator.controler.AddController;
import wekinator.epsi.com.wekinator.controler.DBController;
import wekinator.epsi.com.wekinator.controler.ScoreController;
import wekinator.epsi.com.wekinator.data.Actor;
import wekinator.epsi.com.wekinator.data.Answer;
import wekinator.epsi.com.wekinator.data.Question;
import wekinator.epsi.com.wekinator.data.User;


public class AddActivity extends Activity {


    private ScoreController scoreController;
    private AddController addController;
    private User user;
    private boolean nameWasTyped = false;
    private boolean isFinished = false;

    private TextView actorNameLabel;
    private EditText actorNameText;

    private TextView actorNewQuestionLabel;
    private EditText actorNewQuestionText;

    private TextView actorNewAnswerLabel;
    private EditText actorNewAnswerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        addController = new AddController();
        scoreController = (ScoreController) getIntent().getSerializableExtra("scoreController");
        scoreController.getActorList().add(addController.getActor());
        user = (User) getIntent().getSerializableExtra("user");
        addController.fillUserAnswer(user);

        actorNameLabel = (TextView) findViewById(R.id.actorNameLabel);
        actorNameText = (EditText) findViewById(R.id.actorNameText);

        actorNewQuestionLabel = (TextView) findViewById(R.id.actorNewQuestionLabel);
        actorNewQuestionText = (EditText) findViewById(R.id.actorNewQuestionText);

        actorNewAnswerLabel = (TextView) findViewById(R.id.actorNewAnswerLabel);
        actorNewAnswerText = (EditText) findViewById(R.id.actorNewAnswerText);
    }

    private void switchToQuestion() {
        actorNameLabel.setVisibility(View.GONE);
        actorNameText.setVisibility(View.GONE);

        actorNewQuestionLabel.setVisibility(View.VISIBLE);
        actorNewQuestionText.setVisibility(View.VISIBLE);

        actorNewAnswerLabel.setVisibility(View.VISIBLE);
        actorNewAnswerText.setVisibility(View.VISIBLE);
        nameWasTyped = true;
    }

    private void validateQuestions() {
        actorNewQuestionLabel.setVisibility(View.VISIBLE);
        actorNewQuestionLabel.setText("Nous avons ajouté cet acteur");
        actorNewQuestionText.setVisibility(View.GONE);

        actorNewAnswerLabel.setVisibility(View.GONE);
        actorNewAnswerText.setVisibility(View.GONE);
        isFinished = true;
    }

    private void saveNewActor() {
        Actor newActor = addController.getActor();
        List<Question> newQuestionList = questionThatDoesNotExits(user, newActor);
        DBController db = new DBController(this);
        db.addNewActor(newActor, newQuestionList);
    }

    public void actionButton(View view)
    {
        if (!nameWasTyped) {
            String name = actorNameText.getText().toString();
            addController.setActorName(name);
            switchToQuestion();
        } else if (!isFinished) {
            String questionName = actorNewQuestionText.getText().toString();
            String answerValuer = actorNewAnswerText.getText().toString();
            Answer answer = addController.addAnswer(questionName, answerValuer);

            /*user.getAnswer().add(answer); // WARNNING CREATE DUPLICATE INTO THE NEW ACTOR
            scoreController.reloadActorScore();
            List<Actor> majorActor = scoreController.getMajorActorList();
            if (majorActor.contains(addController.getActor())) {*/
                saveNewActor();
                validateQuestions();
            /*} else {
                actorNewQuestionLabel.setText("Entrez une nouvelle question :");
                actorNewQuestionText.setText("");
                actorNewAnswerText.setText("");
            }*/
        } else {
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private List<Question> questionThatDoesNotExits(User user, Actor newActor) {
        List<Question> questionList = new ArrayList<>();
        List<Question> originilaQuestionList = (new DBController(this)).getQuestionList();
        for (Answer answer : newActor.getAnswerList()) {
            Question question = answer.getQuestion();
            boolean isFound = false;
            for (int i = 0; i < originilaQuestionList.size() && isFound == false; i++) {
                if (originilaQuestionList.get(i).getName().equalsIgnoreCase(question.getName())) {
                    isFound = true;
                }
            }
            if (!isFound) {
                questionList.add(question);
            }
        }
        return questionList;
    }
}

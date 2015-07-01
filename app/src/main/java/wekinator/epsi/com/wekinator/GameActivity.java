package wekinator.epsi.com.wekinator;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wekinator.epsi.com.wekinator.controler.DBController;
import wekinator.epsi.com.wekinator.controler.QuestionFinder;
import wekinator.epsi.com.wekinator.controler.ScoreController;
import wekinator.epsi.com.wekinator.data.Actor;
import wekinator.epsi.com.wekinator.data.Answer;
import wekinator.epsi.com.wekinator.data.Question;
import wekinator.epsi.com.wekinator.data.User;


public class GameActivity extends Activity {

    private TextView questionNumber;
    private TextView questionLabel;
    private User user;
    private QuestionFinder questionFinder;
    private ScoreController scoreController;
    private Question currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DBController db = new DBController(this);
        List<Actor> actorList = new ArrayList<Actor>();
        try {
            actorList = db.getAllActor();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            actorList = new ArrayList<Actor>();
        } catch (IOException e) {
            e.printStackTrace();
            actorList = new ArrayList<Actor>();
        }


        questionNumber = (TextView) findViewById(R.id.questionNumber);
        questionLabel = (TextView) findViewById(R.id.questionLabel);
        user = new User();
        questionFinder = new QuestionFinder(user, actorList);
        scoreController = new ScoreController(user, actorList);
        displayNextQuestion();
    }

    private void displayNextQuestion() {

        int newQuestionNumber = user.getAnswer().size() + 1;
        questionNumber.setText("Question n° " + newQuestionNumber);
        currentQuestion = questionFinder.findNextQuestion();
        if (currentQuestion == null) {
            actorFound();
        } else {
            questionLabel.setText(currentQuestion.getText());
        }
    }

    public void yesButton(View view)
    {
        buttonPushed(1);
    }

    public void noButton(View view)
    {
        buttonPushed(5);
    }

    public void dontKnowButton(View view)
    {
        buttonPushed(3);
    }

    public void probablyYesButton(View view)
    {
        buttonPushed(2);
    }

    public void probablyNoButton(View view)
    {
        buttonPushed(4);
    }

    private void buttonPushed(int buttonValue)
    {

        Answer newAnswer = new Answer();
        newAnswer.setNumber(buttonValue);
        newAnswer.setQuestion(currentQuestion);

        user.getAnswer().add(newAnswer);
        scoreController.addAnswersScore(newAnswer);
        if (user.getAnswer().size() > 8 && scoreController.majorExist()){
            actorFound();
        }
        else{
            displayNextQuestion();
        }
    }

    public void actorFound()
    {
        if (scoreController.getMajorActorList().size() == 0) {
            scoreController.populateBestActor();
        }
        Intent intent = new Intent(GameActivity.this, SuccessActivity.class);
        intent.putExtra("scoreController", scoreController);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void goToPrevious(View view) {
        if (user.getAnswer().size() > 1) {
            user.getAnswer().remove(user.getAnswer().size() - 1);
            scoreController.reloadActorScore();
            displayNextQuestion();
        } else if (user.getAnswer().size() > 0) {
            Answer a = user.getAnswer().get(0);
            user.getAnswer().remove(user.getAnswer().size() - 1);
            scoreController.reloadActorScore();
            questionNumber.setText("Question n° " + 1);
            currentQuestion = a.getQuestion();
            questionLabel.setText(currentQuestion.getText());
        }
    }
}

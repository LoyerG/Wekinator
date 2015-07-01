package wekinator.epsi.com.wekinator;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import wekinator.epsi.com.wekinator.R;
import wekinator.epsi.com.wekinator.controler.ScoreController;
import wekinator.epsi.com.wekinator.data.User;

public class SuccessActivity extends Activity {


    private ScoreController scoreController;
    private TextView actorLabel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        actorLabel = (TextView) findViewById(R.id.actorFound);
        scoreController = (ScoreController) getIntent().getSerializableExtra("scoreController");
        user = (User) getIntent().getSerializableExtra("user");
        String name = scoreController.getMajorActorList().get(0).getName();
        if (name != null) {
            actorLabel.setText("Acteur trouvé: " + scoreController.getMajorActorList().get(0).getName());
        } else {
            actorLabel.setText("Nous n'avons pas trouvé d'acteur, voulez vous l'ajouter ?");
        }
    }

    public void goToMenu(View view)
    {
        Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void goToAdd(View view)
    {
        Intent intent = new Intent(SuccessActivity.this, AddActivity.class);
        intent.putExtra("scoreController", scoreController);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}

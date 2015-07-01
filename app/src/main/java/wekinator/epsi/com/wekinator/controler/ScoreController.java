package wekinator.epsi.com.wekinator.controler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wekinator.epsi.com.wekinator.data.Actor;
import wekinator.epsi.com.wekinator.data.Answer;
import wekinator.epsi.com.wekinator.data.Question;
import wekinator.epsi.com.wekinator.data.User;

/**
 * Created by Slyzz on 11/02/2015.
 */
public class ScoreController implements Serializable {
    public final static int MIN_ECART = 10;
    public final static int MAX_SCORE = 35;
    public final static int MIN_SCORE = 0;
    User user;
    List<Actor> actorList;
    List<Actor> majorActorList;

    public ScoreController(User user, List<Actor> actorList) {
        this.user = user;
        this.actorList = actorList;
        this.majorActorList = new ArrayList<Actor>();
        purgeActorScore();
    }

    public void populateBestActor() {
        Actor bestActor = actorList.get(0);
        for (Actor actor : actorList) {
            if (actor.getScore() > bestActor.getScore()) {
                bestActor = actor;
            }
        }
        majorActorList.add(0, bestActor);
    }

    public void purgeActorScore() {
        for (Actor actor : this.actorList) {
            actor.setScore(0);
        }
        this.majorActorList.clear();
    }

    public List<Actor> getActorList() {
        return actorList;
    }

    public void addAnswersScore(Answer answer) {
        this.majorActorList.clear();
        for (Actor actor : this.actorList) {
            int value = calculateUserAnswerValue(answer, actor);
            actor.setScore(actor.getScore() + value);
            if (actor.getScore() >= MAX_SCORE) {
                int indexFound = findBestIndexFromScore(majorActorList, actor);
                this.majorActorList.add(indexFound, actor);
            }
        }
    }

    private int findBestIndexFromScore(List<Actor> actorList, Actor actorToAdd) {
        int bestIndex = -1;
        for (int i = 0; i < actorList.size() && bestIndex < 0; i++) {
            Actor actor = actorList.get(i);
            if (actorToAdd.getScore() > actor.getScore()) {
                bestIndex = i;
            }
        }
        if (bestIndex == -1) {
            bestIndex = actorList.size();
        }
        return bestIndex;
    }

    public void reloadActorScore() {
        purgeActorScore();
        for (Answer answer : this.user.getAnswer()) {
            addAnswersScore(answer);
        }
    }

    private int calculateUserAnswerValue(Answer answer, Actor actor) {
        Question answerQuestion = answer.getQuestion();
        int userAnswerNumber = answer.getNumber();
        int neededAnswerNumber = 3;
        for (Answer actorAnswer : actor.getAnswerList()) {
            if (answerQuestion.getName().equals(actorAnswer.getQuestion().getName())) {
                neededAnswerNumber = actorAnswer.getNumber();
                break;
            }
        }
        int val = 0;
        if (neededAnswerNumber > 0) {
            int diff = Math.abs(neededAnswerNumber - userAnswerNumber);
            switch (diff) {
                case 0:
                    val = 3;
                    break;
                case 1:
                    val = 1;
                    break;
                case 2:
                    val = -1;
                    break;
                case 3:
                    val = -2;
                    break;
                case 4:
                    val = -3;
                    break;
            }
        }

        return val;
    }

    public boolean majorExist() {
        //return this.majorActorList.size() > 0;
        boolean exist = false;
        if (this.majorActorList.size() == 1) {
            exist = true;
        } else if (this.majorActorList.size() > 1) {
            Actor actorOne = this.majorActorList.get(0);
            Actor actorTwo = this.majorActorList.get(1);
            if (actorOne.getScore() - actorTwo.getScore() >= MIN_ECART) {
                exist = true;
            }
        }
        return exist;
    }

    public List<Actor> getMajorActorList() {
        return this.majorActorList;
    }
}

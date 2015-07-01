package wekinator.epsi.com.wekinator.controler;

import java.util.ArrayList;
import java.util.List;

import wekinator.epsi.com.wekinator.data.Actor;
import wekinator.epsi.com.wekinator.data.Answer;
import wekinator.epsi.com.wekinator.data.Question;
import wekinator.epsi.com.wekinator.data.User;

/**
 * Created by WuzUrDaddy on 21/04/2015.
 */


public class AddController {
    private Actor actor;


    public AddController(){
        actor = new Actor();
    }

    public Actor getActor(){
        return this.actor;
    }

    public void setActor(Actor newActor){
        this.actor = newActor;
    }

    public void setActorName(String actorName){
        this.actor.setName(actorName);
    }

    public void fillUserAnswer(User user){
        List<Answer> tmpList = new ArrayList<>(user.getAnswer());
        actor.setAnswerList(tmpList);
    }

    public Answer addAnswer(String newQuestion, String newAnswer){
        Answer answer = new Answer();
        Question question = new Question();
        question.setName(newQuestion);
        question.setText(newQuestion);
        answer.setQuestion(question);
        answer.setNumber(Integer.parseInt(newAnswer));
        actor.getAnswerList().add(answer);
        return answer;
    }
}

package wekinator.epsi.com.wekinator.controler;

import android.support.v7.appcompat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wekinator.epsi.com.wekinator.data.Actor;
import wekinator.epsi.com.wekinator.data.Answer;
import wekinator.epsi.com.wekinator.data.Question;
import wekinator.epsi.com.wekinator.data.User;

/**
 * Created by Mélanie on 11/03/15.
 */
public class QuestionFinder {
    public final static int RANDOM_LIMIT_FOR_FIRST = 20;
    User user;
    List<Actor> actorList;
    List<Question> allQuestion;

    public QuestionFinder(User user, List<Actor> actorList){
        this.user = user;
        this.actorList = actorList;
        this.allQuestion = new ArrayList<Question>();
        this.refreshAllQuestion();
    }

    public Question findNextQuestion(){
        Question choosenQuestion = null;
        if (user.getAnswer().size() > 0) {
            List<Question> questionList = findMostValuableQuestion(1);
            if (questionList.size() > 0) {
                choosenQuestion = questionList.get(0);
            } else {
                return null;
            }

        } else {
            Random rand = new Random();
            int n = rand.nextInt(RANDOM_LIMIT_FOR_FIRST);
            List<Question> questionList = findMostValuableQuestion(RANDOM_LIMIT_FOR_FIRST);
            if (questionList.size() < n) {
                n = questionList.size() - 1;
            }
            choosenQuestion = questionList.get(n);
        }
        return choosenQuestion;
    }

    public List<Question> findMostValuableQuestion(int limit) {
        List<Question> choosenQuestionList = new ArrayList<Question>(limit);
        for (Question question  : this.allQuestion){
            if (questionAlreadyAsked(question) == false) {
                int score = calcScoreForQuestion(question);
                question.setScore(score);
                int indexToAdd = findBestIndexFromScore(choosenQuestionList, question);
                choosenQuestionList.add(indexToAdd, question);

                if (choosenQuestionList.size() > limit) {
                        choosenQuestionList.remove(choosenQuestionList.size()-1);
                }
            }
        }
        return choosenQuestionList;
    }

    private int findBestIndexFromScore(List<Question> questionList, Question questionToAdd) {
        int bestIndex = -1;
        for (int i = 0; i < questionList.size() && bestIndex < 0; i++) {
            Question question = questionList.get(i);
            if (questionToAdd.getScore() > question.getScore()) {
                bestIndex = i;
            }
        }
        if (bestIndex == -1) {
            bestIndex = questionList.size();
        }
        return bestIndex;
    }

    private int findLessValuableQuestion(List<Question> questionList, Question question) {
        int lessIndex = -1;
        for (int i = 0; i < questionList.size(); i++) {
            Question currentQuestion = questionList.get(i);
            if (currentQuestion.getScore() < question.getScore()
                    && (lessIndex > -1 && questionList.get(lessIndex).getScore() > currentQuestion.getScore())) {
                lessIndex = i;
            }
        }
        return lessIndex;
    }

    private int calcScoreForQuestion(Question question) {
        List<Actor> tmpActor = new ArrayList<Actor>();
        tmpActor.addAll(actorList);
        int score = 1;
        for (int i = 1; i < 6; i++) {
            int numberActor = foundActorForAnswer(i, question, tmpActor);
            score *= (numberActor + 1);
        }
        return score;
    }

    private int foundActorForAnswer(int idAnswer, Question question, List<Actor> tmpActor) {
        int number = 0;
        List<Integer> indexToRemove = new ArrayList<Integer>();
        for (int i = 0; i < tmpActor.size(); i++) {
            Actor actor = tmpActor.get(i);
            if (actor.getScore() > ScoreController.MIN_SCORE) {
                Answer actorAnswer = null;
                for(Answer answer : actor.getAnswerList()){
                    if(answer.getQuestion().getName().equalsIgnoreCase(question.getName())){
                        actorAnswer = answer;
                        break;
                    }
                }
                if(actorAnswer.getNumber() == idAnswer){
                    number++;
                    indexToRemove.add(i);
                }
            }
        }

        for (Integer index : indexToRemove) {
            tmpActor.remove(index);
        }
        return number;
    }

    private void refreshAllQuestion() {
        this.allQuestion.clear();
        if (actorList.size() > 0) {
            for (Answer actorAnswer : actorList.get(0).getAnswerList()) {
                this.allQuestion.add(actorAnswer.getQuestion());
            }
        }
    }

    private boolean questionAlreadyAsked(Question question){
        for (Answer answer : user.getAnswer()) {
            if (answer.getQuestion() == question) {
                return true;
            }
        }
        return false;
    }
}

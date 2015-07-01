package wekinator.epsi.com.wekinator.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mélanie on 11/02/2015.
 */
public class Actor implements Serializable {
    private String name;
    private int score;
    private List<Answer> answerList;

    public Actor() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public List<Answer> getAnswerList() {
        return this.answerList;
    }

    public void setAnswerList(List<Answer> newAnswerList) {
        this.answerList = newAnswerList;
    }

    public String toXmlString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<Personne>");
        sb.append("<Name>");
        sb.append(getName());
        sb.append("</Name>");

        sb.append("<ReponseListe>");
        for (Answer answer : answerList) {
            sb.append(answer.toXmlString());
        }
        sb.append("</ReponseListe>");

        sb.append("</Personne>");
        return sb.toString();
    }
}

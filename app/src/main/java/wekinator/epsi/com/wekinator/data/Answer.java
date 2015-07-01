package wekinator.epsi.com.wekinator.data;

import java.io.Serializable;

/**
 * Created by Mélanie on 11/02/2015.
 */
public class Answer implements Serializable {
    private String text;
    private int number;
    private Question question;

   public Answer() {

    }

    public String getText() {
        return this.text;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int newNumber) {
        this.number = newNumber;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question newQuestion) {
        this.question = newQuestion;
    }

    public String toXmlString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<Reponse>");

        sb.append("<Question>");
        sb.append(getQuestion().getName());
        sb.append("</Question>");

        sb.append("<Valeur>");
        sb.append(getNumber());
        sb.append("</Valeur>");

        sb.append("</Reponse>");
        return sb.toString();
    }
}

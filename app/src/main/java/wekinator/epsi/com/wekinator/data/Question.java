package wekinator.epsi.com.wekinator.data;

import java.io.Serializable;

/**
 * Created by Mélanie on 11/02/2015.
 */
public class Question implements Serializable {
    private String text;
    private String name;
    private int score;

    public Question() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public String toXmlString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<Question>");
        sb.append("<Libelle>");
        sb.append(getName());
        sb.append("</Libelle>");
        sb.append("</Question>");
        return sb.toString();
    }
}

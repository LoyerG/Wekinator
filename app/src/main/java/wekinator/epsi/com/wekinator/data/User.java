package wekinator.epsi.com.wekinator.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mélanie on 11/02/2015.
 */
public class User implements Serializable {
    private String name;
    private int age;
    private boolean isMale;
    private List<Answer> answer;

    public User(){
        answer = new ArrayList<>();

    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int newAge) {
        this.age = newAge;
    }

    public boolean IsMale() {
        return this.isMale;
    }

    public void setIsMale(Boolean newIsMale) {
        this.isMale = newIsMale;
    }

    public List<Answer> getAnswer() {
        return this.answer;
    }

    public void setAnswer(List<Answer> newAnswer) {
        this.answer = newAnswer;
    }
}

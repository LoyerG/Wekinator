package wekinator.epsi.com.wekinator.controler;

import android.content.Context;
import android.content.SharedPreferences;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import wekinator.epsi.com.wekinator.R;
import wekinator.epsi.com.wekinator.data.Actor;
import wekinator.epsi.com.wekinator.data.Answer;
import wekinator.epsi.com.wekinator.data.Question;

/**
 * Created by Slyzz on 21/04/2015.
 */
public class DBController {
    private XmlPullParserFactory xmlFactoryObject;
    private XmlPullParser myparser;
    private List<Question> questionList = new ArrayList<Question>();
    private String ns;
    private Context context;

    public DBController(Context ctx) {
        super();
        context = ctx;
        saveDBIfNeeded();
    }

    private void initialize() {
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            myparser = xmlFactoryObject.newPullParser();
            loadQuestion();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Question> getQuestionList() {
        initialize();
        return questionList;
    }
    private void loadQuestion() throws XmlPullParserException, IOException {
        questionList.clear();
        Question question = new Question();
        myparser.setInput(getSreamReaderForQuestion());
        //myparser.require(XmlPullParser.START_DOCUMENT, ns, "QuestionListe");

        while (myparser.next() != XmlPullParser.END_DOCUMENT) {
            String name = myparser.getName();
            if (myparser.getEventType() == XmlPullParser.END_TAG && name.equals("Question")) {
                questionList.add(question);
                question = new Question();
            }
            if (myparser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (name.equals("Libelle")) {
                String text = readLibelle(myparser);
                question.setName(text);
                question.setText(text);
            }

        }
    }

    public List<Actor> getAllActor() throws XmlPullParserException, IOException {
        this.initialize();

        List<Actor> actorList = new ArrayList<Actor>();
        List<Answer> answerList = null;
        Actor acteur = null;
        myparser.setInput(getSreamReaderForActor());
        //myparser.require(XmlPullParser.START_DOCUMENT, ns, "PersonneListe");

        while (myparser.next() != XmlPullParser.END_DOCUMENT) {
            String name = myparser.getName();
            int type = myparser.getEventType();
            if (type == XmlPullParser.START_TAG) {
                if (name.equals("Personne")) {
                    acteur = new Actor();
                } else if (name.equals("Name")) {
                    acteur.setName(readName(myparser));
                } else if (name.equals("ReponseListe")) {
                    answerList = new ArrayList<Answer>();
                } else if (name.equals("Reponse")) {
                    answerList.add(readReponse(myparser));
                }
            } else if (type == XmlPullParser.END_TAG) {
                if (name.equals("ReponseListe")) {
                    acteur.setAnswerList(answerList);
                } else if (name.equals("Personne")) {
                    actorList.add(acteur);
                    fillActorAnswerForUnkown(acteur);
                    acteur = new Actor();
                }
            }
        }
        return actorList;
    }

    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Name");
        String title = readText(parser);
        return title;
    }

    private String readLibelle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Libelle");
        String libelle = readText(parser);
        return libelle;
    }

    private Answer readReponse(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Reponse");
        Answer reponse = new Answer();
        String questionVal = null;
        int val = 0;
        while (myparser.next() != XmlPullParser.END_DOCUMENT && val < 2) {
            if (myparser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = myparser.getName();
            if (name.equals("Question")) {
                questionVal = readText(myparser);
                val++;
            } else if (name.equals("Valeur")) {
                reponse.setNumber(Integer.parseInt(readText(myparser)));
                val++;
            }
        }

        Question question = getQuestionFromAnswer(questionVal);
        reponse.setQuestion(question);
        return reponse ;
    }

    private Question getQuestionFromAnswer(String questionXml) {
        for (Question question : questionList) {
            if (questionXml.equalsIgnoreCase(question.getText())) {
                return question;
            }
        }
        return null;
    }

    private void fillActorAnswerForUnkown(Actor actor) {
        List<Answer> answerList = new ArrayList<Answer>();
        for (Question question : questionList) {
            boolean questionFound = false;
            for (int i = 0; i < actor.getAnswerList().size() && questionFound == false; i++) {
                Answer answer = actor.getAnswerList().get(i);
                Question answerQuestion = answer.getQuestion();
                String answerQuestionName = answerQuestion.getName();
                if (question.getName().equalsIgnoreCase(answerQuestionName)) {
                    questionFound = true;
                }
            }

            if (!questionFound) {
                Answer answer = new Answer();
                answer.setQuestion(question);
                answer.setNumber(0);
                answerList.add(answer);
            }
        }
        actor.getAnswerList().addAll(answerList);
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    public void addNewActor(Actor actor, List<Question> newQuestion) {
        initialize();

        questionList.addAll(newQuestion);
        StringBuilder sb = new StringBuilder();
        sb.append("<QuestionListe>");
        for (Question question : questionList) {
            sb.append(question.toXmlString());
        }
        sb.append("</QuestionListe>");

        SharedPreferences sharedPref = context.getSharedPreferences("DataBases", 0);

        //now get Editor
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putString("question_db", sb.toString());

        List<Actor> actorList = null;
        try {
            actorList = getAllActor();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (actorList != null) {
            actorList.add(actor);
            sb = new StringBuilder();
            sb.append("<PersonneListe>");
            for (Actor actorXml : actorList) {
                sb.append(actorXml.toXmlString());
            }
            sb.append("</PersonneListe>");


            editor.putString("actor_db", sb.toString());
        }


        editor.commit();

    }

    private String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }

    private void insertInSharedPreferences() {
        // Create object of SharedPreferences.
        SharedPreferences sharedPref = context.getSharedPreferences("DataBases", 0);

        //now get Editor
        SharedPreferences.Editor editor= sharedPref.edit();
        InputStream inputStreamActor = context.getResources().openRawResource(R.raw.actor_db);
        InputStream inputStreamQuestion = context.getResources().openRawResource(R.raw.question_db);
        //put your value
        editor.putString("actor_db", readTextFile(inputStreamActor));
        editor.putString("question_db", readTextFile(inputStreamQuestion));
        editor.putBoolean("isSaved", true);
        //commits your edits
        editor.commit();
    }

    private Reader getSreamReaderForActor() {
        Reader stream = null;
        SharedPreferences sharedPref = context.getSharedPreferences("DataBases", 0);
        String data = sharedPref.getString("actor_db", "");
        if (!data.equals("")) {
            stream = new StringReader (data);
        }
        return stream;
    }

    private Reader getSreamReaderForQuestion() {
        Reader stream = null;
        SharedPreferences sharedPref = context.getSharedPreferences("DataBases", 0);
        String data = sharedPref.getString("question_db", "");
        if (!data.equals("")) {
            stream = new StringReader (data);
        }
        return stream;
    }

    private void saveDBIfNeeded() {
        SharedPreferences sharedPref = context.getSharedPreferences("DataBases", 0);
        boolean isSaved = sharedPref.getBoolean("isSaved", false);
        if (!isSaved) {
            insertInSharedPreferences();
        }
    }
}

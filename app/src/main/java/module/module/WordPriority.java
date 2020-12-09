package module;

import com.example.rems.DataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordPriority {

    private static Map<String, Integer> priorityWords;
    private static Map<String,String> timeWords;
    static private DataBaseHelper db = DataBaseHelper.getInstance(null);//TODO:make sure that the main call the method with the Context

    public WordPriority() {
        priorityWords = new HashMap<>();
        priorityWords = db.queryForPriorityWords();
    }

    public static Map<String, Integer> getPriorityWords() {
        return priorityWords;
    }

    public static void setTimeWords(){//this is called when opening the app to hard code the map of timeWords
        timeWords=new HashMap<>();

        //day of the week,month,month numeric,day of the month.
        //for day of the month and month numeric it must come together as like 1/12 ot 26.4 option for year later,will be checked if identify 6/7/2021 or 7/21
        //!!!working by WORLD STANDARD DD/MM/YYYY not MM/DD/YYYY
        String[] days={"sunday","monday","tuesday","wednesday","thursday","friday","saturday",
                "january","february","march","april","may","june","july","august","september","october","november","december",
                "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};

        //if i see number then : it means next number is minuets,no need to add each number up to 59 to the list
        //if i see twenty/thirty ill check for first second....ninth
        String[] hours={"today","tomorrow","AM","PM","st","nd","rd","th",
                "first","second","third","fourth","fifth","sixth","seventh","eighth","ninth","tenth","eleventh","twelfth","thirteenth","fourteenth","fifteenth","sixteenth","seventeenth","eighteenth","nineteenth","twentieth",
                "twenty","thirtieth","thirty",
                ":","00","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23",
                "01","02","03","04","05","06","07","08","09",
                "breakfast","brunch","lunch","dinner","dawn","twilight","sunrise","morning","daytime","evening","sunset","dusk","night","noon","afternoon","midnight"};

        //if identified it will be the triggered for the system to search for the other thime words of hours/days
        String[] conjunction={"at","on","for","by","to","now","till","until","before","after","then","by the time","as long as","as soon as","by the time"};

        for (int i=0; i < days.length + hours.length + conjunction.length; i++){ //adding the timeWord to the map
            if(i<days.length)
                timeWords.put(days[i],"days");
            else if(i>=days.length && i<days.length+hours.length)
                timeWords.put(hours[i-days.length],"hours");
            else
                timeWords.put(conjunction[i-days.length-hours.length],"conjunction");
        }
    }

    public static void setPriorityWords() {
        //TODO:check if the word is split (for example:"drink coffee","run,shower","drive/walk" etc) if so forbid it
        WordPriority.priorityWords = new HashMap<>();
    }
    public static boolean addWord(String word,Integer score){
        if((word==null || word=="") || (score<0 || score>10) || priorityWords.containsKey(word))
            return false;
        priorityWords.put(word,score);
        if (priorityWords.size()>0)
            return true;
        else
            return false;
    }

    public static boolean editWord(String word,Integer newScore){
        if (priorityWords.put(word,newScore)!=null)
            return true;
        else
            return false;
    }
    public static boolean editWord(String word, String newWord){
        if (priorityWords.put(newWord, priorityWords.remove(word))!=null)
            return true;
        else
            return false;
    }

    public static boolean editWord(String oldword, String newWord, Integer oldScore, Integer newScore){ //TODO:add to book and see wahat to do with the other two
        if(db.updateWord(oldword,newWord,newScore)){
            priorityWords.put(newWord, priorityWords.remove(oldword));
            priorityWords.put(newWord,newScore);
            return true;
        }
        else {
            //db.updateWord(oldword, newWord, oldScore);
            //TODO:add toast edit fail or something its 3AM in dead can't think about it RN
            return false;
        }

    }

    public static boolean removeWord(String word){
        if(db.deletePrioritiyWord(word))
            return priorityWords.remove(word)!=null ? true:false;
        else
            return false;
    }
    public static Integer getWordpriority(String word){
        return priorityWords.get(word);
    }
    public static boolean findWord(String word){
        return priorityWords.containsKey(word);
    }


    //TODO:implement timeWords and the method getTimeWords()

}

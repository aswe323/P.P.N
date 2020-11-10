package module;

import com.example.rems.DataBaseHelper;

import java.util.HashMap;
import java.util.Map;

public class WordPriority {

    private static Map<String, Integer> priorityWords;
    static private DataBaseHelper db = DataBaseHelper.getInstance(null);//TODO:make sure that the main call the method with the Context

    public WordPriority() {
        priorityWords = new HashMap<>();
        priorityWords = db.queryForPriorityWords();
    }

    public static Map<String, Integer> getPriorityWords() {
        return priorityWords;
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
    public static boolean removeWord(String word){
        return priorityWords.remove(word)!=null ? true:false;
    }
    public static Integer getWordpriority(String word){
        return priorityWords.get(word);
    }
    public static boolean findWord(String word){
        return priorityWords.containsKey(word);
    }

    //TODO:implement timeWords and the method getTimeWords()

}

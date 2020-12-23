package module;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.rems.DataBaseHelper;

import java.util.HashMap;
import java.util.Map;

public class WordPriority {

    private static Map<String, Integer> priorityWords;
    private static Map<String,String> bucketWords;
    static private DataBaseHelper db = DataBaseHelper.getInstance(null);//TODO:make sure that the main call the method with the Context

    public WordPriority() {
        priorityWords = new HashMap<>();
        bucketWords = new HashMap<>();
        bucketWords = getBucketWords();
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

    public static boolean removeWord(String word) {
        if (db.deletePrioritiyWord(word))
            return priorityWords.remove(word) != null ? true : false;
        else
            return false;
    }

    public static Integer getWordpriority(String word) {
        return priorityWords.get(word);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Integer getPriorityFromSentence(String sentence) {
        Integer returned = 0;
        Map<String, Integer> filtered = new HashMap<>(priorityWords);

        filtered.entrySet().removeIf(e->!sentence.contains(e.getKey()));

        for (Integer priority : filtered.values()) returned += priority;
        return returned;
    }

    public static boolean findWord(String word) {
        return priorityWords.containsKey(word);
    }

    //region bucket words region

    /**
     * calls {@link DataBaseHelper#insertBucketWord(String, String) insertBucketWord} from {@link DataBaseHelper}
     * @param word the word itself
     * @param range the time range for this bucket word
     * @return true if inserted successfully else return false
     */
    public static boolean addBucketWord(String word,String range){ //TODO:add to the book
        if((word==null || word=="") || priorityWords.containsKey(word))
            return false;

        bucketWords.put(word,range);
        return  db.insertBucketWord(word,range);
    }

    /**
     * calls {@link DataBaseHelper#queryForBucketWords() queryForBucketWords} from {@link DataBaseHelper}
     * @return map of the bucket words in the database
     */
    public static Map<String,String> getBucketWords(){ //TODO:add to the book
        return db.queryForBucketWords();
    }

    /**
     * calls {@link DataBaseHelper#updateBucketWord(String, String, String) updateBucketWord}  updateBucketWord} from {@link DataBaseHelper}
     * @param oldWord the word we want to update
     * @param newWord the new word we update to
     * @param range the range
     * @return true if updated successfully else return false
     */
    public static boolean updateBucketWordWord(String oldWord,String newWord,String range){ //TODO:add to the book
        return db.updateBucketWord(oldWord,newWord,range);
    }

    /**
     * calls {@link DataBaseHelper#deleteBucketWord(String) deleteBucketWord} updateBucketWord} from {@link DataBaseHelper}
     * @param word the word we want to delete
     * @return true if deleted successfully else return false
     */
    public static boolean deleteBucketWord(String word){ //TODO:add to the book
        return db.deleteBucketWord(word);
    }
    //endregion


}

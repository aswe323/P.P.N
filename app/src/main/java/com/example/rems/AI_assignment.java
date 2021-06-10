package com.example.rems;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import module.ActivityTask;
import module.ActivityTasksUsed;
import module.WordPriority;

public class AI_assignment {

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDateTime AITimeSetter(String content, int priority){//set time by natty library if natty library filed to set time will notify used to set it by hand\change content TODO:add to the book
        String[] bucketWord_N_bucketRange;
        String activityDate="";
        ArrayList<ActivityTask> remindersOfTheDay=null;
        //using natty library to see is word can generate a time and date
        List<DateGroup> groups;
        Parser parser = new Parser();
        groups = parser.parse(content);
        if (groups.size()>0){//check if i got a date

            List dates = groups.get(0).getDates();//get the date that natty created for us
            LocalDateTime ldt= ((Date) dates.get(0)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); //convert it to LocalDateTIme
        }
        else{
            bucketWord_N_bucketRange=bucketRangeChecker(content);
            if(bucketWord_N_bucketRange[0].equals("NaN"))
                return null; //TODO:notify user
            else {
                //remindersOfTheDay
            }
            //TODO:search reminders in range
        }
        return null; //****if didn't got date and time by natty library then return null*****
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<ActivityTask> getReminders(String[] bucketWord_N_bucketRange, String content,LocalDateTime nattyTime){
        int totalMinutes;
        String[] range=bucketWord_N_bucketRange[1].split("-");//take the range form-to and split it into array of {form,to}
        String[] HoursMinutes; //used to brake the time of the range to an array of {hours,minutes}
        LocalDateTime fromTime,toTime;

        if(nattyTime!=null){//check if the natty time is in range, if not return null
            HoursMinutes=range[0].split(":");//get the time of start of the range
            fromTime=LocalDateTime.of(nattyTime.getYear(),nattyTime.getMonth(),nattyTime.getDayOfMonth(),Integer.parseInt(HoursMinutes[0]),Integer.parseInt(HoursMinutes[1]));
            HoursMinutes=range[1].split(":");//get the time of end of the range
            //because the time range can "leak" to the next day we will add the time total minutes difference between the start and the end of the range to our LocalDateTime, hence the LocalDateTime will move to the next day
            totalMinutes = Math.abs((fromTime.getHour() - Integer.parseInt(HoursMinutes[0])))*60 + Math.abs(fromTime.getMinute()-Integer.parseInt(HoursMinutes[1]));
            toTime = fromTime.plusMinutes(totalMinutes);
            if(nattyTime.compareTo(fromTime)<0 || nattyTime.compareTo(toTime)>0)//if the time if smaller then the start of the range or bigger then the end of the range return null
                return null;
        }

        if(checkWordBefore(content,"before",bucketWord_N_bucketRange[0])){//if the reminder ask *before* the range then take the reminders from start of the day to start of range
            HoursMinutes=range[0].split(":");//get the time of start of the range
            //check if the natty installed a time,if not then use the date of today and the time range,if yes so use the date of natty and use the time range
            toTime = nattyTime==null ? LocalDate.now().atTime(Integer.parseInt(HoursMinutes[0]),Integer.parseInt(HoursMinutes[1])):LocalDateTime.of(nattyTime.getYear(),nattyTime.getMonth(),nattyTime.getDayOfMonth(),Integer.parseInt(HoursMinutes[0]),Integer.parseInt(HoursMinutes[1]));
            //check if the natty installed a time,if not then use the date of today and the time now,if yes so use the date of natty and use the time now
            fromTime = nattyTime==null ? LocalDateTime.now():LocalDateTime.of(nattyTime.getYear(),nattyTime.getMonth(),nattyTime.getDayOfMonth(), LocalTime.now().getHour(),LocalTime.now().getMinute());
            if(toTime.isBefore(fromTime))//if time from is after the to range then add 1 day
                toTime=toTime.plusDays(1);
            return ActivityTasksUsed.getActivitiesByRange(fromTime,toTime);
        }
        else if(checkWordBefore(content,"after",bucketWord_N_bucketRange[0])){//if the reminder ask *after* the range then take the reminders from start of the end of the range to end of the day
            HoursMinutes=range[0].split(":");//get the time of start of the range
            //check if the natty installed a time,if not then use the date of today and the time range,if yes so use the date of natty and use the time range
            fromTime = nattyTime==null ? LocalDate.now().atTime(Integer.parseInt(HoursMinutes[0]),Integer.parseInt(HoursMinutes[1])):LocalDateTime.of(nattyTime.getYear(),nattyTime.getMonth(),nattyTime.getDayOfMonth(),Integer.parseInt(HoursMinutes[0]),Integer.parseInt(HoursMinutes[1]));
            HoursMinutes=range[1].split(":");//get the time of end of the range
            //because the time range can "leak" to the next day we will add the time total minutes difference between the start and the end of the range to our LocalDateTime, hence the LocalDateTime will move to the next day
            totalMinutes = Math.abs((fromTime.getHour() - Integer.parseInt(HoursMinutes[0])))*60 + Math.abs(fromTime.getMinute()-Integer.parseInt(HoursMinutes[1]));
            fromTime.plusMinutes(totalMinutes);
            toTime=LocalDateTime.of(fromTime.getYear(),fromTime.getMonth(),fromTime.getDayOfMonth(),23,59);//set the day of the start to the end of it's day
            //TODO: check if the range if over for today even with natty time and if yes then move to next day
            return ActivityTasksUsed.getActivitiesByRange(fromTime,toTime);
        }
        else{
            HoursMinutes=range[0].split(":");//get the time of start of the range
            if(nattyTime==null)
                fromTime = LocalDateTime.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth(),Integer.parseInt(HoursMinutes[0]),Integer.parseInt(HoursMinutes[1]));
            else
                //לבדוק אם הנתי בטווח בכלל
        }
        //int minutesToAdd=
        //check befores/after if there is then choose (now-from if before | to-endOfDay if after)
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean AIPriority(int priorityCurrentPlaceHolder,int priorityNewReminder){//if the current reminder has bigger priority return false, else if the new one has bigger priority return true//TODO:add to the book
        return priorityCurrentPlaceHolder>priorityNewReminder ? false:true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean ReReSchedule(ActivityTask activityTask){

        //TODO:make an dialog box asking id usr want to accept new times of rescheduale reminder and the installetion of the new one instad
        int minutesToSubtract=30;
        LocalDateTime dateAndTime=activityTask.getTimeOfActivity();
        String activityDate=""+dateAndTime.getYear()+"-"+dateAndTime.getMonth()+"-"+dateAndTime.getDayOfMonth();//convert the date to a string to get all the reminders at this day
        while (ActivityTasksUsed.findExactActivityTask(0,0,dateAndTime.minusMinutes(minutesToSubtract),null,null,null)!=null) {
            minutesToSubtract-=5;
        }
        dateAndTime=dateAndTime.minusMinutes(minutesToSubtract);
        activityTask.setTimeOfActivity(dateAndTime);

        //need to call method that create dialog box asking for confirmation
        //if confirmed return true, else return false

        return true;
    }

    /**
     * search if one of the bucket words exsist in the content od the reminder,if so returng the range if not return NaN
     *
     * @param content the reminder content
     * @return string of range (if bucket word was found) or NaN
     */
    private static String[] bucketRangeChecker(String content){
        Map<String,String> bucketWords= WordPriority.getBucketWords();
        for(Map.Entry<String,String> bucketWord:bucketWords.entrySet()){
            if(checkIfWordExist(content,bucketWord.getKey()))
                return new String[]{bucketWord.getKey(),bucketWord.getValue()};
        }
        return new String[]{"NaN","NaN"};
    }

    public static String getPreviousWord(String str, String word){//used to get the previous word before AN\PM to get the time TODO:not sure if needed if yes add to the book
        Pattern p = Pattern.compile(".*?\\b(?i)([^ ]+)\\W+"+word);//take all the words before the @word until you see a space '  '
        Matcher m = p.matcher(str);
        return  m.find()? m.group(1):"NAN";
    }
    public static boolean checkIfWordExist(String str, String word){//used check if word exist in the string TODO:add to the book
        String regex=".*?\\b(?i)("+word+")\\b.*";
        return  str.matches(regex);
    }

    /**
     * get the reminder content,the word to check (before/after) and the bucket word.<br>
     * this method check id the word before the bucket word is the word we searched for (before/after)
     * @param content the reminder content
     * @param WordToCheck the word we search for
     * @param bucketWord the word we want to check what before it
     * @return true if found(before/after) or false if didn't
     */
    private static boolean checkWordBefore(String content,String WordToCheck,String bucketWord){
        String regex=".*?\\b(?i)([^ ]+)\\W+"+bucketWord;//search for the word before bucketWord int the content
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if(m.find() && checkIfWordExist(m.group(1),WordToCheck))
            return true;
        return false;

    }
}

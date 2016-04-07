import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by pieter on 2/22/16.
 */
public class Word {

    //The key
    private String word;

    //The values it points to, and the number of times it followed this word.
    private HashMap<String, Integer> map;

    //How many different times it occurred.
    private int occurences;

    public Word(String word){
        this.word = word;
        map = new HashMap<>(5);
    }

    public void addOccurence(String word){
        //Adds an occurrence
        if(map.containsKey(word)){
            //There were no previous occurrences, so a new one is added, with it appearing one time.
            map.put(word, map.get(word) + 1);
        } else {
            //There were previous occurrences, so we update this following value's integer
            map.put(word, 1);
        }
        //Increase the total number of times we saw this word.
        occurences++;
    }

    public String getNext(){
        //Get a random integer that is based on the occurrences
        int spot = (int) (Math.random() * occurences);

        //get the keys to iterate through
        Set<String> set = map.keySet();
        int count = 0;
        String next = null;

        //Simply add up the occurrences of the following words, until we've got count to be < spot.
        for(String s : set){
            count += map.get(s);
            if(spot < count) {
                next = s;
                break;
            }
        }
        return next;
    }
}

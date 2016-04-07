import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {

    static HashMap<String, Word> map;

    public static void main(String[] args) throws IOException {
        //Initialize Hashmap
        map = new HashMap<>();
        //Read in the data from a file. In this case a bunch of lines of text.
        BufferedReader in = new BufferedReader(new FileReader("Sanders Text.txt"));
        String line;
        List<String> lines = new ArrayList<String>();
        int j = 0;
        //removing extra spaces, etc.
        while ((line = in.readLine()) != null) {
            j++;
            line = line.replaceAll("\t", "");
            line = line.replaceAll("\n", "");
            lines.add(line);
            if(j % 1000 == 0 ) System.out.println(j);
        }
        //Putting all the text into a single string.
        String text = "";
        for (String s : lines) {
            text += s + " ";
        }
        //Split the text to get each word out.
        String[] split = text.split(" ");
        for (int i = 0; i < split.length - 2; i++) {
            //Get the first word. It is used as a key.
            String curr = split[i];
            //Get the next word. It is used as a value.
            String next = split[i + 1];
            //Add an occurence of just a single word pointing to a single other word.
            add(curr);
            addOccurence(curr, next);

            //Now add the word that was our value to the key.
            curr += next;
            //Get the next value.
            next = split[i + 2];
            //Add an occurence of the updated key that has two word in it, and have it point to a single other word.
            add(curr);
            addOccurence(curr, next);
        }
        mark();
    }

    static void mark() {
        Scanner sc = new Scanner(System.in);
        //Gets word to start generation on.
        System.out.println("Put seed word here:");
        while (true) {
            String input = sc.nextLine();
            //If it starts with put, I am adding data to the chain.
            if (input.startsWith("put ")) {
                input = input.substring(4, input.length());
                input = input.replace("\n", "").replace("\r", "");
                String[] split = input.split(" ");
                for (int i = 0; i < split.length - 1; i++) {
                    String curr = split[i];
                    String next = split[i + 1];
                    add(curr);
                    System.out.print(curr + " ");
                    addOccurence(curr, next);
                }
                System.out.println();
            } else {
                //Integer to stop generation after a thousand words.
                int i = 1;

                //Structure to hold all the words.
                ArrayList<String> words = new ArrayList<>();

                //Add the current word.
                words.add(input);

                //Print the words as I go.
                System.out.print(input + " ");

                //Get the next word using input (input is .get(0)) as a key.
                input = map.get(words.get(0)).getNext();
                //Add that returned word to the list of words we generated.
                words.add(input);
                System.out.print(input + " ");
                //Simply moving the pointer forward.
                input = map.get(words.get(1)).getNext();


                while (input != null && i < 1000) {
                    //Moves the pointer to the next word.
                    i++;

                    //Add our current generated word to the list of words.
                    words.add(input);
                    System.out.print(input + " ");

                    //Tries to update input so that the key is two words concatenated.
                    try {
                        //Gets the previously used word, and the current used word, concatenates both and uses that as a key.
                        //With the concatenated key, it then gets the next word.
                        input = map.get(words.get(i-1)+ words.get(i)).getNext();
                    }
                    //Otherwise the two word key had no values to follow.
                    catch (NullPointerException e) {
                        try{
                            //Tries to get a key of just one word.
                           input = map.get(words.get(i)).getNext();
                        } catch (NullPointerException p){
                            //Otherwise we are done, hit a dead end.
                            input = null;
                            System.out.println(" END");
                        }
                    }
                }
                System.out.println();
            }
        }
    }

    static void add(String w) {
        if (!map.containsKey(w)) {
            map.put(w, new Word(w));
        }
    }

    static void addOccurence(String word, String occurence) {
        map.get(word).addOccurence(occurence);
    }
}

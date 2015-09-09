import java.io.File;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
       
        //TODO
        int counter = 0;
        String line = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Map<String, Integer> map = new HashMap<String, Integer>();

        Integer[] sorted = getIndexes();
        Arrays.sort(sorted);

        try {
            fileReader = new FileReader(this.inputFileName);
            bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                if (Arrays.asList(sorted).contains(counter)) {
                    Integer times = Collections.frequency(Arrays.asList(sorted), counter);
                    // System.out.println("Line: " + counter + " - " + line);
                    StringTokenizer st = new StringTokenizer(line, this.delimiters);
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken().trim().toLowerCase();
                        if (!Arrays.asList(stopWordsArray).contains(token)) {
                            Integer freq = map.get(token);
                            map.put(token, (freq == null) ? 1 * times: freq + 1 * times);
                        }
                    }
                }
                counter++;
            }

            // System.out.println( "disambiguation: " + map.get("disambiguation") );
            // System.out.println( "county: " + map.get("county"));
            // System.out.println(new PrettyPrintingMap<String, Integer>(map));
            Map<String, Integer> sorted_map = sortByValues(sortByKeys(map));
            Set<String> keys = sorted_map.keySet();
            String[] array = keys.toArray(new String[keys.size()]);
            ret = Arrays.copyOf(array, 20);
            // for (String item: ret){
            //     System.out.println( item + " - " + sorted_map.get(item));
            // }
            // System.out.println("results: " + ret);

        } catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" +
                this.inputFileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '"
                + this.inputFileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        finally
        {
            if(fileReader != null){
               // Always close files.
               bufferedReader.close();
            }
        }
        // End of TODO

        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }

    /*
     * Paramterized method to sort Map e.g. HashMap or Hashtable in Java
     * throw NullPointerException if Map contains null key
     */
    @SuppressWarnings("unchecked")
    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByKeys(Map<K,V> map){
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys);

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
        for(K key: keys){
            sortedMap.put(key, map.get(key));
        }

        return sortedMap;
    }

    /*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable
     * throw NullPointerException if Map contains null values
     * It also sort values even if they are duplicates
     */
    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @Override
            @SuppressWarnings("unchecked")
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();

        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}

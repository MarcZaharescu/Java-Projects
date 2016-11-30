import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// you can also use imports, for example:
// import java.util.*;

// you can write to stdout for debugging purposes, e.g.
// System.out.println("this is a debug message");

class Solution {

    static final Map<String, String> CODE;

    static {
        Map<String, String> temp = new HashMap<>();
        
        temp.put("",    " ");
        temp.put(".-",   "A");
        temp.put("-...", "B");
        temp.put("-.-.", "C");
        temp.put("-..",  "D");
        temp.put(".",    "E");
        temp.put("..-.", "F");
        temp.put("--.",  "G");
        temp.put("....", "H");
        temp.put("..",   "I");
        temp.put(".---", "J");
        temp.put("-.-",  "K");
        temp.put(".-..", "L");
        temp.put("--",   "M");
        temp.put("-.",   "N");
        temp.put("---",  "O");
        temp.put(".--.", "P");
        temp.put("--.-", "Q");
        temp.put(".-.",  "R");
        temp.put("...",  "S");
        temp.put("-",    "T");
        temp.put("..-",  "U");
        temp.put("...-", "V");
        temp.put(".--",  "W");
        temp.put("-..-", "X");
        temp.put("-.--", "Y");
        temp.put("--..", "Z");

        temp.put(".----","1");
        temp.put("..---","2");
        temp.put("...--","3");
        temp.put("....-","4");
        temp.put(".....","5");
        temp.put("-....","6");
        temp.put("--...","7");
        temp.put("---..","8");
        temp.put("----.","9");
        temp.put("-----","0");
        
      

        CODE = Collections.unmodifiableMap(temp);
    }
    public String solution(String S) {

    	// ok is a used to check if the input is morse valid
    	int ok=1;
    	
    	// the returned converted string
        String sentence="";
        
       // remove the white spaces at the beginning of S
       S=S.trim();
        
        
        // we split the input string into words
        String[] words = S.split("       ");
         
         
        // loop through each words
        for(int i =0;i<words.length;i++)
        {
        	
        	 // we split each word into letters
        	  String[] letters = words[i].split("   ");
        	 
        	  // we loop through each letter
        	  for(int j=0;j<letters.length;j++){
        		 
        		  // we declare a new variable called letter that gets the letter value from the morse hashmap
        	     String letter = null;
        	     letter = CODE.get(letters[j]);
        	 
        	     // if it cannot find a value it returns null, hence the ok becomes 0
        	     if(letter==null){
        	      ok=0;
        		}
        	     
        	     // otherwise we add the converted letter to the sentence
        	     sentence+=letter;
        	  }
        	 
        	// we add a space to the sentence only if it is not the last world
        	if(i<words.length-1)
        	sentence+=" ";
        	
			
        }
        	
        //if a wrong character existed in the initial input then it returns INVALID CODE
        if(ok==0)
        sentence="INVALID CODE";
        
        // returns the sentence
		return sentence;
        
    }
    
    public static void main(String[] args)
    {
    	
    	Solution s = new Solution();
     
    	System.out.println(s.solution(" "));}
}
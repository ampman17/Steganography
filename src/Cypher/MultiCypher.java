package Cypher;

public class MultiCypher extends Cypher.ConvertChar {

    private String string;
    private int[] shifts;
    private int numOfCyphers;

    public void setter(String s, int[] i) {
        string = s;
        shifts = i;
        numOfCyphers = shifts.length;
    }

    //Encrypts the given string and returns the output
    public String EncryptString(){
        char[] ch = new char[string.length()];

        //Converts each char in the string one at a time
        for(int i = 0; i<string.length(); i++){
            ch[i] = ConvertChar(string.charAt(i), shifts[i%numOfCyphers]);
        }
        return String.copyValueOf(ch);
    }

    //Decrypts the given string and returns the output
    public String DecryptString(){
        char[] ch = new char[string.length()];

        //Converts each char in the string one at a time
        for(int i = 0; i<string.length(); i++){
            ch[i] = ConvertChar(string.charAt(i), shifts[i%numOfCyphers]*-1);
        }

        return String.copyValueOf(ch);
    }



}

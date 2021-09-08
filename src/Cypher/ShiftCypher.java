package Cypher;

public class ShiftCypher extends ConvertChar {

    private String string;
    private int shiftSize;
    public void setter(String c, int x){ string = c; shiftSize = x; }

    //Encrypts the given string and returns the output
    public String EncryptString(){
        char[] ch = new char[string.length()];

        //Converts each char in the string one at a time
        for(int i = 0; i<string.length(); i++){
            ch[i] = ConvertChar(string.charAt(i), shiftSize);
        }
        return String.copyValueOf(ch);
    }

    //Decrypts the given string and returns the output
    public String DecryptString(){
        char[] ch = new char[string.length()];
        shiftSize *=-1; //since decrypting the string is simply adding the negative value we multiply by -1

        //Converts each char in the string one at a time
        for(int i = 0; i<string.length(); i++){
            ch[i] = ConvertChar(string.charAt(i), shiftSize);
        }
        shiftSize *=-1;
        return String.copyValueOf(ch);

    }

}

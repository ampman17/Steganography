package Cypher;

public abstract class ConvertChar {

    //Converts any character by its shiftValue
    //If the char is not in the alphabet it returns the passed character
    public char ConvertChar(char c, int shiftSize){
        int value = c;
        if(value>=65 && value<=90) return UpperCase(c, shiftSize);
        else if(value>=97 && value<=122) return LowerCase(c, shiftSize);
        else return c;
    }

    //Here I add 65 and later remove 65 since ASCII Uppercase Chars begin at 65
    //This allows me to use modular 26 without added complications and then simply add the 65 at the end
    private char UpperCase(char c, int shiftSize){
        int value = (((int)c-65+shiftSize+26)%26)+65;
        return (char)value;
    }

    //Here I add 97 and later remove 97 since ASCII Lowercase Chars begin at 97
    //This allows me to use modular 26 without added complications and then simply add the 97 at the end
    private char LowerCase(char c, int shiftSize){
        int value = (((int)c-97+shiftSize+26)%26)+97;
        return (char)value;
    }

}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LexicalAnalyzer {
    static HashMap<String, Integer> lexemStore = new HashMap<>();
    char[] chars = null;
    static int position = 0;

    public LexicalAnalyzer(String datafile) {
        try {
            //open the file and read the file line by line
            BufferedReader bufferedReader = new BufferedReader(new FileReader(datafile));
            String line;
            String contents = "";
            while ((line = bufferedReader.readLine()) != null) {
                contents += line.trim().replace("\n", " ").replace("\t", "") + " ";
            }
            //split the file contents into a char array
            chars = contents.toCharArray();
            //create a map with the keywords
            populateStore();
           /* for (char c: chars){
                System.out.print(c);
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void populateStore() {
        //o for integer literals 1 for identifiers -1 for error
        lexemStore.put("procedure", Keywords.PROCEDURE);
        lexemStore.put("begin", Keywords.BEGIN);
        lexemStore.put(":=", Keywords.ASSIGN);
        lexemStore.put("+", Keywords.PLUS);
        lexemStore.put("-", Keywords.MINUS);
        lexemStore.put("*", Keywords.MULTIPLY);
        lexemStore.put("/", Keywords.DIVIDE);
        lexemStore.put("=", Keywords.EQUAL);
        lexemStore.put("!=", Keywords.NOT_EQUAL);
        lexemStore.put("(", Keywords.L_PARENTH);
        lexemStore.put(")", Keywords.R_PARENTH);
        lexemStore.put("if", Keywords.IF);
        lexemStore.put("else", Keywords.ELSE);
        lexemStore.put("endif", Keywords.ENDIF);
        lexemStore.put("then", Keywords.THEN);
        lexemStore.put("end", Keywords.END);
        lexemStore.put(";", Keywords.SEMI_COLON);
        lexemStore.put(" ", Keywords.SPACE);

    }

    public int lex() {
        //loop infinitely
        while (true) {
            //return end of file if the end of file is reached
            if (position >= chars.length) {
                return Keywords.EOF;
            }
            //check if the character at the current position is a letter
            if (Character.isLetter(chars[position])) {
                //build a string if the as long as consecutive characters are letters or numbers
                StringBuffer stringBuffer = new StringBuffer();
                while (Character.isLetterOrDigit(chars[position])) {
                    stringBuffer.append(chars[position]);
                    position += 1;
                }
                //check for reserved keywords from the built strings.
                if (lexemStore.containsKey(stringBuffer.toString())) {
                    if(chars[position] == '('){

                    }
                    else{
                        position++;
                    }
                    //return a reserved keyword token
                    return lexemStore.get(stringBuffer.toString());
                } else {
                    //return an identifier token
                    position++;
                    return Keywords.IDENTIFIER;
                }
                //check if a character is a digit
            } else if (Character.isDigit(chars[position])) {
                //System.out.println(position);
                while (Character.isDigit(chars[position])) {
                    position++;
                    //System.out.println(position);
                }
                //position++;
                //System.out.println("now returning 0");
                //return an integer literal token
                return Keywords.INT_LITERAL;
            } else {
                // handle other characters such as assignment operators, arithmetic operators.
                if (chars[position] == ':') {
                    //check the token next to it.
                    if (nextTo('=')) {
                        position++;
                        return lexemStore.get(":=");
                    }
                } else if (chars[position] == '=') {
                    position++;
                    return lexemStore.get("=");
                } else if (chars[position] == '!') {
                    if (nextTo('=')) {
                        position++;
                        return lexemStore.get("!=");
                    }
                } else if (chars[position] == '+') {
                    position++;
                    return lexemStore.get("+");
                } else if (chars[position] == '-') {
                    position++;
                    return lexemStore.get("-");
                } else if (chars[position] == '*') {
                    position++;
                    return lexemStore.get("*");
                } else if (chars[position] == '/') {
                    position++;
                    return lexemStore.get("/");
                } else if (chars[position] == '(') {
                    position++;
                    return lexemStore.get("(");
                } else if (chars[position] == ')') {
                    position++;
                    return lexemStore.get(")");
                } else if (chars[position] == ' ') {
                    position++;
                    //return nothing if the character is an empty space
                } else if (chars[position] == ';') {
                    position++;
                    return lexemStore.get(";");
                } else {
                    position++;
                    return Keywords.ERROR;
                }
            }
        }
    }

    //This method peeks the adjacent character to the right of the current character
    private boolean nextTo(char c) {
        if (chars[position + 1] == c) {
            position++;
            return true;
        }

        return false;
    }
}


public class Parser {
    private LexicalAnalyzer lexicalAnalyzer;
    private int currentToken;
    private int previousPosition;

    //constructor
    public Parser(String datafile) {
        //initialize the lexer
        lexicalAnalyzer = new LexicalAnalyzer(datafile);
    }

    //program grammar block
    public boolean program() {
        if (lexicalAnalyzer.lex() == Keywords.PROCEDURE) {
            if (lexicalAnalyzer.lex() == Keywords.IDENTIFIER) {
                if (lexicalAnalyzer.lex() == Keywords.BEGIN) {
                    if (stmtList()) {
                        return true;
                    }else{
                        return false;
                    }
                }
            }
        }
        return false;
    }

    //stmtlist grammer block
    private boolean stmtList() {
        if(stmt()){
            //System.out.println("Stmt found");
            while (true){
                //check for possible additional statements
                if(!stmtList()){
                    break;
                }
            }
            return currentToken==Keywords.EOF || currentToken==Keywords.ELSE || currentToken==Keywords.ENDIF || currentToken==Keywords.END;
        }
        return false;
    }

    //single stmt grammar block
    private boolean stmt() {
        //a stmt comprises of either and assign or if block
        if(assign()){
            return true;
        }else{
            //set the position to the previous position
            LexicalAnalyzer.position = previousPosition;
        }

        if(ifStatement()){
            return true;
        }else{
            LexicalAnalyzer.position = previousPosition;
            return false;
        }
    }

    //assign grammar block
    private boolean assign() {
        //store current position as previous
        previousPosition = LexicalAnalyzer.position;
        if(var()){//check for presence of a variable
            //System.out.println("Var found");
            currentToken = lexicalAnalyzer.lex();
            if(currentToken==Keywords.ASSIGN){//check for an assignment operator
                //System.out.println("assign key found");
                return expr();//validate the expression
            }else if(currentToken == Keywords.END){
                return lexicalAnalyzer.lex() == Keywords.SEMI_COLON;
            }
        }
        return false;
    }

    //if grammar block
    private boolean ifStatement() {
        //save current position
        previousPosition = LexicalAnalyzer.position;
        if (lexicalAnalyzer.lex() == Keywords.IF) {
            if (lexicalAnalyzer.lex() == Keywords.L_PARENTH) {
                if (bool()) {//check for presence of a boolean comparison
                    if (lexicalAnalyzer.lex() == Keywords.R_PARENTH) {
                        if (lexicalAnalyzer.lex() == Keywords.THEN) {
                            if (stmtList()) {//check for presence of a stmtlist
                                currentToken = lexicalAnalyzer.lex();
                                if (currentToken == Keywords.ELSE) {//in case there is an else block
                                    if (stmtList()) {//check for presence of a stmt
                                        return lexicalAnalyzer.lex() == Keywords.SEMI_COLON;
                                    }
                                } else if (currentToken == Keywords.ENDIF) {//incase the block jump to endif
                                    return lexicalAnalyzer.lex() == Keywords.SEMI_COLON;//ensure there is a semicolon
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    //expr grammar block
    private boolean expr() {
        //check for a valid term
        if (term()) {
            //System.out.println("Term found");
            while (true) {
                currentToken = lexicalAnalyzer.lex();
                //check for presence of an operator
                if (currentToken == Keywords.PLUS || currentToken == Keywords.MINUS || currentToken == Keywords.MULTIPLY || currentToken == Keywords.DIVIDE) {
                    return expr(); //validate expression
                } else {
                    if (currentToken == Keywords.SEMI_COLON) {//ensure it ends with a semicolon
                        return true;
                    }else {
                        break;
                    }
                }
            }
        }
        return false;
    }

    //term grammar block
    private boolean term() {
        if(var()){//check for a valid var
            return true;
        }else{
            //set position to previous just in case it wasn't valid
            LexicalAnalyzer.position = previousPosition;
        }

        if(intV()){ //check for a valid integer literal
            return true;
        }else {
            //move the position to the previous position
            LexicalAnalyzer.position = previousPosition;
            return false;
        }
    }

    //bool grammar block
    private boolean bool() {
        if (var()) {//check for a valid var
            currentToken = lexicalAnalyzer.lex();
            //ensure there is a comparison operator
            if (currentToken == Keywords.EQUAL || currentToken == Keywords.NOT_EQUAL) {
                if(intV()){
                    return true;
                }
                LexicalAnalyzer.position = LexicalAnalyzer.position - 1;
                if(currentToken == Keywords.IDENTIFIER){
                    return true;
                }
            }
        }
        return false;
    }

    //int grammar block
    private boolean intV() {
        previousPosition = LexicalAnalyzer.position;
        currentToken = lexicalAnalyzer.lex();
        return currentToken == Keywords.INT_LITERAL;
    }

    //var grammar block
    private boolean var() {
        previousPosition = LexicalAnalyzer.position;
        currentToken = lexicalAnalyzer.lex();
        return currentToken ==  Keywords.IDENTIFIER;
    }

}

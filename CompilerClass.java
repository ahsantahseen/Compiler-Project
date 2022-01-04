
/*
Compiler Construction Project 

Author: Muhammad Ahsan 1912310 Section 5F BSCS
Language: Java 
Compiled with: JavaSE-13/ JDK-11
Development Resources Used: Microsoft Visual Studio Code with Redhat Java Extension 

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

class Token {
    private int attributeValue;
    private String TokenName;
    private String Type;
    private String Value;

    public Token(int attributeValue, String TokenName, String Type, String Value) {
        this.attributeValue = attributeValue;
        this.TokenName = TokenName;
        this.Type = Type;
        this.Value = Value;
    }

    @Override
    public String toString() {
        if (TokenName.length() < 5) {
            return " " + attributeValue + " \t\t | " + TokenName + "\t\t|" + Type + " \t| " + Value + " \t    ";
        } else {
            return " " + attributeValue + " \t\t | " + TokenName + "\t|" + Type + " \t| " + Value + "\t    ";
        }
    }
}

public class CompilerClass {

    static ArrayList<Token> SymbolTable = new ArrayList<Token>();
    static String ActionTable[][] = {
            { "s5", "error", "error", "s4", "error", "error" },
            { "error", "s6", "error", "error", "error", "accept" },
            { "error", "r2", "s7", "error", "r2", "r2" },
            { "error", "r4", "r4", "error", "r4", "r4" },
            { "s5", "error", "error", "s4", "error", "error" },
            { "error", "r6", "r6", "error", "r6", "r6" },
            { "s5", "error", "error", "s4", "error", "error" },
            { "s5", "error", "error", "s4", "error", "error" },
            { "error", "s6", "error", "error", "s11", "error" },
            { "error", "r1", "s7", "error", "r1", "r1" },
            { "error", "r3", "r3", "error", "r3", "r3" },
            { "error", "r5", "r5", "error", "r5", "r5" },
    };
    static String GotoTable[][] = {
            { "1", "2", "3" },
            { "error", "error", "error" },
            { "error", "error", "error" },
            { "error", "error", "error" },
            { "8", "2", "3" },
            { "error", "error", "error" },
            { "error", "9", "3" },
            { "error", "error", "10" },
            { "error", "error", "error" },
            { "error", "error", "error" },
            { "error", "error", "error" },
            { "error", "error", "error" },
    };
    static String Grammer[][] = {
            { "E", "E", "+", "T" },
            { "E", "T" },
            { "T", "T", "*", "F" },
            { "T", "F" },
            { "F", "(", "E", ")" },
            { "F", "id" },
    };
    static ArrayList<String> InputBuffer = new ArrayList<String>();
    static Stack<String> CurrentStack = new Stack<String>();
    static int InputBufferPointer = 0;

    // ===== Utility Methods=======//

    /*
     * Function that checks whether the lexeme is one of the keywords that we have
     * stored in the array, if it's a keyword return true else return false
     */
    static boolean iskeyword(String lexeme) {
        // Storing keywords in an array
        String keywords[] = { "int", "char", "string", "if", "else", "do", "while" };
        // Looping through the keywords
        for (int i = 0; i < keywords.length; i++) {
            // if our lexeme matches the keywords then return true
            if (lexeme.matches(keywords[i])) {
                return true;
            }
        }
        // if not return false
        return false;
    }

    // Method to insert Token into Symbol Table
    static void addToken(int attributeValue, String TokenName, String Type, String Value) {

        SymbolTable.add(new Token(attributeValue, TokenName, Type, Value));
    }

    // Index Calculator For Stack Peek
    static int IndexStackPeek(String topOfStack) {
        if (topOfStack.equals("id") || topOfStack.equals("E")) {
            return 0;
        } else if (topOfStack.equals("+") || topOfStack.equals("T")) {
            return 1;
        } else if (topOfStack.equals("*") || topOfStack.equals("F")) {
            return 2;
        } else if (topOfStack.equals("(")) {
            return 3;
        } else if (topOfStack.equals(")")) {
            return 4;
        } else if (topOfStack.equals("$")) {
            return 5;
        } else if (topOfStack.matches("\\d+")) {
            return Integer.parseInt(topOfStack);
        } else {
            return -1;
        }
    }

    // Index Calculator For Buffer Input
    static int IndexBufferInput(String input) {
        if (input.equals("id")) {
            return 0;
        } else if (input.equals("+")) {
            return 1;
        } else if (input.equals("*")) {
            return 2;
        } else if (input.equals("(")) {
            return 3;
        } else if (input.equals(")")) {
            return 4;
        } else if (input.equals("$")) {
            return 5;
        } else {
            return -1;
        }
    }

    // Index Calculator For Production Head in Grammer
    static int GrammerSymbolIndex(String head) {

        if (head.equals("E")) {
            return 0;
        } else if (head.equals("T")) {
            return 1;
        } else if (head.equals("F")) {
            return 2;
        } else {
            return -1;
        }
    }

    // ----Tokenizer-----//
    static void Tokenizer(String line, int lineNumber, int attributeValue) {
        System.out.println();
        System.out.println("=============");
        System.out.println("Line#" + lineNumber++);
        System.out.println();
        System.out.println(line);
        System.out.println();
        System.out.println("=============");
        // Init lexeme with an empty string
        String lexeme = "";
        // Looping through each character of the line
        for (int i = 0; i < line.length(); i++) {
            // Condition to check for identifiers and keywords
            if (Character.isLetter(line.charAt(i))) {
                /*
                 * Here we are concatinating the lexeme with the next char,
                 * and then we are running a while loop until the length of the line (string)
                 * and a character must be a letter or a digit or even _ which is used for
                 * variables naming we then concate the lexeme
                 */
                lexeme += line.charAt(i++);
                while (i < line.length()
                        && (Character.isLetterOrDigit(line.charAt(i)) || line.charAt(i) == '_')) {
                    lexeme += line.charAt(i++);
                }
                // Taking the pointer to one step back
                i--;
                // If our lexeme is a keyword we are returning it as a keyword
                if (iskeyword(lexeme)) {
                    System.out.println("Keyword: " + lexeme);
                    // addToken(attributeValue, lexeme, "-", "-");
                    // attributeValue++;
                    InputBuffer.add(lexeme);
                }
                // Otherwise we are returning it as a identifier
                else {
                    System.out.println("Identifier: " + lexeme);
                    addToken(attributeValue, "id", "-", lexeme);
                    attributeValue++;
                    InputBuffer.add("id");
                }
                // Clearing the lexeme once it's returned
                lexeme = "";
            }
            // Condition to check for integer values
            else if (Character.isDigit(line.charAt(i))) {
                /*
                 * Here we are contactinating the lexeme with the next char,
                 * and then we are running a while loop until length of the line (string)
                 * and the character must be a digit and concatinating the lexeme
                 * 
                 */
                lexeme += line.charAt(i++);
                while (i < line.length() && (Character.isDigit(line.charAt(i)))) {
                    lexeme += line.charAt(i++);
                }
                // Taking the pointer to one step back
                i--;
                // Returning the number
                addToken(attributeValue, "in", "-", lexeme);
                attributeValue++;
                System.out.println("Number : " + lexeme);
                // Clearing the lexeme once it's returned
                InputBuffer.add("in");
                lexeme = "";
            }
            // Condition to check for Strings
            else if (line.charAt(i) == '"') {
                /*
                 * Here we are contactinating the lexeme with the next char,
                 * and then we are running a while loop until length of the line (string)
                 * and concatinating the lexeme, we are using an Regex (regular expression) that
                 * checks if our lexeme is a string or not by simply that if anything is between
                 * quotes "" then instantly break loop because our string has been formed
                 * 
                 */
                lexeme += line.charAt(i++);
                while (i < line.length()) {
                    lexeme += line.charAt(i++);
                    if (lexeme.matches("\".*\"")) {
                        break;
                    }
                }
                // Taking the pointer to one step back
                i--;
                // Returning the string with trimming out the "" by creating a substring that
                // starts from 2nd index to 2nd last index
                lexeme = lexeme.substring(1, lexeme.length() - 1);
                System.out.println("String : " + lexeme);
                addToken(attributeValue, "sl", "-", lexeme);
                attributeValue++;
                // Clearing the lexeme once it's returned
                InputBuffer.add(lexeme);
                lexeme = "";
            } else if (Character.isWhitespace(line.charAt(i))) {
                // Do nothing
            } else if (line.charAt(i) == '<') {
                lexeme += line.charAt(i++);
                while (i < line.length()) {
                    lexeme += line.charAt(i++);
                    if (lexeme.matches("<>") ) {
                        InputBuffer.add("<>");
                        break;
                    }
                    else if(lexeme.matches("<=") ){
                     InputBuffer.add("<=");
                     break;   
                    }
                    else if(lexeme.matches("<.*")){
                     InputBuffer.add("<");
                     break;
                    }
                    else{

                    }

                }
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("ro : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
            } else if (line.charAt(i) == '>') {
                lexeme += line.charAt(i++);
                while (i < line.length()) {
                    lexeme += line.charAt(i++);
                    if (lexeme.matches(">=") || lexeme.matches(">.*")) {
                        break;
                    }

                }
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("ro : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
                InputBuffer.add(">");
            } else if (line.charAt(i) == '+') {
                lexeme += line.charAt(i++);
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("ao : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
                InputBuffer.add("+");

            } else if (line.charAt(i) == '-') {
                lexeme += line.charAt(i++);
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("ao : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
                InputBuffer.add("-");

            } else if (line.charAt(i) == '*') {
                lexeme += line.charAt(i++);
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("ao : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
                InputBuffer.add("*");

            }
            // Here we are defining condition for divide and single line comments
            else if (line.charAt(i) == '/') {
                // Storing into lexeme
                lexeme += line.charAt(i++);
                // Looping through the line
                while (i < line.length()) {
                    // Concatinating lexeme
                    lexeme += line.charAt(i);
                    // Increment the pointer
                    i++;
                    // Regex for matching
                    if (lexeme.matches("/[a-z]|[A-Z]|[0-9]")||lexeme.matches("/ ")) {
                        lexeme=lexeme.replaceAll(".$", "");
                        i-=2;
                        // Returning the op
                        break;
                    }
                }
                // Seprator Logic between divide and comments
                if (lexeme.startsWith("//") || lexeme.startsWith("/*")) {
                    System.out.println("comment: " + lexeme);
                } else {
                    System.out.println("divide: " + lexeme);
                }
                // Clearing the lexeme once it's returned
                InputBuffer.add(lexeme);
                lexeme = "";

            } else if (line.charAt(i) == '=') {
                lexeme += line.charAt(i++);
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("oo : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";

            } else if (line.charAt(i) == '(') {
                lexeme += line.charAt(i++);
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("oo : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
                InputBuffer.add("(");

            } else if (line.charAt(i) == ')') {
                lexeme += line.charAt(i++);
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("oo : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
                InputBuffer.add(")");

            } else if (line.charAt(i) == '{') {
                lexeme += line.charAt(i++);
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("oo : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
                InputBuffer.add("{");
            } else if (line.charAt(i) == '}') {
                lexeme += line.charAt(i++);
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("oo : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
                InputBuffer.add("}");
            } else if (line.charAt(i) == ';') {
                lexeme += line.charAt(i++);
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("oo : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
                InputBuffer.add(";");
            } else {
                // Error Handling for unrecognized lexemes
                lexeme += line.charAt(i);
                System.out.println("Lexical Error: Unrecognized Lexeme: " + lexeme+" At Line: " + (lineNumber - 1));
                InputBuffer.add(lexeme);
                lexeme = "";
            }
        }

    }

    // ======= EOUM =======//
    // Function for lexical analysis
    public void lexicalAnalysis(int mode) {
        if (mode == 0) {
            try {
                // Creating File Object for our file
                File fileObject = new File("input.txt");
                // Creating File Reader for our file Object
                FileReader fileReader = new FileReader(fileObject);
                // Creating Buffered Reader for our file Reader
                BufferedReader br = new BufferedReader(fileReader);
                // Creating Line String
                String line;
                // Init Line Number to start from 1
                int lineNumber = 1;
                // Attribute Values for Token in Symbol Table
                int attributeValue = 7;
                // Storing Keywords in Symbol Table
                addToken(0, "int", "-", "-");
                addToken(1, "char", "-", "-");
                addToken(2, "string", "-", "-");
                addToken(3, "if", "-", "-");
                addToken(4, "else", "-", "-");
                addToken(5, "do", "-", "-");
                addToken(6, "while", "-", "-");
                System.out.println("----------------------------------------------------");
                System.out.println("                LEXICAL ANALYSIS      ");
                System.out.println("----------------------------------------------------");
                // While looop until we don't have any lines
                while ((line = br.readLine()) != null) {
                    Tokenizer(line, lineNumber, attributeValue);
                    lineNumber++;
                    attributeValue++;
                }
                br.close();
            } catch (

            // Exception handling
            FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode == 1) {

            Scanner input = new Scanner(System.in);
            System.out.println("Enter a line: ");
            String line = "";
            line = input.next() + input.nextLine();

            // Init Line Number to start from 1
            int lineNumber = 1;
            // Attribute Values for Token in Symbol Table
            int attributeValue = 7;
            // Storing Keywords in Symbol Table
            addToken(0, "int", "-", "-");
            addToken(1, "char", "-", "-");
            addToken(2, "string", "-", "-");
            addToken(3, "if", "-", "-");
            addToken(4, "else", "-", "-");
            addToken(5, "do", "-", "-");
            addToken(6, "while", "-", "-");
            System.out.println("----------------------------------------------------");
            System.out.println("                LEXICAL ANALYSIS      ");
            System.out.println("----------------------------------------------------");
            Tokenizer(line, lineNumber, attributeValue);
            input.close();
        } else {
            System.out.println("Incorrect Mode");
        }
    }

    // Function To Print Symbol Table
    public void printLexicalSymbolTable() {
        System.out.println("----------------------------------------------------");
        System.out.println("                SYMBOL TABLE                 ");
        System.out.println("----------------------------------------------------");
        System.out.println("|Attribute Value |  Token Name  | Type |   Value   |");
        System.out.println("----------------------------------------------------");
        // Looping through the arrayList
        for (Token token : SymbolTable) {
            System.out.println(token.toString());
        }
    }

    // Function for lexical and Syntax analysis
    public void SyntaxAnalysis() {
        System.out.println("----------------------------------------------------");
        System.out.println("                SYNTAX ANALYSIS      ");
        System.out.println("----------------------------------------------------");
        // Adding $ in the end of the Input Buffer
        InputBuffer.add("$");
        // Pushing 0 as the start state in our stack
        CurrentStack.push("0");
        // Getting Input from InputBuffer
        String input = InputBuffer.get(InputBufferPointer);
        // Running Infinite Loop
        while (true) {
            // Getting Input from InputBuffer
            input = InputBuffer.get(InputBufferPointer);
            // Getting Top of the stack
            String topOfStack = CurrentStack.peek();
            // Printing The Current Input
            System.out.println("Current Input: "+input);
            // Printing The Current Stack
            System.out.println("Current Stack: "+CurrentStack.toString());
            /**
             * Here I am using various Index calculator functions that i made because
             * of my data structure to access 2D arrays i need index values rather than
             * the value itself so i am using functions that i made that calculates the
             * index
             * according the given input (Utility Functions are defined Above in the class
             * as static functions)
             * 
             */
            int s = IndexStackPeek(topOfStack);
            int BufferIndexEquivalent = IndexBufferInput(input);
            if(s!=-1&&BufferIndexEquivalent!=-1){
            // This is the condition for shift move
            if (ActionTable[s][BufferIndexEquivalent].startsWith("s")) {
                // Retriveing Which State to shift
                String ShiftState = ActionTable[s][BufferIndexEquivalent].substring(1);
                System.out.println("\nShift s"+ShiftState);
                // Pushing Input Value of Buffer and the shift state
                CurrentStack.push(input);
                CurrentStack.push(ShiftState);
                // Incrementing to the next input in the buffer using this pointer variable
                InputBufferPointer++;
            }
            // This is the condition for reduce state
            else if (ActionTable[s][BufferIndexEquivalent].startsWith("r")) {
                // Retriveing Which State to reduce
                String reduceState = ActionTable[s][BufferIndexEquivalent].substring(1);
                // Calculating the Pop length
                System.out.println("\nReduce  r"+reduceState);
                int popLen = Grammer[Integer.parseInt(reduceState) - 1].length;
                // Popping from stack according the length
                for (int j = 1; j < 2 * popLen - 1; j++) {
                    CurrentStack.pop();
                }
                // Updating the top of the stack value
                topOfStack = CurrentStack.peek();
                /**
                 * Here I am using various Index calculator functions that i made because
                 * of my data structure to access 2D arrays i need index values rather than
                 * the value itself so i am using functions that i made that calculates the
                 * index
                 * according the given input (Utility Functions are defined Above in the class
                 * as static functions)
                 * 
                 */
                int topOfStackIndexEquivalent = IndexStackPeek(topOfStack);
                // Pushing The Symbol in the Stack
                CurrentStack.push(Grammer[Integer.parseInt(reduceState) - 1][0]);
                int GrammerSymbolIndexEquivalent = GrammerSymbolIndex(Grammer[Integer.parseInt(reduceState) - 1][0]);
                // Pushing The GOTO Table value into the stack
                CurrentStack.push(GotoTable[topOfStackIndexEquivalent][GrammerSymbolIndexEquivalent]);
                System.out.println();
                // Printing out the Production Rule
                for (int z = 0; z < Grammer[Integer.parseInt(reduceState) - 1].length; z++) {
                    if (z == 0) {
                        System.out.print(
                                Grammer[Integer.parseInt(reduceState) - 1][z] + "-->");
                    } else {
                        System.out.print(
                                Grammer[Integer.parseInt(reduceState) - 1][z]);
                    }
                }
                System.out.println();
            }
            // Condtion For Acceptance
            else if (ActionTable[s][BufferIndexEquivalent].equals("accept")) {
                System.out.println("\n Compiled Successfully!");
                break;
            }
            else{
                System.out.println("Syntax Error At Line:1 Unexpected Symbol: " + InputBuffer.get(InputBufferPointer));
                break;
            }
            }
            // Error Routine
            else {
                System.out.println("Syntax Error At Line:1 Unexpected Symbol: " + InputBuffer.get(InputBufferPointer));
                break;
            }
        }
    }

    // Main Function
    public static void main(String[] args) {
        // Scanner Class Object
        Scanner sc = new Scanner(System.in);
        // Compiler Class Object
        CompilerClass obj = new CompilerClass();
        /**
         * Main Menu Display For Compiler Options
         * 
         * Options to choose from
         * 
         * 1. Lexical Analysis
         * 2. Lexical Analysis and Syntax Analysis
         * 
         */
        System.out.println("Select Following Feature of compiler");
        System.out.println("1. Lexical Analysis ");
        System.out.println("2. Lexical Analysis and Syntax Analysis ");
        System.out.print("Please Select Your Choice:");
        int choice = sc.nextInt();

        // Basic Menu Coditions if our choice is 1 go to lexical other wise if our
        // choice is 2 go for both combo otherwise show error
        if (choice == 1) {
            obj.lexicalAnalysis(0);
            obj.printLexicalSymbolTable();

        } else if (choice == 2) {
            obj.lexicalAnalysis(1);
            obj.printLexicalSymbolTable();
            obj.SyntaxAnalysis();
        } else {
            System.out.println("Error Wrong Choice");
        }
    }

}
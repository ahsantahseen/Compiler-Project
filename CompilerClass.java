
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
                lexeme = "";
            } else if (Character.isWhitespace(line.charAt(i))) {
                // Do nothing
            } else if (line.charAt(i) == '<') {
                lexeme += line.charAt(i++);
                while (i < line.length()) {
                    lexeme += line.charAt(i++);
                    if (lexeme.matches("<>") || lexeme.matches("<=") || lexeme.matches("<.*")) {
                        break;
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
                    if (lexeme.matches("/ ")) {
                        // Returning the op
                        break;
                    }
                }
                // Seprator Logic between divide and comments
                if (lexeme.startsWith("//")) {
                    System.out.println("comment: " + lexeme);
                } else {
                    System.out.println("divide: " + lexeme);
                }
                // Clearing the lexeme once it's returned
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
            } else if (line.charAt(i) == '}') {
                lexeme += line.charAt(i++);
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("oo : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
            } else if (line.charAt(i) == ';') {
                lexeme += line.charAt(i++);
                // Taking the pointer to one step back
                i--;
                // Returning the op
                System.out.println("oo : " + lexeme);
                // Clearing the lexeme once it's returned
                lexeme = "";
            } else {
                // Error Handling for unrecognized lexemes
                lexeme += line.charAt(i);
                System.out.println("Error: Unrecognized Lexeme: " + lexeme);
                System.out.println("Line: " + (lineNumber - 1));
                lexeme = "";
            }
        }
    }

    // ======= EOUM =======//
    // Function for lexical analysis
    public void lexicalAnalysis(int mode) {
        System.out.println("Lexical Analysis");
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
        System.out.println("Syntax");
        InputBuffer.add("$");
        CurrentStack.push("0");
        String input = InputBuffer.get(InputBufferPointer);
        int i = 0;
        while (true) {
            input = InputBuffer.get(InputBufferPointer);
            String topOfStack = CurrentStack.peek();
            // Debug Lines are commented
            // System.out.println(topOfStack);
            System.out.println(CurrentStack.toString());
            int s = IndexStackPeek(topOfStack);

            int BufferIndexEquivalent;
            if (input.equals("id")) {
                BufferIndexEquivalent = 0;
            } else if (input.equals("+")) {
                BufferIndexEquivalent = 1;
            } else if (input.equals("*")) {
                BufferIndexEquivalent = 2;
            } else if (input.equals("(")) {
                BufferIndexEquivalent = 3;
            } else if (input.equals(")")) {
                BufferIndexEquivalent = 4;
            } else if (input.equals("$")) {
                BufferIndexEquivalent = 5;
            } else {
                BufferIndexEquivalent = -1;
            }
            if (ActionTable[s][BufferIndexEquivalent].startsWith("s")) {
                // System.out.println("Shift State");
                String ShiftState = ActionTable[s][BufferIndexEquivalent].substring(1);
                // System.out.println("Shift " + ShiftState);
                CurrentStack.push(input);
                CurrentStack.push(ShiftState);
                InputBufferPointer++;
                // System.out.println(CurrentStack.toString());
            } else if (ActionTable[s][BufferIndexEquivalent].startsWith("r")) {
                // System.out.println("Reduce State");
                String reduceState = ActionTable[s][BufferIndexEquivalent].substring(1);
                // System.out.println("reduce " + reduceState);
                int popLen = Grammer[Integer.parseInt(reduceState) - 1].length;
                // System.out.println("len: " + (popLen - 1));
                for (int j = 1; j < 2 * popLen - 1; j++) {
                    CurrentStack.pop();
                }
                topOfStack = CurrentStack.peek();
                int topOfStackIndexEquivalent = IndexStackPeek(topOfStack);
                CurrentStack.push(Grammer[Integer.parseInt(reduceState) - 1][0]);
                // System.out.println(CurrentStack.toString());
                int GrammerSymbolIndexEquivalent;

                if (Grammer[Integer.parseInt(reduceState) - 1][0].equals("E")) {
                    GrammerSymbolIndexEquivalent = 0;
                } else if (Grammer[Integer.parseInt(reduceState) - 1][0].equals("T")) {
                    GrammerSymbolIndexEquivalent = 1;
                } else if (Grammer[Integer.parseInt(reduceState) - 1][0].equals("F")) {
                    GrammerSymbolIndexEquivalent = 2;
                } else {
                    GrammerSymbolIndexEquivalent = -1;
                }
                // System.out.println("SS: " + topOfStackIndexEquivalent);
                // System.out.println("GS: " + GrammerSymbolIndexEquivalent);
                // Error Here Need Correct Indexing here
                // System.out.println("Pushing: " +
                // GotoTable[topOfStackIndexEquivalent][GrammerSymbolIndexEquivalent]);
                CurrentStack.push(GotoTable[topOfStackIndexEquivalent][GrammerSymbolIndexEquivalent]);
                // System.out.println("After Goto: " + CurrentStack.toString());
                System.out.println();
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
            } else if (ActionTable[s][BufferIndexEquivalent].equals("accept")) {
                System.out.println("Compiled!");
                break;
            } else {
                System.out.println("Error");
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
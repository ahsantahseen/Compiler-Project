
/*
Compiler Construction Project 

Author: Muhammad Ahsan 1912310 Section 5F BSCS
Language: Java 
Compiled with: JavaSE-13
Development Resources Used: Microsoft Visual Studio Code with Redhat Java Extension 

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CompilerClass {

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

    // Function for lexical analysis
    public void lexicalAnalysis() {
        System.out.println("Lexical Analysis");
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
            // While looop until we don't have any lines
            while ((line = br.readLine()) != null) {
                System.out.println("Line#" + lineNumber++);
                System.out.println(line);
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
                        }
                        // Otherwise we are returning it as a identifier
                        else {
                            System.out.println("Identifier: " + lexeme);
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

                    } else if (line.charAt(i) == ')') {
                        lexeme += line.charAt(i++);
                        // Taking the pointer to one step back
                        i--;
                        // Returning the op
                        System.out.println("oo : " + lexeme);
                        // Clearing the lexeme once it's returned
                        lexeme = "";
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
                        System.out.println("Line: " + lineNumber);
                        lexeme = "";
                    }
                }
            }
            br.close();
        } catch (

        // Exception handling
        FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function for lexical and Syntax analysis
    public void lexicalAndSyntaxAnalysis() {
        System.out.println("Lexical + Syntax");
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
        sc.close();
        // Basic Menu Coditions if our choice is 1 go to lexical other wise if our
        // choice is 2 go for both combo otherwise show error
        if (choice == 1) {
            obj.lexicalAnalysis();
        } else if (choice == 2) {
            obj.lexicalAndSyntaxAnalysis();
        } else {
            System.out.println("Error Wrong Choice");
        }
    }

}
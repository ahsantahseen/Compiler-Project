import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class CompilerClass {
    static boolean iskeyword(String str) {
        String keywords[] = { "int", "char", "string", "if", "else", "do", "while" };
        if (!Character.isLowerCase(str.charAt(0))) {
            return false;
        }
        for (int i = 0; i < keywords.length; i++) {
            if (str.matches(keywords[i])) {
                return true;
            }
        }
        return false;
    }

    public void lexicalAnalysis() {
        System.out.println("Lexical Analysis");
        try {
            File fileObject = new File("input.txt");
            FileReader fileReader = new FileReader(fileObject);
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                System.out.println("Line#" + lineNumber++);
                System.out.println(line);
                String lexeme = "";
                for (int i = 0; i < line.length(); i++) {
                    if (Character.isLetter(line.charAt(i))) {
                        lexeme += line.charAt(i++);
                        while (i < line.length()
                                && (Character.isLetterOrDigit(line.charAt(i)) || line.charAt(i) == '_')) {
                            lexeme += line.charAt(i++);
                        }
                        i--;
                        if (iskeyword(lexeme)) {
                            System.out.println("Keyword: " + lexeme);
                        } else {
                            System.out.println("Identifier: " + lexeme);
                        }
                        lexeme = "";
                    }
                }
            }
        } catch (

        FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lexicalAndSyntaxAnalysis() {
        System.out.println("Lexical + Syntax");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CompilerClass obj = new CompilerClass();
        System.out.println("Select Following Feature of compiler");
        System.out.println("1. Lexical Analysis ");
        System.out.println("2. Lexical Analysis and Syntax Analysis ");
        System.out.print("Please Select Your Choice:");
        int choice = sc.nextInt();
        sc.close();

        if (choice == 1) {
            obj.lexicalAnalysis();
        } else if (choice == 2) {
            obj.lexicalAndSyntaxAnalysis();
        } else {
            System.out.println("Error Wrong Choice");
        }
    }

}
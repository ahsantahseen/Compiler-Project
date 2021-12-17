import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class CompilerClass {
    public void lexicalAnalysis() {
        System.out.println("Lexical Analysis");
        try {
            File fileObject = new File("input.txt");
            Scanner fileReader = new Scanner(fileObject);
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                String lines[] = data.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String lexeme[] = lines[i].split(" ");
                    System.out.println(Arrays.toString(lexeme));
                    for (int j = 0; j < lexeme.length; j++) {
                        if (lexeme[j].equals("int")) {
                            System.out.println("<" + lexeme[j].toString() + ">,<0>");
                        } else if (lexeme[j].equals("char")) {
                            System.out.println("<" + lexeme[j].toString() + ">,<1>");
                        } else if (lexeme[j].equals("string")) {
                            System.out.println("<" + lexeme[j].toString() + ">,<2>");
                        } else if (lexeme[j].equals("if")) {
                            System.out.println("<" + lexeme[j].toString() + ">,<3>");
                        } else if (lexeme[j].equals("else")) {
                            System.out.println("<" + lexeme[j].toString() + ">,<4>");
                        } else if (lexeme[j].equals("do")) {
                            System.out.println("<" + lexeme[j].toString() + ">,<5>");
                        } else if (lexeme[j].equals("while")) {
                            System.out.println("<" + lexeme[j].toString() + ">,<6>");
                        } else if (lexeme[j].matches(
                                "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|v|u|w|x|y|z).(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|0|1|2|3|4|5|6|7|8|9)*.(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|0|1|2|3|4|5|6|7|8|9)")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<id>,<" + lexeme[j].toString() + ">");
                        } else if (lexeme[j].matches(
                                "\".*\"")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<sl>,<" + lexeme[j].toString() + ">");
                        } else if (lexeme[j].matches(
                                "[0-9]+")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<in>,<" + lexeme[j].toString() + ">");
                        } else if (lexeme[j].equals("<")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<ro>,<lt>");
                        } else if (lexeme[j].equals("<=")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<ro>,<le>");
                        } else if (lexeme[j].equals("==")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<ro>,<eq>");
                        } else if (lexeme[j].equals("!=")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<ro>,<ne>");
                        } else if (lexeme[j].equals(">")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<ro>,<gt>");
                        } else if (lexeme[j].equals(">=")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<ro>,<ge>");
                        } else if (lexeme[j].equals("+")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<ao>,<ad>");
                        } else if (lexeme[j].equals("-")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<ao>,<sb>");
                        } else if (lexeme[j].equals("*")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<ao>,<ml>");
                        } else if (lexeme[j].equals("/")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<ao>,<dv>");
                        } else if (lexeme[j].equals("=")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<oo>,<as>");
                        } else if (lexeme[j].equals("(")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<oo>,<op>");
                        } else if (lexeme[j].equals(")")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<oo>,<cp>");
                        } else if (lexeme[j].equals("{")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<oo>,<ob>");
                        } else if (lexeme[j].equals("}")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<oo>,<cb>");
                        } else if (lexeme[j].equals(";")) {
                            System.out
                                    .println("<" + lexeme[j].toString() + ">,<oo>,<tr>");
                        } else {
                            System.out.println("not-recognized");
                        }
                    }
                }
            }
            fileReader.close();
        } catch (

        FileNotFoundException e) {
            System.out.println("An error occurred.");
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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CompilerClass {
    public void lexicalAnalysis() {
        System.out.println("Lexical Analysis");
        try {
            File fileObject = new File("input.txt");
            Scanner fileReader = new Scanner(fileObject);
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                String tokens[] = data.split(" ");
                for (int i = 0; i < tokens.length; i++) {
                    System.out.println(tokens[i]);
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main {
    public static void lexicalAnalysis() {
        System.out.println("Lexical");
        try {
            File fileObject = new File("./lexicalInput.txt");
            Scanner fileReader = new Scanner(fileObject);
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                System.out.println(data);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void lexicalAndSyntaxAnalysis() {
        System.out.println("Lexical + Syntax");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select Following Feature of compiler");
        System.out.println("1. Lexical Analysis ");
        System.out.println("2. Lexical Analysis and Syntax Analysis ");
        System.out.print("Please Select Your Choice:");
        int choice = sc.nextInt();
        sc.close();
        if (choice == 1) {
            lexicalAnalysis();
        } else if (choice == 2) {
            lexicalAndSyntaxAnalysis();
        } else {
            System.out.println("Error Wrong Choice");
        }
    }

}
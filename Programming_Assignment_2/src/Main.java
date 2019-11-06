import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the filepath: ");
        String datafile = scanner.nextLine();
        Parser parser = new Parser(datafile);
        System.out.println("Result: "+parser.program());
    }
}

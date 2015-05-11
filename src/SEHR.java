
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


/**
 * SEHR - Semantic Electronic Health Record
 * @author Markéta Wolfová, Martin Graubner, Adam Barák, Marek Ľuptáčik
 */
public class SEHR {

    /**
     * 
     * main method for joining of libraries Converter and Importer
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException 
     * @throws java.io.IOException
     */
    public static void main( String[] args ) throws FileNotFoundException, IOException {
        
        welcomeInstructions();
        
        // Ask for directory path
        // Path example: C:\Users\Admin\Documents\archetypes
         Scanner sc = new Scanner( System.in );
        System.out.print( "Path to directory with archetypes: " );
        String dirPath = sc.nextLine();
        
        // Converts archetypes (ADL files) to JSON file
        Converter.ConvertArchetypes( dirPath );
        
        // Import archetypes from JSON file to elasticsearch
        // First parameter stands for cluster name, second for path to JSON file 
        String[] elasticargs = {"elasticsearch","output.json"};
        Importer.main(elasticargs);
    }
    
    /**
     * convenience method to welcome user
     */
    public static void welcomeInstructions()
    {
        System.out.println("Welcome to SEHR - Semantic Electonic Health Record");
        System.out.println("- path example: C:\\Users\\Admin\\Documents\\archetypes");
        System.out.println("- you can review JSON messages in the same directory as your program directory");
    }
}


import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * 
 * @author Markéta Wolfová
 */
public class SEHR {

    /**
     * 
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main( String[] args ) throws FileNotFoundException {
        
        // Ask for directory path
        // Path example: C:\Users\Admin\Documents\archetypes\
        Scanner sc = new Scanner( System.in );
        System.out.println( "Path to directory with archetypes: " );
        String dirPath = sc.nextLine();
        
        // Converts archetypes (ADL files) to JSON file
        Converter.ConvertArchetypes( dirPath );
    }
    
}

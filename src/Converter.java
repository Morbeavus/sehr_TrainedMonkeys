
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.openehr.am.archetype.Archetype;

/**
 * ADL to JSON converter
 * @author Markéta Wolfová
 */
public class Converter {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main( String[] args ) throws FileNotFoundException {
        
        // Ask for directory path
        Scanner sc = new Scanner( System.in );
        System.out.print( "Cesta ke slozce s archetypy: " );
        String dirPath = sc.nextLine();
        
        // Load archetypes
        ArrayList<File> archetypes = Loader.LoadFiles( dirPath );
        Archetype archetypeObject[] = Loader.ParseFiles( archetypes );

        // Parse archetypes
        File output = new File( "output.txt" );
        PrintWriter pw = new PrintWriter( output );
        
        for ( int i = 0; i < archetypeObject.length; i++ ) {
            
            pw.println( DataParser.GetFileName( archetypeObject[ i ] ));
            pw.println( DataParser.GetFileID( archetypeObject[ i ] ));
            pw.println( DataParser.GetFileDate( archetypeObject[ i ] ));
            
            pw.println();
        }
        
        pw.close();
    }
}

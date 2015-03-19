import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

import org.openehr.am.archetype.Archetype;
import se.acode.openehr.parser.ADLParser;

/**
 * ADL to JSON converter
 * @author Markéta Wolfová
 */
public class Converter {

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args ) {
        
        Scanner sc = new Scanner( System.in );
        System.out.print( "Cesta ke slozce s archetypy: " );
        String dirPath = sc.nextLine();
        
        // *** Archetype Loader ***
        
        File dir = new File( dirPath );
        ArrayList<File> archetypes = new ArrayList(); // seznam cest k archetypum
        
        File files[] = dir.listFiles(); // pole vsech souboru ve slozce dir
        int pocetArch = 0;
        try {
            for ( int i = 0; i < files.length; i++ ) {
            
                String filePath = files[ i ].getAbsolutePath();
                if ( filePath.substring( filePath.length() - 4 ).equals( ".adl" )) { // kontrola formatu, nacita jen ADL soubory

                    archetypes.add( new File( filePath ));
                    pocetArch++;
                }
            }
        }
        catch( Exception e ) {
                
            System.out.println( "Wrong directory path or empty directory." );
            System.exit( 0 );
        }
        
        System.out.println( "Pocet nactenych archetypu: "+ pocetArch ); // pomocne
        if ( pocetArch == 0 ) {
        
            System.out.println( "Directory does not contain any archetype files." );
            System.exit( 0 );
        }
        
        // --------------------
        
        
        
        // *** ADL to AOM Parser ***
        
        Archetype archetypeObjects[] = new Archetype[ archetypes.size()];
        
        for ( int i = 0; i < archetypes.size(); i++ ) {
            
            try {
                
                //archetypeObjects[ i ] = new ADLParser( archetypes.get( i )).parse();
            }
            catch ( Exception e ) {
                
                System.out.println( "Couldn't parse an archetype to objects." );
            }
        }
        
        // --------------------
    }
}

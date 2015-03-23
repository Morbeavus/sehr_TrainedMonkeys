
import java.io.File;
import java.util.ArrayList;
import org.openehr.am.archetype.Archetype;
import se.acode.openehr.parser.ADLParser;

/**
 * Loading and parsing archetypes
 * @author Markéta Wolfová
 */
public class Loader {
    
    /**
     * Loading archetypes
     * @param dirPath path to a directory with archetypes
     * @return archetypes Array list with archetypes (ADL files)
     */
    public static ArrayList<File> LoadFiles( String dirPath ) {
        
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
        
        if ( pocetArch == 0 ) {
        
            System.out.println( "Directory does not contain any archetype files." );
            System.exit( 0 );
        }
        
        return archetypes;
    }
    
    /**
     * Parsing archetypes
     * @param archetypes Array list with archetypes (ADL files)
     * @return archetypeObjects array with archetypes parsed to objects (AOM files)
     */
    public static Archetype[] ParseFiles( ArrayList<File> archetypes ) {
        
        Archetype archetypeObject[] = new Archetype[ archetypes.size()];
        
        for ( int i = 0; i < archetypes.size(); i++ ) {
            
            try {
                
                archetypeObject[ i ] = new ADLParser( archetypes.get( i )).parse();
            }
            catch ( Exception e ) {
                
                System.out.println( "Couldn't parse an archetype to objects." );
                System.exit( 0 );
            }
        }
        
        return archetypeObject;
    }
    
}

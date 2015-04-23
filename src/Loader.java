
import java.io.File;
import java.util.ArrayList;

import org.openehr.am.archetype.Archetype;
import se.acode.openehr.parser.ADLParser;

/**
 * Loading and parsing archetypes (ADL files)
 * @author Markéta Wolfová
 */
public class Loader {
    
    /**
     * Loading archetypes
     * @param dirPath path to a directory with archetypes
     * @return ArrayList with archetypes (ADL files) found in specified direcotry
     */
    public static ArrayList<File> LoadFiles( String dirPath ) {
        
        File dir = new File( dirPath );
        ArrayList<File> archetypes = new ArrayList();   // list of paths to archetypes
    
        File[] files = dir.listFiles(); // array of all files in specified directory
        int pocetArch = 0;
        try {
            
            for ( File file : files ) {
                String filePath = file.getAbsolutePath();
                if ( filePath.substring( filePath.length() - 4 ).equals( ".adl" )) { // controlling formats, loads only ADL files to new ArrayList
                    
                    archetypes.add( new File( filePath ));
                    pocetArch++;
                }
            }
        }
        catch ( Exception e ) {
            
            System.out.println( "ERROR: Wrong directory path or empty directory." );
            System.exit( 0 );
        }
        
        if ( pocetArch == 0 ) {
            
            System.out.println( "WARNING: Directory does not contain any archetype files." );
            System.exit( 0 );
        }
        
        return archetypes;
    }
  
    /**
     * Parsing archetypes to objects (AOM files)
     * @param archetypes ArrayList with archetypes (ADL files)
     * @return archetypeObjects array with archetypes parsed to objects (AOM files)
     */
    public static Archetype[] ParseFiles( ArrayList<File> archetypes ) {
        
        Archetype[] archetypeObject = new Archetype[ archetypes.size() ];
        for ( int i = 0; i < archetypes.size(); i++ ) {
            try {
                
                archetypeObject[ i ] = new ADLParser((File)archetypes.get( i )).parse();
            }
            catch ( Exception e ) {
                
                System.out.println( "Couldn't parse an archetype to objects." );
                System.exit( 0 );
            }
        }
        
        return archetypeObject;
    }
    
}

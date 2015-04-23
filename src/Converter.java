
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.constraintmodel.CObject;

/**
 * ADL to JSON converter
 * @author Markéta Wolfová
 */
public class Converter {
    
    /**
     * writes to file
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main( String[] args ) throws FileNotFoundException {
        
        // Ask for directory path
        Scanner sc = new Scanner( System.in );
        System.out.println( "Path to directory with archetypes: " );
        String dirPath = sc.nextLine();
        
        ConvertArchetypes( dirPath );
    }
  
    /**
     * writes to file
     * @param dirPath path to cirectory containing archetypes
     * @throws java.io.FileNotFoundException
     */
    public static void ConvertArchetypes( String dirPath ) throws FileNotFoundException {
        
        // Load archetypes
        ArrayList<File> archetypes = Loader.LoadFiles( dirPath );
        Archetype[] archetypeObject = Loader.ParseFiles( archetypes );

        // Parse archetype data and print to JSON file
        File output = new File( "output.json" );
        PrintWriter pw = new PrintWriter( output );
        ArchetypeTree tree = new ArchetypeTree();
        for ( int i = 0; i < archetypeObject.length; i++ ) {
            
            pw.println( "{" );
            pw.println( "   \"description\":\"" + DataParser.GetFileType( archetypeObject[ i ] ).toLowerCase() + "\"," );
            pw.println( "   \"name\":\"" + DataParser.GetFileID( archetypeObject[ i ]) + "\"," );
            SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
            pw.println( "   \"postDate\":\"" + sdf.format(((File)archetypes.get( i )).lastModified()) + "\"," );

            pw.println( "   \"properties\":{" );
            buildTree( archetypeObject[ i ], tree );
            tree.print( pw, -1 );                                                 // -1 beginning of listing, node is set to root
            pw.println( "   }" );

            pw.println( "}" );
            pw.println();

            tree.clear();
        }
        
        pw.close();
    }
  
    /**
     * saves values from PathNodeMap to ArrayListu and builds tree
     * @param archetypeObject archetype objects (AOM file)
     * @param tree instance of tree
     */
    public static void buildTree( Archetype archetypeObject, ArchetypeTree tree ) {
        
        Object[] mapKeys = DataParser.GetMapKeySet( archetypeObject );          // array of PathNodeMap keys to access their values
        ArrayList<CObject> mapValues = new ArrayList();                         // list of values from PathNodeMap
        
        // saves object to arraylist
        for ( int j = 0; j < DataParser.GetMapSize( archetypeObject ); j++ ) {
            
            mapValues.add( DataParser.GetMapValue( archetypeObject, mapKeys[ j ].toString()));
        }
        
        pushRoot( tree, mapValues );
        pushRootsChildren( tree, mapValues );
        pushChildren( tree, mapValues );
        /*if ( useless ) System.out.println( archetypeObject.getArchetypeId().localID());*/
        pushTypes( tree, mapValues );
    }

    /**
     * deletes unimportant objects and adds root to tree
     * @param tree instance of tree
     * @param mapValues ArrayList of values from PathNodeMap
     */
    public static void pushRoot( ArchetypeTree tree, ArrayList<CObject> mapValues ) {
        
        for ( int j = 0; j < mapValues.size(); j++ ) {
            if (( DataParser.GetIDFromObject((CObject)mapValues.get( j )) == null ) && 
                ( !DataParser.GetNameFromObject((CObject)mapValues.get( j )).contains( "DV_" )))
            {
                mapValues.remove( j );
                j--;
            }
            else if ( DataParser.GetIsRootFromObject((CObject)mapValues.get( j ))) {
                
                tree.push( DataParser.GetIDFromObject((CObject)mapValues.get( j )), DataParser.GetIsRequiredFromObject((CObject)mapValues.get( j )), 
                DataParser.GetOccurrencesFromObject((CObject)mapValues.get( j )), DataParser.GetParentFromObject((CObject)mapValues.get( j )));

                mapValues.remove( j );
                j--;
            }
        }
    }

    /**
     * inserts root's children to tree
     * @param tree instance of tree
     * @param mapValues ArrayList of values from PathNodeMap
     */
    public static void pushRootsChildren( ArchetypeTree tree, ArrayList<CObject> mapValues ) {
        
        for ( int j = 0; j < mapValues.size(); j++ ) {
            
            if (( DataParser.GetParentFromObject((CObject)mapValues.get( j )).equals( "" )) && 
            ( !DataParser.GetNameFromObject((CObject)mapValues.get( j )).contains( "DV_" ))) {
                
                tree.push( DataParser.GetIDFromObject((CObject)mapValues.get( j )), DataParser.GetIsRequiredFromObject((CObject)mapValues.get( j )), 
                            DataParser.GetOccurrencesFromObject((CObject)mapValues.get( j )), DataParser.GetParentFromObject((CObject)mapValues.get( j )));

                mapValues.remove( j );
                j--;
            }
        }
    }

    
  /*static boolean useless = false;*/
  /**
     * inserts rest of the children
     * @param tree instance of tree
     * @param mapValues ArrayList of values from PathNodeMap
     */
    public static void pushChildren( ArchetypeTree tree, ArrayList<CObject> mapValues ) {
        
        boolean pushingDone, 
                repeat = true;  // indicates if pushing object was successful
        while ( repeat ) {  // indicates if while should repeat (when at least one pushing failed)
            
            repeat = false;
            /*useless = false;*/ 
            for ( int j = 0; j < mapValues.size(); j++ ) {
                if ( !DataParser.GetNameFromObject((CObject)mapValues.get( j )).contains( "DV_" )) {
                
                    /*pushingDone = tree.temp(DataParser.GetIDFromObject( mapValues.get( j )));
                    if ( pushingDone ) {
                        
                        useless = true;
                    } */
                    pushingDone = tree.push(DataParser.GetIDFromObject((CObject)mapValues.get( j )), DataParser.GetIsRequiredFromObject((CObject)mapValues.get( j )), 
                                          DataParser.GetOccurrencesFromObject((CObject)mapValues.get( j )), DataParser.GetParentFromObject((CObject)mapValues.get( j )));
                    if ( pushingDone ) {
                    
                        mapValues.remove( j );
                        j--;
                    }
                    else {
                        
                        repeat = true;
                    }
                }
            }
        }
  }

    /**
     * adds data type to tree's node
     * @param tree instance of tree
     * @param mapValues ArrayList of values from PathNodeMap
     */
    public static void pushTypes( ArchetypeTree tree, ArrayList<CObject> mapValues ) {
        
        for ( int j = 0; j < mapValues.size(); j++ ) {
            
            tree.push(DataParser.GetNameFromObject((CObject)mapValues.get( j )), DataParser.GetParentFromObject((CObject)mapValues.get( j )));

            mapValues.remove( j );
            j--;
        }
    }
    
}

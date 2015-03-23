
import org.openehr.am.archetype.Archetype;

/**
 * Finds and saves important data from archetypes
 * @author Markéta Wolfová
 */
public class DataParser {
    
    /**
     * Gets name of an archetype
     * @param archetype archetype objects
     * @return archetype name
     */
    public static String GetFileName( Archetype archetype ) {
        
        return archetype.getArchetypeId().conceptName();
    }
    
    /**
     * Gets ID of an archetype
     * @param archetype archetype objects
     * @return archetype ID
     */
    public static String GetFileID( Archetype archetype ) {
        
        return archetype.getArchetypeId().localID();
    }   

    /**
     * Gets date of the latest revision of an archetype
     * @param archetype archetype objects
     * @return archetype date
     */
    public static String GetFileDate( Archetype archetype ) {
        
        return archetype.getDescription().getOriginalAuthor().toString();
    }
 
    
}

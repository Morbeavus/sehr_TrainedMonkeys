
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.constraintmodel.CObject;


/**
 * Finds and saves important data from archetype's objects
 * @author Markéta Wolfová
 */
public class DataParser {
    
    //                                                                           *** Archetype methods ***
    
    /**
     * Gets type of an archetype
     * @param archetype archetype objects
     * @return archetype type as String
     */
    public static String GetFileType( Archetype archetype ) {
        
        return archetype.getArchetypeId().rmEntity();
    }
    
    /**
     * Gets ID of an archetype
     * @param archetype archetype objects
     * @return archetype ID as String
     */
    public static String GetFileID( Archetype archetype ) {
        
        return archetype.getArchetypeId().localID();
    }
    
    //                                                                           *** PathNodeMap methods ***
    
    /**
     * Gets size of archetype's PathNodeMap
     * @param archetype archetype objects
     * @return size of map (Integer)
     */
    public static int GetMapSize( Archetype archetype ) {
        
        return archetype.getPathNodeMap().size();
    }
    
    /**
     * Gets PathNodeMaps keys to access their values
     * @param archetype archetype objects
     * @return array of map keys (Objects)
     */
    public static Object[] GetMapKeySet( Archetype archetype ) {
        
        return archetype.getPathNodeMap().keySet().toArray();
    }
    
    /**
     * Gets maps values (CObjects)
     * @param archetype archetype objects
     * @param key an object to access NodePathMap value
     * @return map value of given key (CObject)
     */
    public static CObject GetMapValue( Archetype archetype, String key ) {
        
        return archetype.getPathNodeMap().get( key );
    }
    
    //                                                                           *** Object methods ***
    
    /**
     * Gets objects RmTypeName
     * @param object a map value
     * @return objects RmTypeName as String
     */
    public static String GetNameFromObject( CObject object ) {
        
        return object.getRmTypeName();
    }
    
    /**
     * Gets objects ID
     * @param object a map value
     * @return objects ID as String
     */
    public static String GetIDFromObject( CObject object ) {
        
        return object.getNodeId();
    }
    
    /**
     * Gets objects IsRequired
     * @param object a map value
     * @return objects IsRequired as String
     */
    public static boolean GetIsRequiredFromObject( CObject object ) {
        
        return object.isRequired();
    }
    
    /**
     * asks if input object is root
     * @param object a map value
     * @return true if its root, false if not
     */
    public static boolean GetIsRootFromObject( CObject object ) {
        
        return object.isRoot();
    }
    
    /**
     * gets path to parent of object
     * @param object a map value
     * @return path to objects parent
     */
    public static String GetParentFromObject( CObject object ) {
        
        try {
            
            return object.getParent().parentNodePath();
        }
        catch( Exception e ) {
            
            return "";
        }
    }
    
}

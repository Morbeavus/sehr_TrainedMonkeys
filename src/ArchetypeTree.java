
import java.io.PrintWriter;


/**
 * inserts nodes to tree and prints the tree to file
 * @author Markéta Wolfová
 */
public class ArchetypeTree {
    
    ArchetypeNode root = null;
    ArchetypeNode node, nodeParent;
    
    /**
     * creates instance of tree
     */
    ArchetypeTree() { }
    
    /**
     * inserts node to tree
     * @param ID ID of object
     * @param required  restriction (if object is required when inputting data)
     * @param parent path to object parent
     * @return true if push was succefull, false if not
     */
    boolean push( String ID, boolean required, String parent ) {
        
        node = new ArchetypeNode( ID, required );
        
        if ( root == null ) root = node;
        else if ( parent.equals( "" )) {
            
            ArchetypeNode.AddChild( root, node );
            ArchetypeNode.AddParent( node, root );
            ArchetypeNode.AddRank( node, root.children.size() - 1 );
        }
        else {
            
            findNode( getNodeParent( parent ), -1 );                            // -1 is start of search, node is set to root
            if ( nodeParent != null ) {
                
                ArchetypeNode.AddChild( nodeParent, node );
                ArchetypeNode.AddParent( node, nodeParent );
                ArchetypeNode.AddRank( node, nodeParent.children.size() - 1 );
            }
            else {
                
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * adds data types to nodes of tree
     * @param name name of object and data type of its parent
     * @param parent path to objects parent
     */
    void push( String name, String parent ) {
        
        findNode( getNodeParent( parent ), -1 );
        if ( nodeParent != null ) {
            
            ArchetypeNode.AddType( nodeParent, convertType( name ));
        }
    }
    
    /**
     * renames ADL data types to JSON data types
     * @param type name of the data type
     * @return name of the data type converted to be used by elasticsearch
     */
    String convertType( String type ) {
        
        switch ( type ) {
            
            case "DV_TEXT": return "String";
            case "DV_CODED_TEXT": return "String";
            case "DV_DATE_TIME": return "Date";
            case "DV_TIME": return "Date"; // ?
            case "DV_DATE": return "Date";
            case "DV_COUNT": return "Integer";
            case "DV_ORDINAL": return "Integer";
            case "DV_IDENTIFIER": return "Integer";
            case "DV_QUANTITY": return "Double";
            case "DV_DURATION": return "Double"; // ?
            case "DV_PROPORTION": return "Float";
            case "DV_BOOLEAN": return "Boolean";
            case "DV_INTERVAL": return "Any"; // ?
            case "DV_INTERVAL<DV_COUNT>": return "Double"; // ?
            case "DV_INTERVAL<DV_QUANTITY>": return "Double"; // ?
            case "DV_INTERVAL<DV_DATE>": return "Date"; // ?
            case "DV_INTERVAL<DV_DATE_TIME>": return "Date"; // ?
            case "DV_MULTIMEDIA": return "Any"; // ?
            case "DV_PARSABLE": return "Any"; // ?
            case "DV_URI": return "String"; // ?
            case "DV_EHR_URI": return "String"; // ?
            default: System.out.println("WARNING: Unknown data type "+ type +"."); return "Any";
        }
    }
    
    /**
     * finds objects parent using path
     * @param parent path to parent
     * @return parent's ID
     */
    String getNodeParent( String parent ) {
        
        int start = 0, end = 0;
        for ( int i = parent.length() - 1; i >= 0; i-- ) {
            
            if ( parent.charAt( i ) == ']' ) {
                
                end = i;
            }
            if ( parent.charAt( i ) == '[' ) {
                
                start = i + 1;
                break;
            }
        }
        if ( start == 0 && end == 0 ) return root.ID;
        
        return parent.substring( start, end );
    }
    
    int rank;                                                                   // temp variable, saves rank of child
    /**
     * finds node using its ID
     * @param ID ID of object
     * @param i index, rank of child which is next
     *          when method is called -1 signifies root, rest is done automaticaly with recursion 
     */
    void findNode( String ID, int i ) {
        
        if ( i == -1 ) {
            
            nodeParent = root;
            i = 0;
        }
        
        if ( nodeParent.ID.equals( ID )) return;
        
        if ( nodeParent.children.isEmpty() || i >= nodeParent.children.size()) {
            
            rank = nodeParent.rank;
            nodeParent = nodeParent.parent;
            if ( nodeParent == null ) { }
            else findNode( ID, rank + 1 );
        }
        else {
            
            nodeParent = nodeParent.children.get( i );
            findNode( ID, 0 );
        }
    }
    
    /**
     * listing of the whole tree
     * @param pw instance PrintWriteru for writing to file
     * @param i index, rank of child which is next
     *          when method is called -1 signifies root, rest is done automaticaly with recursion 
     */
    void print( PrintWriter pw, int i ) {
        
        if ( i == -1 ) {
            
            node = root;
            i = 0;
        }
        
        if ( node.children.isEmpty()) {
            
            pw.println( "       \""+ node.ID +"\":{" );
            pw.println( "           \"type\":\""+ node.type +"\"," );
            pw.println( "           \"isRequired\":\""+ node.required +"\"" );
            
            rank = node.rank;
            node = node.parent;
            if ( node == null ) pw.println( "       }" );
            else print( pw, rank + 1 );
        }
        else if ( i >= node.children.size()) {
            
            pw.println( "       }" );
            
            rank = node.rank;
            node = node.parent;
            if ( node == null ) pw.println( "       }" );
            else print( pw, rank + 1 );
        }
        else {
            
            if ( i == 0 ) pw.println( "       \""+ node.ID +"\":{" );
            else pw.println( "       }," );
            
            node = node.children.get( i );
            print( pw, 0 );
        }
    }
    
    /**
     * deletes tree and prepares for next archetype
     */
    void clear() {
        
        root.children = null;
        root = null;
    }
}

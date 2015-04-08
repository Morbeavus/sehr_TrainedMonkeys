
import java.util.ArrayList;


/**
 * defines a node for building a tree
 * @author Markéta Wolfová
 */
public class ArchetypeNode {
    
    String ID, type;
    int rank; // rank of child
    boolean required;
    ArchetypeNode parent;
    ArrayList<ArchetypeNode> children;
    
    /**
     * constructor creates instance of node
     */
    ArchetypeNode() { }
    
    /**
     * constructor with paramaters
     * @param ID ID of object
     * @param required restriction (if object is required when inputting data)
     */
    ArchetypeNode( String ID, boolean required ) {
        
        this.ID = ID;
        this.required = required;
        this.parent = null;
        children = new ArrayList();
    }
    
    /**
     * adds data type to specified node
     * @param node object of node
     * @param type data type
     */
    static void AddType( ArchetypeNode node, String type ) {
        
        node.type = type;
    }
    
    /**
     * adds rank of child
     * @param node Node object
     * @param rank rank of child
     */
    static void AddRank( ArchetypeNode node, int rank ) {
        
        node.rank = rank;
    }
    
    /**
     * adds parent of node
     * @param node Node object
     * @param parent Node's parent object
     */
    static void AddParent( ArchetypeNode node, ArchetypeNode parent ) {
        
        node.parent = parent;
    }
    
    /**
     * adds child to node
     * @param node Node object
     * @param child Node's child object
     */
    static void AddChild( ArchetypeNode node, ArchetypeNode child ) {
        
        node.children.add( child );
    }
    
}

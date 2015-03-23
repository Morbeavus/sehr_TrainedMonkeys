import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;


public class main {
    public static String properities, name, path = "file.json";
    
    public static void load(){
        int c = 0, i, j;
        String current;
        Scanner scan;
        
        try {
            scan = new Scanner(new File(path));
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            return;
        }
        
        name="";
        properities="";
        
        do{
            current = scan.nextLine();
            if(current.contains("name")){
                i = current.indexOf("name")+7;
                j = i;
                while(current.charAt(j)!='"'){
                    j++;
                }
                name = current.substring(i, j);
                
                if(scan.hasNext()){
                    current = scan.nextLine();
                }
                continue;
            }
            if(current.contains("{")){
                c++;
            }
            if(current.contains("}")){
                c--;
            }
            
            
            properities += current.replaceAll("    ", "");
        }while(scan.hasNext()&&c>0);  
    }
    
    public static void main(String[] args){
        int op;
        String index_name;
        Scanner sc = new Scanner(System.in);
        
        Node node;
        
        node = nodeBuilder().clusterName("mara").node();
        Client client = node.client();

              load();
              index_name = sc.next();
              IndexResponse response = client.prepareIndex(index_name, name).setSource(properities).execute().actionGet(); //TODO třetí index
              node.close();
                    
        System.out.println("Name: "+name);
        System.out.println("Properities: "+properities);
        System.out.println("Program byl ukoncen");
    }
}

import java.io.*;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Logger;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ExceptionsHelper;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

import org.elasticsearch.indices.IndexAlreadyExistsException;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.node.Node;


public class Main {
    public static String description, name, postdate, properities;
    public static Client client;
    
    /**
    * method load loads JSON file
    * saves name of an index(description), type(name), postdate and properities
    *
    * @param path indicates path to json file
    * @return 
    */
    
    public static int load(String path){
        int i;
        String current;
        Scanner scan;
        
        // initialization of the scanner
        try {
            scan = new Scanner(new File(path));
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            return 1;
        }
        
        current = scan.nextLine();
        
        current = scan.nextLine();
        i = current.indexOf(":");
        description = current.substring(i+2, current.length()-2);
        
        current = scan.nextLine();
        i = current.indexOf(":");
        name = current.substring(i+2, current.length()-2);
        
        current = scan.nextLine();
        i = current.indexOf(":");
        postdate = current.substring(i+2, current.length()-2);
        
        current = scan.nextLine();
        i = current.indexOf(":");
        properities = "{\"properties\":{"+current.substring(i+2);
        while(scan.hasNext()){
            current = scan.nextLine();
            if(scan.hasNext()){
                properities += current;
            }
        }
        properities+="}";
        properities=properities.replaceAll(" ", "");
        
        scan.close();
        return 0;
    }
    
    /**
     * Method CreateIndex create the index according a description in JSON file.
     * adds postDate(date of creating the index).
     * @return 
     */
    
    public static int createIndex(){
        try {
			IndexResponse response = client.prepareIndex(description, name)         //index number
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("postDate", new Date())
                            .endObject()
                    )
                    .execute()
                    .actionGet();
            return 0;
        } catch (IOException | ElasticsearchException e) {
            if (ExceptionsHelper.unwrapCause(e) instanceof IndexAlreadyExistsException) {
                System.out.println("Index [" + description + "] already exists");
            }
            else System.out.println("Error in creating index");
            return 1;
        }
    }
    
    public static void createMapping(){
    	  Logger.getLogger("Elastic Search creating mapping with contents: " + properities);
    	  PutMappingRequest putMapping=new PutMappingRequest(description);
    	  putMapping.type(name).source(properities);
    	  client.admin().indices().putMapping(putMapping).actionGet();
    	}
    
    /**
     * Metoda Main connects all the methods.
     * 
     * @param args
     * @throws java.io.IOException
     */
    
    public static void main(String[] args) throws IOException{
        Node node;
          
        node = nodeBuilder().clusterName("elasticsearch").node();
        client = node.client();
        
        //access of user
        if(args.length==0){
            Scanner sc= new Scanner(System.in);
            int operator=0;
            int i;
            System.out.println("load JSON - 1\n create index - 2\n create mapping - 3\n exit program - 4");
            while(true){
                System.out.println("Enter operator");
                operator = sc.nextInt();
                if(operator == 1){ //load
                    i = load("objectree.json");
                    if(i == 0){
                        System.out.println("Load complete");
                        System.out.println("description: "+description);
                        System.out.println("name: "+name);
                        System.out.println("properities: "+properities);
                    }
                    System.out.println("-----------");
                    continue;
                }
                if(operator == 2){
                    i = createIndex();
                    if(i == 0){
                        System.out.println("Index created");
                    }
                    System.out.println("-----------");
                    continue;
                }
                if(operator == 3){
                    createMapping();
                    System.out.println("Mapping created");
                    System.out.println("-----------");
                    continue;
                }
                if(operator == 4){
                    sc.close();
                    break;
                }

                System.out.println("Invalid operator \n"
                        + "load JSON - 1\n"
                        + "create index - 2\n"
                        + "create mapping - 3\n"
                        + "exit program - 4");
            }
        }
        
        //access of program
        if(args.length==1){
            int i;
            i = load(args[0]);
            if(i == 0){
                i = createIndex();
                if(i == 0){
                    createMapping();
                }
            }
        }
        
        //Invalid arguments
        if(args.length>1){
            System.out.println("Invalid arguments");
        }
        
        client.close();
        node.close();  
        
        System.out.println("Program terminated");
    }
}

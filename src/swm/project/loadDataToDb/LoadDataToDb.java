/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swm.project.loadDataToDb;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import swm.project.Consts;

/**
 * @author Kruti
 */
public class LoadDataToDb {
    MongoClient mongoClient;
    MongoDatabase db;
    
    
    public LoadDataToDb() {
        connectToDb();
    }
    
      
    private void connectToDb(){
        mongoClient = new MongoClient("localhost",27017);
        db=mongoClient.getDatabase(Consts.DBNAME);
    }

  
    
    /**
     * Call this function to load user data from u.data in db
     */
    public void loadUserDataToDb() throws NullPointerException{
        MongoCollection<Document> collection;
        collection = db.getCollection(Consts.UDATA);
        collection.dropCollection();
        db.createCollection(Consts.UDATA);
        
        
        FileInputStream fis=null;
        BufferedReader br;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream("datafiles//u.data");
            br = new BufferedReader(new InputStreamReader(fis));
            String inputLine;
            
            while((inputLine = br.readLine())!=null)
            {
                String [] fields = inputLine.split("\t");
               // System.out.println(fields[0]);
                Document d = new Document();
                d.put("userid", Integer.parseInt(fields[0]));
                d.put("itemid", Integer.parseInt(fields[1]));
                d.put("rating", Integer.parseInt(fields[2]));
                d.put("timestamp", fields[3]);
                collection.insertOne(d);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("u.data file not found"+ex.getMessage());
        } catch (IOException ex) {
            System.out.println("u.data IO exception"+ex.getMessage());
        }
    }
    
    /**
     * Call this function to load all movie data to the DB
     * 
     * Note: calling this deletes the collection if they already exist and then recreates them
     */
    
    
    public void loadMovieDataToDb(){
        
            String [] collectionsToDrop = {Consts.MOVIE_ACTORS_DATA,Consts.MOVIE_GENRE_DATA,Consts.MOVIE_NAME_DATA,Consts.MOVIE_OTHERFEATURES_DATA};
           // dropCollections(new HashSet<>(Arrays.asList(collectionsToDrop)));
            //get collection
            db.createCollection(Consts.MOVIE_NAME_DATA);
            MongoCollection<Document> movieNamesCollection = db.getCollection(Consts.MOVIE_NAME_DATA);
            db.createCollection(Consts.MOVIE_ACTORS_DATA);
            MongoCollection<Document> movieActorsCollection = db.getCollection(Consts.MOVIE_ACTORS_DATA);
            db.createCollection(Consts.MOVIE_OTHERFEATURES_DATA);
            MongoCollection<Document> movieOtherCollection = db.getCollection(Consts.MOVIE_OTHERFEATURES_DATA);
            db.createCollection(Consts.MOVIE_GENRE_DATA);
            MongoCollection<Document> movieGenreCollection = db.getCollection(Consts.MOVIE_GENRE_DATA);
            
            
            FileInputStream fis=null;
            BufferedReader br;
            BufferedInputStream bis = null;
            try {  
                fis = new FileInputStream("datafiles//movData.txt");
                br = new BufferedReader(new InputStreamReader(fis));
                String inputLine;
                int c=1;
                while(((inputLine = br.readLine())!=null) && c<5)
                {
                    String [] fields = inputLine.split("@");
                    for (String field : fields) {
                        System.out.print(field + "____");    
                    }
                    System.out.println("");
                 
                    addMovieNames(c,fields[1], movieNamesCollection);
                    addMovieActors(c, Arrays.asList(fields[4]), movieActorsCollection);
                    c++;
                    
                   
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LoadDataToDb.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {          
            Logger.getLogger(LoadDataToDb.class.getName()).log(Level.SEVERE, null, ex);
        }          
    }
    
    private void addMovieNames(int id, String movieName, MongoCollection<Document> movieNamesCollection){
        Document d = new Document();
        d.put("_id", id);
        d.put(Consts.MOVIE_NAME_FIELD,movieName );
        movieNamesCollection.insertOne(d);
    }
    
    private void addMovieActors(int id, List<String>actors, MongoCollection<Document> movieNamesCollection){
        Document d = new Document();
        d.put("_id", id);
        d.put(Consts.MOVIE_ACTORS_FIELD, actors);
        movieNamesCollection.insertOne(d);
    }
    
    private void addMovieOtherFeatures(int id, String director, MongoCollection<Document> movieOtherCollection){
        Document d = new Document();
        d.put("_id", id);
       
        movieOtherCollection.insertOne(d);
    }
    
    private void addMovieGenre(int id, String director, MongoCollection<Document> movieGenreCollection){
        Document d = new Document();
        d.put("_id", id);
       
        movieGenreCollection.insertOne(d);
    }
    
    public void dropCollections(Set<String> collectionsToDrop){
        MongoCollection<Document> collection;
        MongoIterable<String> collectionNames = db.listCollectionNames();
        
        String colName = collectionNames.first();
        while(colName!=null)
        {
            if(collectionsToDrop.contains(colName)){
                collection = db.getCollection(colName);
                collection.dropCollection();
            }
        }
    }
    
    
} 
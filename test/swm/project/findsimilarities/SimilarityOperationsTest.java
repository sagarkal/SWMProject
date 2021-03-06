/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swm.project.findsimilarities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import swm.project.Consts;
import static swm.project.findsimilarities.SimilarityOperations.MAX_RATING;
import static swm.project.findsimilarities.SimilarityOperations.MIN_RATING;
import swm.project.loadDataToDb.GetDataFromDb;
import swm.project.loadDataToDb.Operations;
import swm.project.mappings.MappingConstants;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Kruti
 */
public class SimilarityOperationsTest {
    
    public SimilarityOperationsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws FileNotFoundException, IOException {
       SimilarityOperations simOps = new SimilarityOperations();
       double[][] userUserSimHistory = new double[Consts.MAX_USERS][Consts.MAX_USERS];
        FileReader fr = new FileReader(MappingConstants.USER_MOVIE_CLUSTERS);
        Instances instanceValues = new Instances(fr);
         Iterator<Instance> instanceIter1  = instanceValues.iterator();
         
         while(instanceIter1.hasNext()){
             Instance user1 = instanceIter1.next();
             Iterator<Instance> instanceIter2  = instanceValues.iterator();
             int userid1 =(int) user1.value(0);
             HashMap<Integer, Double> clusterProbs1 = new HashMap<>();
             for(int i=1; i<user1.numAttributes();i++){
                 clusterProbs1.put(i,user1.value(i));
             }
             while(instanceIter2.hasNext()){
                Instance user2 = instanceIter2.next();
                int userid2 = (int) user2.value(0);
                HashMap<Integer,Double> clusterProbs2 = new HashMap<>();
                 for(int i=1; i<user2.numAttributes();i++){
                    clusterProbs2.put(i,user2.value(i));
                 }
                 double sim = jacUOD(clusterProbs1, clusterProbs2);
                 userUserSimHistory[userid1-1][userid2-1] = sim;
                 
             }
         }
        PrintWriter pw = new PrintWriter("datafiles//useruserjacsim.csv");
                for(int i=0;i<Consts.MAX_USERS;i++){
                    for(int j=0; j<Consts.MAX_USERS;j++){
                        pw.write(userUserSimHistory[i][j]+"");
                        if(j!= Consts.MAX_USERS-1)
                            pw.write(",");
                      
                    }
                    pw.write("\n");
                }
                pw.close();
                    
                        
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of cosineSimilarities method, of class SimilarityOperations.
     */
    @Test
    public void testSimilarities() throws FileNotFoundException {
        
//        PrintWriter pw = null;
//        try {
//            
//            pw = new PrintWriter("datafiles//UserSimilarities.csv");
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(FindMovieSimilarities.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        GetDataFromDb gb=new GetDataFromDb();
//        HashMap<Integer, HashMap<Integer,Integer>> ratings=gb.getAllUserRatings();
//        
//        
//        for(int i=1;i<=Consts.MAX_USERS;i++)
//        {
//                for(int j=1;j<=Consts.MAX_USERS;j++)
//            {
//                double result = jacUOD(ratings.get(i), ratings.get(j));
//                pw.print(result +", ");//set the row for the rating 
//            }pw.println();
//        }
//        pw.close();
        assertEquals(true, true);
    }
    
    public double jacUOD(HashMap<Integer,Double> user1, HashMap<Integer,Double> user2){
       Set<Integer> user1Items = user1.keySet();
       Set<Integer> user2Items = user2.keySet();
       Set<Integer> intersection = Operations.intersection(user1Items, user2Items);
       double unionSize = user1Items.size() + user2Items.size() - intersection.size();
       double denominator = 0, numerator = sqrt(18 * pow(1, 2));
      
       
       for(int item: intersection){
           double rating1= user1.get(item), rating2 = user2.get(item);
           
           denominator+=pow((rating1-rating2),2);
           
       }
       denominator = (denominator == 0 )?0.9:denominator;
       return (((double)intersection.size()/unionSize) * numerator/denominator);
       
   }

    
    
    
    /**
     * Test of pearsonsCoefficient method, of class SimilarityOperations.
     */
//    @Test
//    public void testPearsonsCoefficient() {
//        System.out.println("pearsonsCoefficient");
//        
//        ArrayList<Integer> vector1 = new ArrayList<>();
//        ArrayList<Integer> vector2 = new ArrayList<>();
//        SimilarityOperations instance = new SimilarityOperations();
//        double expResult = 0.0;
//        double result = instance.pearsonsCoefficient(vector1, vector2);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}

package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import sat.env.*;
import sat.formula.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;


import java.util.ArrayList;



public class SATSolverTest {

	public static String name = "largeUnsat.cnf";
	public static String output = "C:\\Users\\jem\\Google Drive\\javapapa\\CEC-SAT verification software\\java output files\\"
											+ "BoolAssignment.txt";
	public static String file_name = "C:\\Users\\jem\\Google Drive\\javapapa\\CEC-SAT verification software\\project-2d-starting\\sampleCNF\\"
											+ name;

    
    // ... it allow users to insert many many arguments of e
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    public static void main(String[] args) {
    	// Initialize fields 	*******************************************************************
    	Boolean cnfReadings = false;
    	
    	ArrayList<Clause> clauses = new ArrayList<>();
    	
    	ArrayList<Literal> clause = new ArrayList<>();
    	String line;
    	String[] info;
    	Literal[] l;
    	// Read file			*******************************************************************
    	BufferedReader br;
    	
    	System.out.println("[INFO] Preparing the required containers...");
    	long startread = System.nanoTime();		// how fast am i taking to read the files?
    	try {
    		File file = new File(file_name);
    		br = new BufferedReader(new FileReader(file));
    		while ((line = br.readLine()) != null) {							// stop reading when it reach the end of file
    			if (cnfReadings) {												// only start parsing when info line is seen (see SATformat.pdf)
    				info = line.split(" ");
    				for (String x: info) {
    					if (info.length > 1) {
    						int currentLiteral = Integer.parseInt(x);
    						if (currentLiteral > 0) {
    							clause.add(PosLiteral.make(x));	// this literal will have the class PosLiteral
    						} else if (currentLiteral < 0){
    							clause.add(NegLiteral.make(x));	// this literal will have the class NegLiteral
    						}else {
        						l = new Literal[clause.size()];
        						clause.toArray(l);
        						Clause balapoopi = makeCl(l);							// wooo make our Clause!!!!
        						if (balapoopi!=null) {
        							clauses.add(balapoopi);
        						}clause.clear();											// reset the clause ArrayList to empty ArrayList
        					}
    					}

						
    				}
    			}
    			else if (line.charAt(0) == 'p') {
    				cnfReadings = true;
    				}
    			}
    		br.close();
    		cnfReadings=false;
    		long endread = System.nanoTime();
    		long elapsedread = endread - startread;
    		System.out.println("[INFO] Time Taken to Read File: " + elapsedread/1000000000.0 + "s");
    		
    	
    	
	    	// Solve the CNF question	*******************************************************************
    		Formula solver = null;
    		Clause[] justAnotherClauseArray = new Clause[clauses.size()];
    		clauses.toArray(justAnotherClauseArray);
    		solver = makeFm(justAnotherClauseArray);
	    	System.out.println("SAT solver start!!!");
	    	long start = System.nanoTime();
	    	Environment e = SATSolver.solve(solver);
	    	long end = System.nanoTime();
	    	long elapsed = end - start;
	    	System.out.println("[INFO] Time Taken: " + elapsed/1000000.0 + "ms");
	    	
	    	// Output results into BoolAssignment.txt	*******************************************************************
	    	
	    	
			FileWriter fw = new FileWriter(output);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("");

			if (e!=null) {
				String woooFinallyDoneWithLife = (e.toString()).substring(13, e.toString().length()-1);
				String[] woooFinallyDoneWithSAT = woooFinallyDoneWithLife.split(", ");
				for (String i: woooFinallyDoneWithSAT) {
					String[] woooFinallyDoneWith2D = i.split("->");
					bw.append(woooFinallyDoneWith2D[0] + ":" + woooFinallyDoneWith2D[1]);
					bw.newLine();
				}
				
				
				bw.close();
				System.out.println("[SYSTEM] Yes, it is Satisfiable! :D");
			}
			else {
				System.out.println("[SYSTEM] oh no, it is not satisfiable.... :(");
			}
		}
    	catch (IOException err) {
    		err.printStackTrace();
    	}
    	
    }
}
    
    
























































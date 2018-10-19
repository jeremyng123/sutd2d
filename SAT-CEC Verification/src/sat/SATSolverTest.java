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


import java.util.*;



public class SATSolverTest {

	public static String name = "unsat1.cnf";
	public static String output = "C:\\Users\\labtech\\Desktop\\SAT-CEC Verification\\sampleCNF\\"
											+ "BoolAssignment.txt";
	public static String file_name = "C:\\Users\\labtech\\Desktop\\SAT-CEC Verification\\sampleCNF\\testfiles\\"
											+ name;

    
    // ... allow users to insert many many arguments of e
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    /*
     * Use if block over try-catch block because less overhead issues
     * https://stackoverflow.com/questions/8621762/java-if-vs-try-catch-overhead
     */
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
        	if (l == null) return null;
            c = c.add(l);
        }
        return c;
    }
    
    public static void main(String[] args) {
    	File file = new File(file_name);
    	BufferedReader br;
    	// Initialize fields 	*******************************************************************
    	Boolean cnfReadings = false;		// start reading the lines only after 'p' char is found
    	
    	List<Clause> clauses = new ArrayList<>();		// store all the clauses into this ArrayList
    	List<Literal> clause = new ArrayList<>();		// store the literals into this clause
    	/*
    	 * Adding the following 1 LinkedList and 2 ArrayList with the various data types as elements is important because 
    	 * adding elements into ArrayList has a runtime complexity of O(1)
    	 * and getting the element from the last element in the LinkedList has a runtime complexity of O(1)
    	 */
    	List<Variable> variables = new LinkedList<>();   //Store all the variables
    	List<PosLiteral> posLiterals = new ArrayList<>();  //Store all the positive literals
        List<NegLiteral> negLiterals = new ArrayList<>();  //Store all the negative literals
    	
    	String line;		// the line read by BufferedReader
    	String[] info;		// get the information of the variables in each clause
    	Literal[] l;		// store literals in every clause
    	// Read file			*******************************************************************
    	
    	System.out.println("[INFO] Preparing the Necessary Ingredients to Solve CNF!");
    	long startread = System.nanoTime();		// how fast am i taking to read the files?
    	try {
    		br = new BufferedReader(new FileReader(file));
    		while ((line = br.readLine()) != null) {							// stop reading when it reach the end of file
    			if (cnfReadings) {												// only start parsing when info line is seen (see SATformat.pdf)
    				info = line.split(" ");
    				for (String i: info) {
    					System.out.println(i);
    				}
    				if (line.length()>1) {
    					for (String x: info) {
    						if (x.length() >= 1) {
    							int currentLiteral = Integer.parseInt(x);
    							/*
    							 * Getting elements from ArrayList (from pos/negLiterals has a runtime of O(1))
    							 */
    							if (currentLiteral > 0) {
    								clause.add(posLiterals.get(Math.abs(currentLiteral)-1));	// this literal will have the class PosLiteral
    							} else if (currentLiteral < 0){
    								clause.add(negLiterals.get(Math.abs(currentLiteral)-1));	// this literal will have the class NegLiteral
    							}else {
    	    						l = new Literal[clause.size()];		// creating a Literal array so that we can use makeCl
    	    						clause.toArray(l);
    	    						Clause balapoopi = makeCl(l);							// wooo make our Clause!!!!
    	    						if (balapoopi != null) {
    	    							clauses.add(balapoopi);
    	    						}
    	    						clause.clear();											// reset the clause ArrayList to empty ArrayList
    	    					}
    						}
							
        				}
    				}
    				
    			}
    			else if (line.charAt(0) == 'p') {
    				cnfReadings = true;
    				String[] problemInfo = line.split(" ");
    				int varLen = Integer.parseInt(problemInfo[2]);  //Get Number of Variables
    				long listTimeStart = System.nanoTime();
                    for(int i = 1 ; i <= varLen ; i++){
                        variables.add(new Variable(Integer.toString(i)));       // Add all possible variables into variable list
                        posLiterals.add(PosLiteral.make(variables.get(i - 1))); // Add all possible variables as Positive Literal
                        negLiterals.add(NegLiteral.make(variables.get(i - 1))); // Add all possible variables as Negative Literal
                    }
                    long listTimeEnd = System.nanoTime();
                    long elapsedList = listTimeEnd - listTimeStart;
                    System.out.println("[INFO] Time Taken to Make Pos/Neg Literals in ArrayList: " + elapsedList/1000000000.0 + "s");
				}
			}
    		br.close();
    		cnfReadings=false;
    		long endread = System.nanoTime();
    		long elapsedread = endread - startread;
    		System.out.println("[INFO] Time Taken to Read File: " + elapsedread/1000000000.0 + "s");
    	
	    	// Solve the CNF question	*******************************************************************
    		Formula solver;
    		Clause[] justAnotherClauseArray = new Clause[clauses.size()];
    		clauses.toArray(justAnotherClauseArray);
    		solver = makeFm(justAnotherClauseArray);
	    	System.out.println("[SYSTEM] SAT solver start!!!");
	    	long start = System.nanoTime();
	    	Environment e = SATSolver.solve(solver);
	    	long end = System.nanoTime();
	    	long elapsed = end - start;
	    	System.out.println("[INFO] Time Taken: " + elapsed/1000000.0 + "ms");
	    	
	    	// Output results into BoolAssignment.txt	*******************************************************************
	    	
	    	File out = new File(output);
			FileWriter fw = new FileWriter(out);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("");									// clear the file

			if (e!=null) {
				String woooFinallyDoneWithLife = e.toString().substring(13, e.toString().length()-1);
				String[] woooFinallyDoneWithSAT = woooFinallyDoneWithLife.split(", ");
				for (String i: woooFinallyDoneWithSAT) {
					String[] woooFinallyDoneWith2D = i.split("->");
					bw.append(woooFinallyDoneWith2D[0] + ":" + woooFinallyDoneWith2D[1]);
					bw.newLine();
				}
				bw.close();
				System.out.println("[RESULT] Yes, it is Satisfiable! :D");
			}else {
				System.out.println("[RESULT] oh no, it is not satisfiable.... :(");
			}
		}
    	catch (IOException err) {
    		err.printStackTrace();
    	}
    }
}
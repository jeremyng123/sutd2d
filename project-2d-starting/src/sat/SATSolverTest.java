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
	/*
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();
    */
	public static String name = "largeSat.cnf";
	public static String output = "C:\\Users\\jem\\Google Drive\\javapapa\\CEC-SAT verification software\\project-2d-starting\\sampleCNF\\"
											+ "BoolAssignment.txt";
	public static String file_name = "C:\\Users\\jem\\Google Drive\\javapapa\\CEC-SAT verification software\\project-2d-starting\\sampleCNF\\"
											+ name;


//    public void testSATSolver1(){
//    	// (a v b)
//    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
///*
//    	assertTrue( "one of the literals should be set to true",
//    			Bool.TRUE == e.get(a.getVariable())  
//    			|| Bool.TRUE == e.get(b.getVariable())	);
//    	
//*/    	
//    }
//    
//    
//    public void testSATSolver2(){
//    	// (~a)
//    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
///*
//    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
//*/    	
//    }
    

    
    
    
    
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
    	Boolean startParse = false;
    	
    	ArrayList<Clause> clauses = new ArrayList<>();
    	ArrayList<Variable> variables = new ArrayList<>();
    	
    	ArrayList<Literal> clause = new ArrayList<>();
    	String line;
    	String[] info;
    	int totalVar;
    	Literal[] l;
    	
    	
    	// Read file			*******************************************************************
    	long startread = System.nanoTime();
    	if (args.length != 0){
    		name = args[0];
    	} BufferedReader br = null;
    	
    	System.out.println("[INFO] Preparing the required containers...");
    	try {
    		File file = new File(file_name);
    		br = new BufferedReader(new FileReader(file));
    		while ((line = br.readLine()) != null) {							// stop reading when it reach the end of file
    			if (startParse) {												// only start parsing when info line is seen (see SATformat.pdf)
    				info = line.split(" ");
    				for (String x: info) {
    					int currentLiteral = Integer.parseInt(x);
    					if (currentLiteral != 0) {
    						if (currentLiteral > 0) {
    							clause.add(PosLiteral.make(x));	// this literal will have the class PosLiteral
    						} else {
    							clause.add(NegLiteral.make(x));	// this literal will have the class NegLiteral
    						}
    					} else {
    						l = new Literal[clause.size()];
    						clause.toArray(l);
    						Clause balapoopi = makeCl(l);							// wooo make our Clause!!!!
    						if (balapoopi!=null) {
    							clauses.add(balapoopi);
    						}
    						clause.clear();											// reset the clause ArrayList to empty ArrayList
    						
    					}
    				}
    			}
    			else if (line.charAt(0) == 'p') {
    				startParse = true;
    				info = line.split(" ");									// split the line into a String[] array
    				totalVar = Integer.parseInt(info[2]);	

    				// get the total number of Variables in this CNF file
    				for (int i=1;i<=totalVar;i++) {
    					variables.add(new Variable(Integer.toString(i)));		// add all the possible variables as an element into ArrayList<Variable> variables
    				}
    				
    				
    			}
    		}
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			br.close();
    			long endread = System.nanoTime();
    			long elapsedread = endread - startread;
    			System.out.println("[INFO] Time Taken to Read Files: " + elapsedread/1000000000.0 + "ms");
    		} catch (IOException e){
    			e.printStackTrace();
    		}
    	}
    	
    	// Solve the CNF question	*******************************************************************
    	Clause[] justAnotherClauseArray = new Clause[clauses.size()];
    	clauses.toArray(justAnotherClauseArray);
    	Formula solver = makeFm(justAnotherClauseArray);
    	System.out.println("SAT solver start!!!");
    	long start = System.nanoTime();
    	Environment e = SATSolver.solve(solver);
    	long end = System.nanoTime();
    	long elapsed = end - start;
    	System.out.println("[INFO] Time Taken: " + elapsed/1000000.0 + "ms");
    	
    	// Output results into file	*******************************************************************
    	
    	try{
    		FileWriter fw = new FileWriter(output);
    		BufferedWriter bw = new BufferedWriter(fw);
    		if (e!=null) {
    			String woooFinallyDoneWithLife = e.toString();
    			bw.write(woooFinallyDoneWithLife);
    			bw.close();
    			System.out.println("Yes, it is Satisfiable! :D");
    			System.out.println(e);
    		}
    		else {
    			System.out.println("oh no, it is not satisfiable.... :(");
    		}
    	} catch (IOException err) {
    		err.printStackTrace();
    	}
    	
    }
    
    
    
}























































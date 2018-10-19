package sat;

import immutable.*;
import sat.env.*;
import sat.formula.*;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     *
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        ImList<Clause> clauses = formula.getClauses();
        Environment ans = new Environment();

        return solve(clauses, ans);
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        if (clauses.isEmpty()) {
        	return env;
        } // find the smallest clause size
        Clause smallest = clauses.first();
    	for(Clause i : clauses) {
    		if (i.isEmpty()) {
        		return null;
        	}
        	smallest = smallest.size() > i.size() ? i : smallest;
        	
    	}
        // initialize common data fields
        Literal l = smallest.chooseLiteral();
        Variable v = l.getVariable();
        ImList<Clause> reducedClause = substitute(clauses,l);
        
        if (smallest.isUnit()) {
        	env = l instanceof PosLiteral ? env.putTrue(v) : env.putFalse(v);
        	return solve(reducedClause, env);
        }else {
        	// set the literal to TRUE, substitute for it in all the clauses, then solve() recursively
        	env = l instanceof PosLiteral ? env.putTrue(v) : env.putFalse(v);
        	Environment tryTrue = solve(reducedClause,env.putTrue(v));
        	return tryTrue != null ? tryTrue : solve(substitute(clauses,l.getNegation()),env.putFalse(v));
        	// return trueLiteral if trueLiteral is not null, else return solve(redClauseNegLit)
        }

    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
    	ImList<Clause> clause = new EmptyImList<>();
    	for (Clause i: clauses) {
    		Clause ans = i.reduce(l);
    		clause = ans != null ? clause.add(ans) : clause;
    	}return clause;
    }

}

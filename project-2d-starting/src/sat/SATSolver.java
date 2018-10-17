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
        if (clauses.size() == 0) {
        	return env;
        } // find the smallest clause size
        Clause smallest = clauses.first();
        for(Clause i : clauses) {
        	smallest = smallest.size() > i.size() ? smallest : i;
        	if (i.size() == 0) {
        		return null;
        	}
        }
        if (smallest.isUnit()) {
        	Literal l = smallest.chooseLiteral();
        	env = l instanceof PosLiteral ? env.putTrue(l.getVariable()) : env.putFalse(l.getVariable());

        	return solve(substitute(clauses,l), env);
        }else {
        	Literal l = smallest.chooseLiteral();
        	// set the literal to TRUE, substitute for it in all the clauses, then solve() recursively
        	ImList<Clause> reducedLiteralsPos = substitute(clauses, l);
        	Environment trueLiteral = solve(reducedLiteralsPos, env.putTrue(l.getVariable()));
        	return trueLiteral != null ? trueLiteral : solve(substitute(clauses,l.getNegation()),env.putFalse(l.getVariable()));
        	// return trueLiteral if trueLiteral is not null, else return solve(redClauseNegLit
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
    	ImList<Clause> newClause = new EmptyImList<>();
    	for (Clause i: clauses) {
    		Clause ans = i.reduce(l);
    		newClause = ans != null ? newClause.add(ans) : newClause;
    	}return newClause;
    }

}

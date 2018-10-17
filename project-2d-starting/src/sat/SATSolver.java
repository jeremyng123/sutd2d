package sat;

import immutable.ImList;
import immutable.EmptyImList;
import immutable.NonEmptyImList;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;

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
        Environment env = new Environment();
        NonEmptyImList clauseList = (NonEmptyImList) formula.getClauses();
        return solve(clauseList, env);
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

        //If there are no clauses, the formula is trivially satisfiable.
        if (clauses.isEmpty()) {
                return env;
        }

        //If there is an empty clause, the clause list is unsatisfiable.
        for (Clause c: clauses){
            if (c.isEmpty()){
                return null;
            }
        }

        //Otherwise, find the smallest clause (by number of literals).
        Clause min = new Clause();
        for (Clause c: clauses){
            if (min.isEmpty()){ //min = c for first iteration
                min = c;
            }
            if (min.size() < c.size()){ //min = min(min, c) subsequently
                min = c;
            }
        } //smallest clause = min

        // If the clause has only one literal, bind its variable in the
        // environment so that the clause is satisfied, substitute
        // for the variable in all the other clauses (using the suggested
        // substitute() method), and recursively call solve()

        if (min.isUnit()){
            Literal l = min.chooseLiteral(); //choose the single literal l
            Environment env1 = env.putTrue(l.getVariable());
            ImList<Clause> clauses1 = substitute(clauses, l);
            //RECURSIVELY SOLVE
            return (solve(clauses1, env1));
        }

        //Otherwise, pick an arbitrary literal from this small clause:
        // First try setting the literal to TRUE, substitute for it in all the
        // clauses, then solve() recursively.
        // If that fails, then try setting the literal to FALSE, substitute,
        // and solve() recursively.

        else {
            Literal l = min.chooseLiteral(); //choose the literal l out of many
            Environment envT = env.putTrue(l.getVariable()); //env now has {1:T}
            ImList<Clause> clausesT = substitute(clauses, l);
            //RECURSIVELY SOLVE
            if (solve(clausesT, envT) == null) {
                Environment envF = env.putFalse(l.getVariable()); //env{1:T} is changed to {1:F}
                ImList<Clause> clausesF = substitute(clauses, l);
                return solve(clausesF, envF);
            } else{
                return solve(clausesT, envT);
            }
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
        /*  Attempt 1 (meimei)
        Formula newF = new Formula();
        for (Clause c: clauses){
            Clause newC = new Clause();
            if (c.contains(l)){
                Literal negl = l.getNegation();
                newC.add(negl);
            }else{
                newC.add(l);
            }
            newF.addClause(newC);
        }
        return (ImList<Clause>) newF;
        */

        //  Attempt 1 Correction:
        //  1. Setting literal to true is not the same as negation
        //  2. Use clause.reduce(literal) instead, returns new clause.
        //  3. Reduce will return unchanged clause if l not in clause. if (c.contains(l)) {...} is redundant.

        //  Mistake:
        //  ImList<Clause> newC = new ImList<Clause>();
        //  ERROR: ImList cannot be instantiated as it is abstract, use EmptyImList/NonEmptyImList instead.

        ImList<Clause> newClauses = new EmptyImList<Clause>();
        for (Clause c: clauses){
            Clause newC = c.reduce(l); 
            if (newC != null) {
                newClauses.add(newC);
            }
        }
        return newClauses;
    }
}

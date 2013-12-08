/*
 * TI2730-A Ontology Engineering & Linked Data
 * Assignment 2
 * Rolf Jagerman (4194616)
 */
package sui.model;

import java.util.LinkedList;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;

/**
 * Stores the result of a query into a tabular form using two linked lists
 * (columns and rows). The user interface can always interpret the data in this
 * class, even when different query constructs and results are used.
 * 
 * @author Rolf Jagerman
 */
public class QueryResult {
    
    /**
     * The columns of the result table
     */
    private LinkedList<String> columns;
    
    /**
     * The rows of the result table
     */
    private LinkedList<Value[]> rows;
    
    /**
     * Creates a new query result
     */
    protected QueryResult() {
        columns = new LinkedList<String>();
        rows = new LinkedList<Value[]>();
        
    }
    
    /**
     * Creates a new query result based on given tuple results
     * 
     * @param result The results
     * @throws QueryEvaluationException 
     */
    public QueryResult(TupleQueryResult result) throws QueryEvaluationException {
        this();
        setTupleResult(result);
    }
    
    /**
     * Creates a new query result based on given graph results
     * 
     * @param result The results
     * @throws QueryEvaluationException 
     */
    public QueryResult(GraphQueryResult result) throws QueryEvaluationException {
        this();
        setGraphResult(result);
    }
    
    /**
     * Creates a new query result based on given boolean results
     * 
     * @param result The results
     */
    public QueryResult(boolean result) {
        this();
        setBooleanResult(result);
    }

    /**
     * @return the columns
     */
    public LinkedList<String> getColumns() {
        return columns;
    }

    /**
     * @return the rows
     */
    public LinkedList<Value[]> getRows() {
        return rows;
    }
    
    /**
     * Sets the query results from a tuple query
     * 
     * @param result The results
     * @throws QueryEvaluationException 
     */
    protected final void setTupleResult(TupleQueryResult result) throws QueryEvaluationException {
        for(String name : result.getBindingNames()) {
            getColumns().add(name);
        }
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            int size = result.getBindingNames().size();
            Value[] values = new Value[size];
            for(int i=0; i<size; i++) {
                values[i] = bindingSet.getValue(result.getBindingNames().get(i));
            }
            getRows().add(values);
        }
    }
    
    /**
     * Sets the query results from a graph query
     * 
     * @param result The results
     * @throws QueryEvaluationException 
     */
    protected final void setGraphResult(GraphQueryResult result) throws QueryEvaluationException {
        getColumns().add("Subject");
        getColumns().add("Predicate");
        getColumns().add("Object");
        while (result.hasNext()) {
            Statement statement = result.next();
            Value[] values = new Value[3];
            values[0] = statement.getSubject();
            values[1] = statement.getPredicate();
            values[2] = statement.getObject();
            getRows().add(values);
        }
    }
    
    /**
     * Sets the query results from a boolean query
     * 
     * @param result The result
     */
    protected final void setBooleanResult(boolean result) {
        final boolean value = result;
        getColumns().add("Result");
        Value[] values = new Value[1];
        values[0] = new Value() {
            @Override
            public String stringValue() {
                return value ? "Yes" : "No";
            }
            @Override
            public String toString() {
                return stringValue();
            }
        };
        getRows().add(values);
    }
    
}

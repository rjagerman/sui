/*
 * TI2730-A Ontology Engineering & Linked Data
 * Assignment 2
 * Rolf Jagerman (4194616)
 */
package sui.model;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

/**
 * A thread that executes and processes a query on SESAME
 * 
 * @author Rolf Jagerman
 */
public class QueryThread extends SwingWorker {
    
    /**
     * The query
     */
    protected String query;
    
    /**
     * The results
     */
    private QueryResult results;
    
    /**
     * The change listener to notify when it is done
     */
    protected ChangeListener listener;
    
    /**
     * The error (if one has occurred)
     */
    private String error;
    
    /**
     * Creates a new query thread
     * 
     * @param query The query
     * @param listener The listener
     */
    public QueryThread(String query, ChangeListener listener) {
        this.query = query;
        this.listener = listener;
        error = "";
    }

    @Override
    protected Object doInBackground() throws Exception {
        try {
            results = SPARQL.executeQuery(query);
            return true;
        } catch (Exception ex) {
            setError(ex.getMessage());
            return false;
        }
    }
    
    @Override
    protected void done() {
        if(listener != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    listener.stateChanged(new ChangeEvent(this));
                }
            });
        }
    }
    
    /**
     * Attempts to stop the thread
     */
    public void stop() {
        this.listener = null;
        this.cancel(true);
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the results
     */
    public QueryResult getResults() {
        return results;
    }

}

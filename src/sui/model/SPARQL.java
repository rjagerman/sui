/*
 * TI2730-A Ontology Engineering & Linked Data
 * Assignment 2
 * Rolf Jagerman (4194616)
 */
package sui.model;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import org.openrdf.query.BooleanQuery;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.Query;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer;
import org.openrdf.sail.memory.MemoryStore;

/**
 * A wrapper for executing SPARQL queries using the SESAME library.
 * Contains the basic functionality needed to execute queries and return
 * results.
 * 
 * @author Rolf Jagerman
 */
public class SPARQL {
    
    /**
     * The RDF repository
     */
    protected static Repository repository;
    
    /**
     * The repository connection
     */
    protected static RepositoryConnection connection;
    
    /**
     * The BASE URI
     */
    protected static String baseURI;
    
    /**
     * The RDF files that have been added
     */
    private static LinkedList<File> files;
    
    /**
     * Initializes the RDF repository and connection
     * 
     * @throws RepositoryException
     * @throws IOException
     * @throws RDFParseException 
     */
    public static void initialize() throws RepositoryException {
        
        baseURI = "http://example.com/";
        files = new LinkedList<File>();
        repository = new SailRepository(new ForwardChainingRDFSInferencer(new MemoryStore()));
        repository.initialize();
        connection = repository.getConnection();
        
    }
    
    /**
     * Adds a file to the repository
     * 
     * @param file The file
     * @throws IOException
     * @throws RDFParseException
     * @throws RepositoryException 
     */
    public static void addFile(File file) throws IOException, RDFParseException, RepositoryException {
        connection.add(file, baseURI, RDFFormat.RDFXML);
        getFiles().add(file);
    }
    
    /**
     * Adds multiple files to the repository
     * 
     * @param files The files
     * @throws IOException
     * @throws RDFParseException
     * @throws RepositoryException 
     */
    public static void addFiles(File[] files) throws IOException, RDFParseException, RepositoryException {
        for(File file : files) {
            addFile(file);
        }
    }
    
    /**
     * Removes the entire repository
     * 
     * @throws RepositoryException 
     */
    public static void clearRepository() throws RepositoryException {
        initialize();
        getFiles().clear();
    }
    
    /**
     * Executes a query
     * 
     * @param query The query
     * @return The query results
     * @throws RepositoryException
     * @throws MalformedQueryException
     * @throws QueryEvaluationException 
     */
    public static QueryResult executeQuery(String query) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
        Query q = connection.prepareQuery(QueryLanguage.SPARQL, query);
        if(q instanceof TupleQuery) {
            return new QueryResult(((TupleQuery)q).evaluate());
        } else if(q instanceof GraphQuery) {
            return new QueryResult(((GraphQuery)q).evaluate());
        } else if(q instanceof BooleanQuery) {
            return new QueryResult(((BooleanQuery)q).evaluate());
        } else {
            throw new QueryEvaluationException("SUI Error: Unknown query type");
        }
    }

    /**
     * @return the files
     */
    public static LinkedList<File> getFiles() {
        return files;
    }
    
}

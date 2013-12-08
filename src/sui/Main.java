/*
 * TI2730-A Ontology Engineering & Linked Data
 * Assignment 2
 * Rolf Jagerman (4194616)
 */
package sui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.openrdf.repository.RepositoryException;
import sui.model.SPARQL;
import sui.views.MainView;

/**
 * This class acts as an entry point of the application.
 * 
 * @author Rolf Jagerman
 */
public class Main {
    
    /**
     * The loader that runs in the swing event handling thread to initiate the
     * GUI and run the application.
     */
    protected static class Loader implements Runnable {
        @Override
        public void run() {
            try {
                SPARQL.initialize();
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }
                MainView mainView = new MainView();
                mainView.setVisible(true);
            } catch(RepositoryException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Fatal Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Entry point of the application
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Loader loader = new Loader();
        SwingUtilities.invokeLater(loader);
    }
    
}

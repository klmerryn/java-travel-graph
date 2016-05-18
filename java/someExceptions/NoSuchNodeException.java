package someExceptions;

/**
 * Checked Exception to be thrown when invalid node
 * requests are made of the graph.
 * 
 * @author kael
 *
 */
public class NoSuchNodeException extends Exception {

	/**
	 * Serializes the Exception class. 
	 */
	private static final long serialVersionUID = 1L;

	//Constructor
	public NoSuchNodeException(){
	}

}

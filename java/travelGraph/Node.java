package travelGraph;
import java.io.Serializable;

/**
 * A node in a Graph<T>.
 * @param <T> the type of value in this Node. Each node has a unique value.
 */

public class Node<T> implements Serializable {
	
	/**
	 * Serializable ID for Node class.
	 */
	private static final long serialVersionUID = -6538786822649728616L;
	
	/** The ID of this Node. */
	private T value; 
	
	public Node(T value) {
        this.value = value;
	}
	
	/**
     * Returns the value of this Node.
     * @return the value of this Node
     */
    public T getValue() {
        return value;
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (value.getClass() != other.value.getClass()) {
			return false;
		}
		Node<T> otherNode = (Node<T>) obj;
		if (value == null) {
			if (otherNode.value != null)
				return false;
		} else if (!value.equals(otherNode.value))
			return false;
		return true;
	}

	/**
     * Sets the value of this Node to value.
     * @param the new value of this Node
     */
    public void setValue(T value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value.toString();
    }

}


package travelGraph;

import java.io.Serializable;

/**
 * An edge connecting two Node<T2>s in a Graph<T2, T1>.
 *
 * @param <T1> the type of value stored in this Edge, used to represent
 * the relationship between the two nodes it connects.
 * @param <T2> the type of value stored in the nodes that this edge connects
 */

public class Edge<T1, T2> implements Serializable
{
	private static final long serialVersionUID = 3827335968158240291L;

	/** The value stored in this Edge. */
	private T1 value;
	
	/** The Node this Edge points away from. */
	private Node<T2> pointingFrom;
	
	/** The Node this Edge points to. */
	private Node<T2> pointingTo;
	
	
	/**
	 * Creates a new Edge with the given value.
	 * @param value the value of the new Edge
	 */
	public Edge(T1 value) {
		this.value = value;
	}
	/**
	 * Creates a new Edge with the given value that points from Node 
	 * pointingFrom to Node pointingTo.
	 * @param value the value of the new Edge
	 * @param pointingFrom the Node the new Edge points away from
	 * @param pointingTo the Node the new Edge points to
	 */
	public Edge(T1 value, Node<T2> pointingFrom, Node<T2> pointingTo) {
		this.value = value;
		this.pointingFrom = pointingFrom;
		this.pointingTo = pointingTo;
	}
	
	/**
     * Returns the value of this Edge.
     * @return the value of this Edge
     */
    public T1 getValue() {
        return value;
    }

    /**
     * Sets the value of this Edge to value.
     * @param the new value of this Edge
     */
    public void setValue(T1 value) {
        this.value = value;
    }
    
    /**
     * Sets the Node to which this Edge points.
     * @param the Node to which this Edge points
     */
    public void setPointingTo(Node<T2> n) {
    	this.pointingTo = n;
    }
    
    /**
     * Sets the Node from which this Edge points.
     * @param n the Node from which this Edge points
     */
    public void setPointingFrom(Node<T2> n) {
    	this.pointingFrom = n;
    }
    
    /**
     * Returns the Node from which this Edge points.
     * @return the Node from which this Edge points
     */
    public Node<T2> getPointingFrom() {
		return pointingFrom;
	}
    
    /**
     * Returns the Node to which this Edge points.
     * @return the Node to which this Edge points
     */
	public Node<T2> getPointingTo() {
		return pointingTo;
	}
	@Override
    public String toString() {
        return "Points from " + this.pointingFrom + " to " + 
    this.pointingTo + " with value " + value.toString();
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pointingFrom == null) ? 0 : pointingFrom.hashCode());
		result = prime * result + ((pointingTo == null) ? 0 : pointingTo.hashCode());
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
		Edge other = (Edge) obj;
		if (pointingFrom == null) {
			if (other.pointingFrom != null)
				return false;
		} else if (!pointingFrom.equals(other.pointingFrom))
			return false;
		if (pointingTo == null) {
			if (other.pointingTo != null)
				return false;
		} else if (!pointingTo.equals(other.pointingTo))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}

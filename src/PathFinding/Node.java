package PathFinding;

public class Node
{

	/**
	 * How much it costs to move orthogonally from one node to another.
	 */
	protected static final int MOVEMENT_COST = 10;

	/**
	 * The node's X position on the map.
	 */
	private int x;

	/**
	 * The node's Y position on the map.
	 */
	private int y;

	/**
	 * If the node is not a wall and can be walked through.
	 */
	private boolean walkable;

	/**
	 * Used for when getting the resulting path. The node prior to this node.
	 */
	private Node parent;

	/**
	 * The cost of getting from the first node to this node.
	 */
	private int g;

	/**
	 * A heuristic that estimates the cost of the cheapest path from this node
	 * to the goal.
	 */
	private int h;

	public Node(int x, int y, boolean walkable){
		this.x = x;
		this.y = y;
		this.walkable = walkable;
	}

	public void setG(Node parent){
		g = (parent.getG() + MOVEMENT_COST);
	}

	public int calculateG(Node parent){
		return (parent.getG() + MOVEMENT_COST);
	}

	public void setH(Node goal){
		h = (Math.abs(getX() - goal.getX()) + Math.abs(getY() - goal.getY())) * MOVEMENT_COST;
	}

	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}

	public boolean isWalkable(){
		return walkable;
	}

	public void setWalkable(boolean walkable){
		this.walkable = walkable;
	}

	public Node getParent(){
		return parent;
	}

	public void setParent(Node parent){
		this.parent = parent;
	}

	public int getF(){
		return g + h;
	}

	public int getG(){
		return g;
	}

	public int getH(){
		return h;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (!(o instanceof Node))
			return false;
		if (o == this)
			return true;

		Node n = (Node) o;
		if (n.getX() == x && n.getY() == y && n.isWalkable() == walkable)
			return true;
		return false;
	}

}
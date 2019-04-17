package PathFinding;

import java.util.LinkedList;
import java.util.List;

public class PathMap {
	
	private int width;
	private int height;
	private Node[][] nodes;


	public PathMap(int[][] map)
	{
		this.width = map[0].length;
		this.height = map.length;
		nodes = new Node[height][width];

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				nodes[x][y] = new Node(x, y, ((map[x][y] == 0 || map[x][y] == 2 )|| map[x][y] > 50));
			}
		}
		/* On ferme les bordures de la map */
		for(int c = 0; c < width; c++) {
			nodes[0][c].setWalkable(false);
			nodes[height-1][c].setWalkable(false);
		}
		for(int l = 0; l < height; l++) {
			nodes[l][0].setWalkable(false);
			nodes[l][width-1].setWalkable(false);
		}
	}
	
	public void print() {
		for (int x = 0; x < width; x++)
		{
			StringBuilder sb = new StringBuilder();
			for (int y = 0; y < height; y++)
			{
				if(nodes[x][y].isWalkable()) {
					sb.append("  ");
				} else {
					sb.append(" #");
				}
			}
			System.out.println(sb.toString());
		}
	}
	
	public Node getNode(int x, int y)
	{
		if (x >= 0 && x < height && y >= 0 && y < width)
		{
			return nodes[x][y];
		}
		else
		{
			return null;
		}
	}
	
	public final List<Node> findPath(int startX, int startY, int goalX, int goalY)
	{
		// Check if start equals end
		if (startX == goalX && startY == goalY)
		{
			// We don't want to move
			return new LinkedList<Node>();
		}

		// The set of nodes already visited.
		List<Node> openList = new LinkedList<Node>();
		// The set of currently discovered nodes still to be visited.
		List<Node> closedList = new LinkedList<Node>();

		// Add starting node to open list.
		openList.add(nodes[startX][startY]);

		// This loop will be broken as soon as the current node position is
		// equal to the goal position.
		while (true)
		{
			// Gets node with the lowest F score from open list.
			Node current = lowestFInList(openList);
			// Remove current node from open list.
			openList.remove(current);
			// Add current node to closed list.
			closedList.add(current);

			// If the current node position is equal to the goal position ...
			if ((current.getX() == goalX) && (current.getY() == goalY))
			{
				// Return a LinkedList containing all of the visited nodes.
				return calcPath(nodes[startX][startY], current);
			}

			List<Node> adjacentNodes = getAdjacent(current, closedList);
			for (Node adjacent : adjacentNodes)
			{
				// If node is not in the open list ...
				if (!openList.contains(adjacent))
				{
					// Set current node as parent for this node.
					adjacent.setParent(current);
					// Set H costs of this node (estimated costs to goal).
					adjacent.setH(nodes[goalX][goalY]);
					// Set G costs of this node (costs from start to this node).
					adjacent.setG(current);
					// Add node to openList.
					openList.add(adjacent);
				}
				// Else if the node is in the open list and the G score from
				// current node is cheaper than previous costs ...
				else if (adjacent.getG() > adjacent.calculateG(current))
				{
					// Set current node as parent for this node.
					adjacent.setParent(current);
					// Set G costs of this node (costs from start to this node).
					adjacent.setG(current);
				}
			}

			// If no path exists ...
			if (openList.isEmpty())
			{
				// Return an empty list.
				return new LinkedList<Node>();
			}
			// But if it does, continue the loop.
		}
	}
	
	private List<Node> calcPath(Node start, Node goal)
	{
		LinkedList<Node> path = new LinkedList<Node>();

		Node node = goal;
		boolean done = false;
		while (!done)
		{
			path.addFirst(node);
			node = node.getParent();
			if (node.equals(start))
			{
				done = true;
			}
		}
		return path;
	}
	
	private Node lowestFInList(List<Node> list)
	{
		Node cheapest = list.get(0);
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).getF() < cheapest.getF())
			{
				cheapest = list.get(i);
			}
		}
		return cheapest;
	}
	
	private List<Node> getAdjacent(Node node, List<Node> closedList)
	{
		List<Node> adjacentNodes = new LinkedList<Node>();
		int x = node.getX();
		int y = node.getY();

		Node adjacent;

		// Check left node
		if (y > 0)
		{
			adjacent = getNode(x, y - 1);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check right node
		if (y < width)
		{
			adjacent = getNode(x, y + 1);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check top node
		if (x > 0)
		{
			adjacent = this.getNode(x - 1, y);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check bottom node
		if (x < height)
		{
			adjacent = this.getNode(x + 1, y);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}
		return adjacentNodes;
	}

}

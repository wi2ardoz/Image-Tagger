/*
 * Non-Generic Hash Table data structure supporting:
 * Insert O(1)
 * Search O(1) average, O(n) worst
 * Insert and Search are implemented with Chaining Method.
 */
import java.util.LinkedList;
public class HashTable {

	// fields
	private final LinkedList<ObjectWithCoordinates>[] table;

	// constructor
	public HashTable(int size){
		this.table = new LinkedList[size];
	}

	// public property
	/**
	 * Insert method: O(1) worst case.
	 * @param object object that has 2D integer coordinates x,y
	 */
	public void insert(ObjectWithCoordinates object){
		int index = getHashIndex(object);

		if(table[index]==null){
			// case first time
			table[index] = new LinkedList<>();
			table[index].add(object);
		}
		else
			// case index already exist. put it in the head of the list.
			table[index].add(0,object);
	}
	/**
	 * Search method: O(1) average case, O(n) worst case.
	 */
	public ObjectWithCoordinates search(int x, int y){

		int index = getHashIndex(x,y);

		if(table[index]!=null)
			// case index already exist. search inside the array list.
			for (ObjectWithCoordinates object:table[index]) {
				if(object.getX() == x && object.getY() == y)
					return object;
			}
		return null;
	}

	// private property
	/**
	 * The Hash Table index function:
	 * h(x) = (point.x + point.y) % m
	 * Where Point is the Coordinates and m is the size of the table.
	 * Coordinates are must be in integer types.
 	 */
	private int getHashIndex(ObjectWithCoordinates object){
		return (object.getX()+object.getY()) % table.length;
	}
	private int getHashIndex(int x,int y){
		return (x+y) % table.length;
	}
}


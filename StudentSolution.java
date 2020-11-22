/*
 * Implementing Facebook Tagger Solution.
 */
import java.util.LinkedList;
public class StudentSolution implements MyInterface{

	// fields
	private final AVL<Tag> avlTagsPivotX;
	private final AVL<Tag> avlTagsPivotY;

	// constructor
	public StudentSolution(){
		this.avlTagsPivotX = new AVL<>();
		this.avlTagsPivotY = new AVL<>();
	}

	// public property
	/**
	 * Creates new Tag object and insert it coordinates to the suitable AVL.
	 * @param objectName String Tag name
	 * @param objectX x coordinate of the Tag
	 * @param objectY y coordinate of the Tag
	 */
	@Override
	public void insertDataFromDBFile(String objectName, int objectX, int objectY) {

		// init new Tag object
		Tag tag = new Tag(objectName,objectX,objectY);

		// insert to the AVL tree by key x or y
		avlTagsPivotX.insert(objectX,tag);
		avlTagsPivotY.insert(objectY,tag);
	}
	/**
	 * Determine what Tags are inside the drawn rectangle and return String array of them.
	 * Implemented with Hash Table search.
	 * Time O(m1+m2+lg(n)) average, O(m1*m2+lg(n)) worst,
	 * wheres m1,m2 are the keys in the range of avlTagsPivotX,avlTagsPivotY accordingly.
	 * @param leftTopX x coordinate of the left top point of the drawn rectangle
	 * @param leftTopY y coordinate of the left top point of the drawn rectangle
	 * @param rightBottomX x coordinate of the right bottom point of the drawn rectangle
	 * @param rightBottomY y coordinate of the right bottom point of the drawn rectangle
	 * @return the tags that are inside the rectangle
	 */
	@Override
	public String[] firstSolution(int leftTopX, int leftTopY, int rightBottomX, int rightBottomY) {

		LinkedList<Tag> tags = new LinkedList<>();	// result - array list of tags coordinates found in the 2D rectangle.
		Tag tag;	// temp tag
		LinkedList<Tag> listX;	// array list stores tags in some range of AVL pivot x
		LinkedList<Tag> listY;	// array list stores tags in some range of AVL pivot y
		HashTable intersectionTable; // using hash table to find intersection between coordinates in two arrays.
		int m1;	// length of listX
		int m2;	// length of listY

		// find m1 points - 1D range pivot x
		// time log(m1 + lg(n))
		listX = avlTagsPivotX.range(leftTopX,rightBottomX);
		m1 = listX.size();

		// find m2 points - 1D range pivot y
		// time log(m2 + lg(n))
		listY = avlTagsPivotY.range(leftTopY,rightBottomY);
		m2 = listY.size();

		// find m1 & m2 intersection - 2D coordinates
		// space O(m1) or O(m2), time O(m1) or O(m2) average, O(m1*m2) worst
		if(m1 > m2){
			// init hash table
			intersectionTable = new HashTable(m1);
			// insert to hash table
			for(ObjectWithCoordinates tempTag: listX)
				intersectionTable.insert(tempTag);
			// loop through the smaller list and preform search on the hash
			for(int i=0; i<m2;i++){
				tag = hashSearch(intersectionTable, listY.get(i));
				if(tag != null) tags.add(tag);
			}
		}
		else{
			// init hash table
			intersectionTable = new HashTable(m2);
			// insert to hash table
			for(ObjectWithCoordinates tempTag: listY)
				intersectionTable.insert(tempTag);
			// loop through the smaller list and preform search on the hash
			for(int i=0; i<m1;i++){
				tag = hashSearch(intersectionTable, listX.get(i));
				if(tag != null) tags.add(tag);
			}
		}

		// convert tags to string array
		if(tags.size() != 0)
			return tagsToStringArray(tags);
		return new String[0];
	}
	/**
	 * Determine what Tags are inside the drawn rectangle and return String array of them.
	 * Implemented with Binary Search.
	 * Time O(min(m1,m2)*lg(max(m1,m2))+m1+m2+lg(n)) average & worst,
	 * wheres m1,m2 are the keys in the range of avlTagsPivotX,avlTagsPivotY accordingly.
	 * @param leftTopX x coordinate of the left top point of the drawn rectangle
	 * @param leftTopY y coordinate of the left top point of the drawn rectangle
	 * @param rightBottomX x coordinate of the right bottom point of the drawn rectangle
	 * @param rightBottomY y coordinate of the right bottom point of the drawn rectangle
	 * @return the tags that are inside the rectangle
	 */
	@Override
	public String[] secondSolution(int leftTopX, int leftTopY, int rightBottomX, int rightBottomY) {

		LinkedList<Tag> tags = new LinkedList<>();	// result - array list of tags coordinates found in the 2D rectangle.
		Tag tag;	// temp tag
		LinkedList<Tag> listX;	// array list stores tags in some range of AVL pivot x
		LinkedList<Tag> listY;	// array list stores tags in some range of AVL pivot y
		int m1;	// length of listX
		int m2;	// length of listY

		// find m1 points - 1D range pivot x
		// time log(m1 + lg(n))
		listX = avlTagsPivotX.range(leftTopX,rightBottomX);
		m1 = listX.size();

		// find m2 points - 1D range pivot y
		// time log(m2 + lg(n))
		listY = avlTagsPivotY.range(leftTopY,rightBottomY);
		m2 = listY.size();

		// find m1 & m2 intersection - 2D coordinates
		// space O(m1) or O(m2), time O(min(m1,m2)*lg(max(m1,m2)))
		if(m1 > m2){
			// loop through the smaller list and preform binary search on the bigger list
			for(int i=0; i<m2;i++){
				tag = binarySearch(listX, listY.get(i),true);
				if(tag != null) tags.add(tag);
			}
		}
		else{
			// loop through the smaller list and preform binary search on the bigger list
			for(int i=0; i<m1;i++){
				tag = binarySearch(listY, listX.get(i),false);
				if(tag != null) tags.add(tag);
			}
		}

		// convert tags to string array
		if(tags.size() != 0)
			return tagsToStringArray(tags);
		return new String[0];
	}

	// private property
	/**
	 * Preform Hash Table search.
	 * @return the desired tag.
	 */
	private Tag hashSearch(HashTable table, Tag tag){
		return (Tag) table.search(tag.getX(),tag.getY());
	}
	/**
	 * Preform Binary search on list
	 * @param isXSearched boolean value to distinguish between coordinate x or y in order to know which property to call.
	 * @return the desired tag
	 */
	private Tag binarySearch(LinkedList<Tag> list,Tag tag,boolean isXSearched) {

		int start = 0;
		int middle;
		int end = list.size() - 1;

		if (isXSearched) {
			while (start <= end) {
				middle = start + (end - start) / 2;
				// check if x is present at mid
				if (list.get(middle).getX() == tag.getX())
					return list.get(middle);
				// if x greater, ignore left half
				if (list.get(middle).getX() < tag.getX())
					start = middle + 1;
				// if x is smaller, ignore right half
				else
					end = middle - 1;
			}
		}
		else {
			while (start <= end) {
				middle = start + (end - start) / 2;
				// check if x is present at mid
				if (list.get(middle).getY() == tag.getY())
					return list.get(middle);
				// if x greater, ignore left half
				if (list.get(middle).getY() < tag.getY())
					start = middle + 1;
				// if x is smaller, ignore right half
				else
					end = middle - 1;
			}
		}
		// object isn't found
		return null;
	}
	/**
	 * Convert tags array to string array with pattern:
	 * NAME X='' Y=''
	 * @return array in pattern {NAME X='' Y='' \n, NAME X='' Y='' \n ....}
	 */
	private String[] tagsToStringArray(LinkedList<Tag> tags){

		String[] strArray = new String[tags.size()];
		StringBuilder[] strBuilder = new StringBuilder[tags.size()];
		int i = 0;

		for (Tag tag: tags) {
			strBuilder[i] = new StringBuilder();
			strBuilder[i].append(tag.getData()).append(" X=").append(tag.getX()).append(" Y=").append(tag.getY()).append("\n");
			strArray[i] = strBuilder[i].toString();
			i++;
		}
		return strArray;
	}
}
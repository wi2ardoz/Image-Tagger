/*
 * Generic AVL Tree data structure supporting:
 * Insert O(Lg(n))
 * Search O(Lg(n))
 * Range O(Lg(n) + k), wheres k is the num of keys in the range
 */
import java.util.LinkedList;
public class AVL<T> {

	// fields
	private AVLNode<T> root;

	// constructor
	public AVL(){
		this.root = null;
	}

	// public property
	/**
	 * Insert new node to AVL.
	 * Time O(Lg(n))
	 * @param key key of the node
	 * @param data data of the node
	 */
	public void insert(int key, T data){
		root = insert(root,key,data);
		root.setFather(null);
	}
	/**
	 * Search in AVL (same as BST).
	 * Time O(lg(n))
	 * @param key key of the desired node
	 * @return the data of the desired node, if didn't found return null
	 */
	public T search(int key){
		return search(root,key) != null ? search(root,key).getData() : null;
	}
	/**
	 * Create list of object in the range [key1,key2] inside the AVL.
	 * Time O(lg(n) + k), wheres k is the keys in the range.
	 * @param key1 int key1
	 * @param key2 iny key2
	 * @return list of the objects in the range
	 */
	public LinkedList<T> range(int key1, int key2){
		if(key2-key1>=0){
			LinkedList<T> tags = new LinkedList<>();
			range(root,tags,key1,key2);
			return tags;
		}
		return null;
	}

	// private property
	/**
	 * Insert new node to AVL.
	 * @param node	recursive search in the avl with node
	 * @param key	key of the node
	 * @param data	data of the node
	 * @return updated root
	 */
	private AVLNode<T> insert(AVLNode<T> node, int key,T data) {
		int balance; // node heights difference factor

		// insert same as BST
		if (node == null)
			return new AVLNode<>(key, data);
		if (key < node.getKey())
			node.setLeftChild(insert(node.getLeftChild(), key, data));
		else if (key > node.getKey())
			node.setRightChild(insert(node.getRightChild(), key, data));
		else
			throw new RuntimeException("Same Value");

		// update height (leaf defined as height '1')
		node.setHeight(1 + Math.max(height(node.getLeftChild()), height(node.getRightChild())));

		// get the balance for later checking avl requirements
		balance = height(node.getLeftChild()) - height(node.getRightChild());

		// -1 <= balanced <= 1 		=> 		skip rotation (node is balanced),
		// balance > 1 				=> 		rotate to right (node is unbalanced),
		// balance < -1 			=> 		rotate to left (node is unbalanced)

		// case LL
		if (balance > 1 && key < node.getLeftChild().getKey())
			return rightRotate(node);

		// case RR
		if (balance < -1 && key > node.getRightChild().getKey())
			return leftRotate(node);

		// case LR
		if (balance > 1 && key > node.getLeftChild().getKey()) {
			node.setLeftChild(leftRotate(node.getLeftChild()));
			return rightRotate(node);
		}

		// case RL
		if (balance < -1 && key < node.getRightChild().getKey()) {
			node.setRightChild(rightRotate(node.getRightChild()));
			return leftRotate(node);
		}

		// case node is unchanged
		return node;
	}
	/**
	 * Add object inorder to the array of objects.
	 * Time O(Log(n))
	 * @param node	node in the avl tree.
	 * @param list array list of objects.
	 */
	private void insertDataInorder(AVLNode<T> node,LinkedList<T> list) {
		if (node != null) {
			insertDataInorder(node.getLeftChild(),list);
			list.add(node.getData());
			insertDataInorder(node.getRightChild(),list);
		}
	}
	/**
	 * In case search didn't find our key use this method
	 * to find the closest object.
	 * Time O(Log(n))
	 * @param node node for navigating in the avl tree.
	 * @param closestNode the temp that stores the closest node will be the result.
	 * @param key  of the wanted object.
	 * @param isDirectionDown [a,b] is our range. false => searching for a will find a+, true => searching for b will find b-.
	 *
	 */
	private AVLNode<T> searchTheClosestNode(AVLNode<T> node,AVLNode<T> closestNode,int key,boolean isDirectionDown){
		// ending condition
		if(node == null)
			return closestNode;
		if(node.getKey() == key)
			return node;

		// catch the closest
		// find a+
		if(!isDirectionDown){
			if(closestNode.getKey() == -1 && node.getKey() >= key)
				closestNode = node;
			if(node.getKey() > key && closestNode.getKey() > node.getKey())
				closestNode = node;
		}
		// find b-
		if(isDirectionDown){
			if(closestNode.getKey() == -1 && node.getKey() <= key)
				closestNode = node;
			if(node.getKey() < key && closestNode.getKey() < node.getKey())
				closestNode = node;
		}

		// continue navigate BST search
		if(node.getKey() < key)
			return searchTheClosestNode(node.getRightChild(),closestNode,key,isDirectionDown);
		else
			return searchTheClosestNode(node.getLeftChild(),closestNode,key,isDirectionDown);
	}
	/**
	 * Finds the node that is the common parent of the two sons.
	 * Time O(k) , wheres k is the keys in the range of [a,b].
	 * @param son1	first son.
	 * @param son2	second son.
	 */
	private AVLNode<T> searchCommonParent(AVLNode<T> son1,AVLNode<T> son2) {

		// validation
		if(son1 == null || son2 == null) return null;

		// get common parent
		// move both son1 & son2 up to their parents only when they both have the same heights
		while(son1.getKey() != -1 && son2.getKey() != -1){
			if(son1.getKey() == son2.getKey())
				return son1;  // they are the same, return one of them.

			if(son1.getHeight() == son2.getHeight()){
				son1 = son1.getFather();
				son2 = son2.getFather();
			}
			else if(son1.getHeight() > son2.getHeight())
				son2 = son2.getFather();
			else
				son1 = son1.getFather();
		}
		return null;
	}
	/**
	 * Right rotate subtree rooted with leftChild.
	 */
	private AVLNode<T> rightRotate(AVLNode<T> pivot) {
		AVLNode<T> leftChild = pivot.getLeftChild();
		AVLNode<T> rightChildOfLeftChild = leftChild.getRightChild();

		// do rotation (father updated within)
		leftChild.setRightChild(pivot);
		pivot.setLeftChild(rightChildOfLeftChild);

		// update heights
		pivot.setHeight(Math.max(height(pivot.getLeftChild()), height(pivot.getRightChild())) + 1);
		leftChild.setHeight(Math.max(height(leftChild.getLeftChild()), height(leftChild.getRightChild())) + 1);

		// return new root
		return leftChild;
	}
	/**
	 * Left rotate subtree rooted with pivot
 	 */
	private AVLNode<T> leftRotate(AVLNode<T> pivot) {
		AVLNode<T> rightChild = pivot.getRightChild();
		AVLNode<T> leftChildOfRightChild = rightChild.getLeftChild();

		// do rotation (father updated within)
		rightChild.setLeftChild(pivot);
		pivot.setRightChild(leftChildOfRightChild);

		// update heights
		pivot.setHeight(Math.max(height(pivot.getLeftChild()),height(pivot.getRightChild())) + 1);
		rightChild.setHeight(Math.max(height(rightChild.getLeftChild()), height(rightChild.getRightChild())) + 1);

		// return new root
		return rightChild;
	}
	/**
	 * Add the keys of the nodes to stringBuilder in order to print it later.
	 */
	private void inOrder(AVLNode<T> node,StringBuilder str){
		if (node == null)
			return;
		inOrder(node.getLeftChild(),str);
		str.append("(").append(node.getKey()).append(") ");
		inOrder(node.getRightChild(),str);
	}
	/**
	 * Checks node in order to return its height.
	 * node == null its height is 0.
	 */
	private int height(AVLNode<T> node){
		if(node!=null)
			return node.getHeight();
		return 0;
	}
	/**
	 * Search in AVL (same as BST).
	 * @param node node in the AVL
	 * @param key	key of the node
	 * @return the desired node, if didn't found return null
	 */
	private AVLNode<T> search(AVLNode<T> node, int key){
		if(node == null || node.getKey() == key)
			return node;
		if(node.getKey() < key)
			return search(node.getRightChild(),key);
		else
			return search(node.getLeftChild(),key);
	}
	/**
	 * Add objects in the AVL range [key1,key2] to the array.
	 * @param node node in the AVL
	 * @param list array list that hold all the objects in the range [key1,key2]
	 * @param key1 key1
	 * @param key2 key2
	 */
	private void range(AVLNode<T> node,LinkedList<T> list,int key1,int key2){

		AVLNode<T> a = new AVLNode<>(); // key1 node
		AVLNode<T> b = new AVLNode<>();	// key2 node
		AVLNode<T> x;	// common parent node
		AVLNode<T> temp; // navigate through avl

		// find closest to key1 & key2
		a = searchTheClosestNode(node,a,key1,false);
		b = searchTheClosestNode(node,b,key2,true);

		// case out of range or doesn't exist
		if(a.getKey() == -1 || b.getKey() == -1 || a.getKey() > b.getKey()) return;
		// case a & b same node
		if(a==b){ list.add(a.getData()); return; }

		// find common parent
		x = searchCommonParent(a,b);

		// case a is the node of the common parent
		if(x == a) {
			// add x (common father)
			list.add(x.getData());

			// iterate through x (not included) to b
			temp = x.getRightChild();
			while (temp != null){
				if(temp.getKey()<=key2){
					insertDataInorder(temp.getLeftChild(),list);
					list.add(temp.getData());
				}
				// navigate temp like BST
				if(temp == b)
					break;
				if(temp.getKey() < key2)
					temp = temp.getRightChild();
				else
					temp = temp.getLeftChild();
			}
			return;
		}
		// case b is the node of the common parent
		if(x == b) {
			// iterate through a to x (not included)
			temp = a;
			do {
				if(temp.getKey()>=key1){
					list.add(temp.getData());
					insertDataInorder(temp.getRightChild(),list);
				}
				// navigate temp
				temp = temp.getFather();
			}while(temp!=x);

			// add x
			list.add(x.getData());

			return;
		}
		// case non of the above - x is the node where a and b split
		// iterate through a to x (not included)
		temp = a;
		do {
			if(temp.getKey()>=key1){
				list.add(temp.getData());
				insertDataInorder(temp.getRightChild(),list);
			}
			// navigate temp
			temp = temp.getFather();
		}while(temp!=x);

		// add x
		list.add(x.getData());

		// iterate through x (not included) to b
		temp = x.getRightChild();
		while (temp != null){
			if(temp.getKey()<=key2){
				insertDataInorder(temp.getLeftChild(),list);
				list.add(temp.getData());
			}
			// navigate temp like BST
			if(temp == b)
				break;
			if(temp.getKey() < key2)
				temp = temp.getRightChild();
			else
				temp = temp.getLeftChild();
		}
	}

	// description
	public String toString(){
		StringBuilder str = new StringBuilder("{");
		inOrder(root, str);
		str.append("}");
		return str.toString();
	}
}
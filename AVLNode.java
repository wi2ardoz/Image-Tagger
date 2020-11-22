/*
 * Generic Node for AVL Tree.
 */
public class AVLNode<T> {

	// fields
	private AVLNode<T> father,leftChild,rightChild;
	private final int key;
	private int height;
	private T data;

	// constructor
	public AVLNode(){
		this.father = null;
		this.leftChild = null;
		this.rightChild = null;
		this.key = -1;
		this.height = 1;
		this.data = null;
	}
	public AVLNode(int key,T data){
		this.father = null;
		this.leftChild = null;
		this.rightChild = null;
		this.key = key;
		this.height = 1;
		this.data = data;
	}

	// getter & setter
	public AVLNode<T> getLeftChild(){
		return leftChild;
	}
	public void setLeftChild(AVLNode<T> leftChild) {
		if(leftChild != null)
			leftChild.setFather(this);
		this.leftChild = leftChild;
	}
	public AVLNode<T> getRightChild(){
		return rightChild;
	}
	public void setRightChild(AVLNode<T> rightChild) {
		if(rightChild != null)
			rightChild.setFather(this);
		this.rightChild = rightChild;
	}
	public AVLNode<T> getFather(){
		return father;
	}
	public void setFather(AVLNode<T> father) {
		this.father = father;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getKey(){
		return key;
	}
	public T getData(){
		if(data != null)
			return data;
		return null;
	}
	public void setData(T data) {
		this.data = data;
	}

	// description
	@Override
	public String toString() {
		
		String s = "";
		
		if (getLeftChild() != null){
			s+="(";
			s+=getLeftChild().toString();
			s+=")";
		}
		s+=getKey();
		
		if (getRightChild() != null){
			s+="(";
			s+=getRightChild().toString();
			s+=")";
		}
		
		return s;
	}
}


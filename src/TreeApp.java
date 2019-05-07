/*  Authors: Michael Ressler and Levi Hill 
* Date: 19 Feb 2018
* Overview: Outlab 2 - Runs input.txt from the file input, and runs the insert method 
* with 3 parameters wrapped by the old insert method. utilizes new insert and delete 
* methods that are recursive and uses one of the rotations when tree is not balanced.
* It uses getBalance to check balance. Rotate left or right methods utilizes a method
* that obtains height and saves it to the int height.  
* Attribute of used source to:
* (Robert Lafore. 2002.Data Structures and Algorithms in Java(2 ed.). Sams, Indianapolis, IN, USA)
*/
	import java.io.BufferedReader;
	import java.io.IOException;
	import java.nio.charset.Charset;
	import java.nio.file.FileSystems;
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.util.Stack;

	//////////////////////////////////////////////////////////////
	class Node {
		public int iData;           // data item (key)
		public double dData;        // data item
		public Node leftChild;      // this Node's left child
		public Node rightChild;     // this Node's right child
		public int height;
		
		public Node(int a, double b) {
			iData = a; 
			dData = b;
			leftChild = null;
			rightChild = null; 
			height = 1; 
		}
		

		public void displayNode() { // display ourself
			System.out.print('{');
			System.out.print(iData);
			System.out.print(", ");
			System.out.print(dData);
			System.out.print("} ");		
		}
	} // end Class Node
	////////////////////////////////////////////////////////////////

	class Tree {
		private Node root;                 // first Node of Tree

		public Tree() {                    // constructor
			root = null;                   // no nodes in tree yet
		}

		int height(Node a) {
			if (a == null) {
				return 0;
			}
			return a.height;
		}
		
		int max(int a, int b) {
	        if(a > b) {
	        	return a;
	        }
	        return b; 
	    }
		
		Node rightRotate(Node b) {
			 Node a = b.leftChild;
		     Node r = a.rightChild;
		 
		        a.rightChild = b;
		        b.leftChild = r;
		 
		        b.height = max(height(b.leftChild), height(b.rightChild)) + 1;
		        a.height = max(height(a.leftChild), height(a.rightChild)) + 1;
		 
		        return a;
		}
		
		
		Node leftRotate(Node a) {
	        Node b = a.rightChild;
	        Node r = b.leftChild;
	 
	        b.leftChild = a;
	        a.rightChild = r;
	 
	        a.height = max(height(a.leftChild), height(a.rightChild)) + 1;
	        b.height = max(height(b.leftChild), height(b.rightChild)) + 1;
	       
	        return b;
	    }
		
		int getBalance(Node a) {
	        if (a == null) {
	            return 0;
	        }
	        return height(a.leftChild) - height(a.rightChild);
	    }

		public Node find(int key) {      // find node with given key
			Node current = root;         // (assumes non-empty tree)
			while (current.iData != key) {          // while no match
				if (key < current.iData) {          // go left?
					current =  current.leftChild; 
				}
				else {                              // or go right?
					current =  current.rightChild;
				}
				if(current == null)                 // if no child
				{                                   // didn't find it
					return null;              
				}			
			}
			return current;                         // found it
		}  //end find()


		public void insert(int id, double dd) {
		
			root = insert(root, id, dd); 
			
		} // end insert()

		
		public Node insert(Node current, int id, double dd) {
			if(current == null)              
			{ 
				current = new Node(id, dd);
				return current;
			}
			else if(id < current.iData) {
				current.leftChild = insert(current.leftChild, id, dd); 
			}
			else if(id >= current.iData)
			{
				current.rightChild = insert(current.rightChild, id, dd); 
			}
			else
			return current;
			
			 current.height = 1 + max(height(current.leftChild),
                     height(current.rightChild));
			
			 
			 int balance = getBalance(current);
			 
			 if (balance > 1 && id < current.leftChild.iData)
		            return rightRotate(current);
		        // Right Right
		        if (balance < -1 && id > current.rightChild.iData)
		            return leftRotate(current);
		        // Left Right
		        if (balance > 1 && id > current.leftChild.iData) {
		            current.leftChild = leftRotate(current.leftChild);
		            return rightRotate(current);
		        }
		        // Right Left
		        if (balance < -1 && id < current.rightChild.iData) {
		            current.rightChild = rightRotate(current.rightChild);
		            return leftRotate(current);
		        }
		        return current;
		}
		
		Node min(Node node)
	    {
	        Node current = node;
	  
	        while (current.leftChild != null) {
	           current = current.leftChild;
	        }
	        return current;
	    }
		
		Node delete(Node root, int id)
	    {
	        if (root == null) {
	            return root;
	        }
	 
	        if (id < root.iData) {
	            root.leftChild = delete(root.leftChild, id);
	        }
	        else if (id > root.iData) {
	            root.rightChild = delete(root.rightChild, id);
	        }
	        else
	        {
	            if ((root.leftChild == null) || (root.rightChild == null))
	            {
	                Node current = null;
	                if (current == root.leftChild) {
	                    current = root.rightChild;
	                }
	                else
	                {
	                    current = root.leftChild;
	                }
	                if (current == null)
	                {
	                    current = root;
	                    root = null;
	                }
	                else   
	                {
	                    root = current; 
	                }
	            }
	            else
	            {
	                Node current = min(root.rightChild);
	                root.iData = current.iData;
	                root.rightChild = delete(root.rightChild, current.iData);
	            }
	        }
	        if (root == null) {
	            return root;
	        }
	        root.height = max(height(root.leftChild), height(root.rightChild)) + 1;

	        int balance = getBalance(root);
	        // Left Left
	        if (balance > 1 && getBalance(root.leftChild) >= 0) {
	            return rightRotate(root);
	        }
	        // Left Right
	        if (balance > 1 && getBalance(root.leftChild) < 0)
	        {
	            root.leftChild = leftRotate(root.leftChild);
	            return rightRotate(root);
	        }
	        // Right Right
	        if (balance < -1 && getBalance(root.rightChild) <= 0)
	            return leftRotate(root);
	        // Right Left
	        if (balance < -1 && getBalance(root.rightChild) > 0)
	        {
	            root.rightChild = rightRotate(root.rightChild);
	            return leftRotate(root);
	        }
	 
	        return root;
	    }

		public Boolean delete(int key) {            
			Node current = root;
			current = delete(current, key);
			return true; 
		}


		//returns node with next-highest value after delNode
		//goes right child, then right child's left descendants
		private Node getSuccessor(Node delNode) {
			Node successorParent = delNode;
			Node successor = delNode;
			Node current = delNode.rightChild;        // go to the right child
			while (current != null) {                 // until no more
				successorParent = successor;          // left children
				successor = current;
				current = current.leftChild;
			}

			if (successor != delNode.rightChild) {    // if successor not right child,
				//make connections
				successorParent.leftChild = successor.rightChild;
				successor.rightChild = delNode.rightChild;
			}
			return successor;
		}


		public void traverse(int traverseType) {
			switch (traverseType) {
			case 1:
				System.out.print("\nPreorder traversal: ");
				preOrder(root);
				break;
			case 2:
				System.out.print("\nInorder traversal: ");
				inOrder(root);
				break;
			case 3:
				System.out.print("\nPostorder traversal: ");
				postOrder(root);
				break;
			default:
				System.out.print("\nInvalid traversal type\n");
				break;
			}
			System.out.println();
		}


		private void preOrder(Node localRoot) {
			if (localRoot != null) {
				System.out.print(localRoot.iData + " ");	
				preOrder(localRoot.leftChild);
				preOrder(localRoot.rightChild);	
			}
		}


		private void inOrder(Node localRoot) {
			if (localRoot != null) {
				inOrder(localRoot.leftChild);
				System.out.print(localRoot.iData + " ");
				inOrder(localRoot.rightChild);		
			}
		}


		private void postOrder(Node localRoot) {
			if (localRoot != null) {
				postOrder(localRoot.leftChild);
				postOrder(localRoot.rightChild);
				System.out.print(localRoot.iData + " ");		
			}
		}


		public void displayTree() {
			Stack<Node> globalStack = new Stack<Node>();
			globalStack.push(root);
			int nBlanks = 32;
			boolean isRowEmpty = false;
			System.out.println(
					".................................................................");
			while (isRowEmpty==false) {
				Stack<Node> localStack = new Stack<Node>();
				isRowEmpty = true;

				for (int j = 0; j < nBlanks; j++) {
					System.out.print(' ');
				}

				while (globalStack.isEmpty()==false) {
					Node temp = (Node) globalStack.pop();
					if (temp != null) {
						System.out.print(temp.iData);
						localStack.push(temp.leftChild);
						localStack.push(temp.rightChild);
						if (temp.leftChild != null ||
								temp.rightChild != null) {
							isRowEmpty = false;
						}
					}
					else {
						System.out.print("--");
						localStack.push(null);
						localStack.push(null);
					}

					for (int j = 0; j < nBlanks*2-2; j++) {
						System.out.print(' ');
					}
				} // end while globalStack not empty
				System.out.println();
				nBlanks /= 2;
				while (localStack.isEmpty()==false) {
					globalStack.push(localStack.pop());
				} // end while isRowEmpty is false
				System.out.println(
						".................................................................");
			} // end displayTree()
		} // end class Tree


		public Node findMin() {
			Node current = root;
			if (current == null) {
				return null;
			}
			else {
				while (current != null) {          // while no match
					Node next =  current.leftChild; 
					if (next != null) {
						current = next;
					}
					else {
						break;
					}
				}
			}
			return current;
		}// end findMin()

		public Node findMax() {
			Node current = root;
			if (current == null) {
				return null;
			}
			else {
				while (current != null) {          // while no match
					Node next =  current.rightChild; 
					if (next != null) {
						current = next;
					}
					else {
						break;
					}
				}
			}
			return current;
		}// end findMin()
	}
	////////////////////////////////////////////////////////////////

	class TreeApp {

		public static void main(String[] args) throws IOException {
			Tree theTree = new Tree();

			final Path IN_PATH = FileSystems.getDefault().getPath("input", "input.txt");
			Charset charset = Charset.forName("US-ASCII");
			try (BufferedReader reader = Files.newBufferedReader(IN_PATH, charset)) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					//System.out.println(line);
					if (line.startsWith("insert")) {
						line = line.replace("insert ", "");
						System.out.println("Inserting: " + line + "\n");
						String[] nodesToInsert = line.split(",");
						for (String string : nodesToInsert) {
							int intToInsert = Integer.parseInt(string);
							theTree.insert(intToInsert, intToInsert + 0.9);
						}
					}
					else if (line.startsWith("find")) {
						line = line.replace("find ", "");
						Node found = theTree.find(Integer.parseInt(line));
						if(found != null) {
							System.out.print("Found: ");
							found.displayNode();
							System.out.print("\n\n");
						}
						else {
							System.out.print("Could not find ");
							System.out.print(line + "\n\n");
						}
						
					}
					else if (line.startsWith("delete")) {
						line = line.replace("delete ", "");
						int valueToDelete = Integer.parseInt(line);
						boolean didDelete = theTree.delete(valueToDelete);
						if (didDelete) {
							System.out.print("Deleted: " + valueToDelete + "\n");
						}
						else {
							System.out.print("Could not delete value: " + valueToDelete+ "\n");
						}

					}
					else if (line.startsWith("traverse")) {
						line = line.replace("traverse ", "");
						theTree.traverse(Integer.parseInt(line));
					}
					else if (line.startsWith("min")) {
						Node minNode = theTree.findMin();
						if(minNode != null) {
							System.out.print("\nMin: ");
							minNode.displayNode();
							System.out.print("\n");
						}
						else {
							System.out.print("The tree is empty \n");
						}

					}
					else if (line.startsWith("max")) {
						Node maxNode = theTree.findMax();
						if(maxNode != null) {
							System.out.print("\nMax: ");
							maxNode.displayNode();
							System.out.print("\n");
						}
						else {
							System.out.print("The tree is empty \n");
						}

					}
					else if (line.startsWith("show")) {
						theTree.displayTree();
					}
				}
			} catch (IOException x) {
				System.err.format("IOException: %s%n", x);
			}
		}	
	}  // end TreeApp class
	////////////////////////////////////////////////////////////////


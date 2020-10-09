#include <stdio.h>
#include <stdlib.h>


struct node{
    int data;
    struct node * left;
    struct node * right;
};


struct BT{
    struct node * root;
    int number_items;
};


// Create a new Node
struct node * newNode(int data){

    // Allocate memory for new node
    struct node * new_node = (struct node*)malloc(sizeof(struct node));

    // Assign data to the node
    new_node->data = data;
    new_node->left = NULL;
    new_node->right = NULL;

    return new_node;
};


// Create a new Binary Tree
struct BT * newBT(int data){

    // Ensure only positive integers can be keys
    if (data >= 0){
        // Allocate memory for BT
        struct BT * new_BT = (struct BT*)malloc(sizeof(struct BT));

        // Create a node which will become the root
        new_BT->root = newNode(data);
        new_BT->number_items = 1;
        return new_BT;
    }

    return NULL;
};


// Add a value to the correct position in Binary Tree
int addValueToBT(struct BT * bt, int data){

    // Check if binary tree passed exists
    if (bt != NULL){

        // Make sure the node key is a positive number
        if (data >= 0){
            if (addValue(bt->root, data)){
                bt->number_items += 1;
            }
            return 1;
        }
    }
    else{
        return 0;
    }

    return 0;
};


// Recursively called by addValueToBT
int addValue(struct node * root, int data){
    int bool_val = 0;
    if (root != NULL){
        // Check to see which side it should be added to
        if (data > root->data){

            // See if right sub tree of child is occupied
            if (root->right != NULL){
                bool_val = addValue(root->right, data);
            }
            else{
                root->right = newNode(data);
                return 1;
            }
        }
        else if (data < root->data){
            // See if left sub tree of child is occupied
            if (root->left != NULL){
                bool_val = addValue(root->left, data);
            }
            else{
                root->left = newNode(data);
                return 1;
            }
        }
        else{
            // We dont add duplicates
            return bool_val;
        }
    }

    return bool_val;
}


// Free up memory used by Binary Tree once we are finished
void destroyBT(struct node * root){
    if (root != NULL){
        destroyBT(root->left);
        destroyBT(root->right);
        free(root);
    }
    else {
        return;
    };
    return;
};


// Find the lowest common ancestor of two nodes in a Binary Tree
int findLCA(struct BT * bt, int firstNodeValue, int secondNodeValue){

    // Check to make sure bt is not null
    if (bt != NULL){
        int firstNodePath[bt->number_items];
        int secondNodePath[bt->number_items];

        // Make sure arrays are initialised to -1
        for (int i=0; i < bt->number_items; i++){
            firstNodePath[i] = -1;
            secondNodePath[i] = -1;
        }

        // Store booleans for paths found to desired nodes
        int foundPath1 = findPath(bt->root, firstNodeValue, firstNodePath, 0);
        int foundPath2 = findPath(bt->root, secondNodeValue, secondNodePath, 0);

        // Make sure path to both nodes was found
        if (!foundPath1 || !foundPath2){
            return -1;
        }
        else{
            // While loop through both arrays and return LCA element
            int index = 0;
            while ((index < bt->number_items) && (firstNodePath[index] != -1 && secondNodePath[index] != -1)){
                if (firstNodePath[index] != secondNodePath[index]){
                    break;
                }
                index += 1;
            }

            // Return the LCA element
            return firstNodePath[index - 1];
        }
    }
    return -1;
}


int findPath(struct node * root, int target, int pathArray[], int index){

    int bool_val = 0;

    if (root != NULL){
        if (target > root->data){

            // Add this nodes data to the pathArray
            pathArray[index++] = root->data;

            // We need to go into the right sub tree
            bool_val = findPath(root->right, target, pathArray, index);

        }
        else if (target < root->data){
            // Add this nodes data to the pathArray
            pathArray[index++] = root->data;

            // We need to go into the left sub tree
            bool_val = findPath(root->left, target, pathArray, index);
        }
        else{
            // We have found the node we were looking for
            pathArray[index++] = root->data;
            bool_val = 1;
        };
    };

    return bool_val;
}


int main()
{
    // Create a binary tree

    /*          8
               / \
              1   9
               \   \
                4   10
               / \
              3   5
             /     \
            2       7
                   /
                  6

    */

    struct BT * bt = newBT(8);
    addValueToBT(bt, 1);
    addValueToBT(bt, 9);
    addValueToBT(bt, 10);
    addValueToBT(bt, 4);
    addValueToBT(bt, 3);
    addValueToBT(bt, 2);
    addValueToBT(bt, 5);
    addValueToBT(bt, 7);
    addValueToBT(bt, 6);


    // Find LCA for various valid inputs
    printf("Testing LCA for various valid inputs: \n");
    printf("LCA of (1,9) should be 8 :>   %d\n",findLCA(bt, 1, 9));
    printf("LCA of (3,5) should be 4 :>   %d\n",findLCA(bt, 3, 5));
    printf("LCA of (2,7) should be 4 :>   %d\n",findLCA(bt, 2, 7));
    printf("LCA of (2,6) should be 4 :>   %d\n",findLCA(bt, 2, 6));
    printf("LCA of (1,4) should be 1 :>   %d\n",findLCA(bt, 1, 4));
    printf("LCA of (5,1) should be 1 :>   %d\n",findLCA(bt, 5, 1));
    printf("LCA of (6,9) should be 8 :>   %d\n\n",findLCA(bt, 6, 9));


    // Test LCA on invalid or incorrect inputs
    printf("Testing LCA on invalid or incorrect inputs: \n");
    printf("LCA of (6,100) should be -1 as 100 does not exist :>   %d\n",findLCA(bt, 6, 100));
    printf("LCA of (-1,5) should be -1 as -1 isnt a valid input :>   %d\n",findLCA(bt, -1, 5));
    printf("LCA of (NULL,5) should be -1 as NULL isnt a valid input :>   %d\n\n",findLCA(bt, NULL, 5));


    // Test LCA on unusual tree structures
    struct BT * bt2 = newBT(10);
    printf("LCA of (10,10) should be 10 :>   %d\n",findLCA(bt2, 10, 10));

    addValueToBT(bt2, 9);
    addValueToBT(bt2, 8);
    addValueToBT(bt2, 7);
    addValueToBT(bt2, 6);
    addValueToBT(bt2, 5);

    printf("LCA of (10,5) should be 10 :>   %d\n",findLCA(bt2, 10, 5));
    printf("LCA of (9,5) should be 9 :>   %d\n",findLCA(bt2, 9, 5));
    printf("LCA of (6,5) should be 6 :>   %d\n",findLCA(bt2, 6, 5));


    // Clear memory used by Binary Trees
    destroyBT(bt);
    destroyBT(bt2);


    return 0;
};

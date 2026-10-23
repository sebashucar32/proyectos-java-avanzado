package org.faciles;

/*
One-Pass Removal of k-th Node from End
Given the head of a singly linked list and an integer k, remove the k-th node from the end in one traversal and return the new head. If k is invalid, return the original list.

Example

Input

head = [5, 6, 7, 8]
k = 3
Output

[6, 7, 8]
Explanation

The list has 4 nodes.
The k-th node from the end with k=3 is the 4th node from the end (value 5), which is the head. Removing it yields [6,7,8].
Input Format

The first line contains an integer n denoting the length of linked list.
The next n lines contains elements of the linked list.
The last line contains k.
Example

4
5
6
7
8
3
here 4 is the length of the linked list, followed by the elements of the list and value of k.

Constraints

0 <= number of nodes in head <= 1000
-10^9 <= value of each node <= 10^9
0 <= k <= 10^9
Output Format

Return the head of the modified linked list after removal.
Sample Input 0

1
5
1
Sample Output 0

5
Sample Input 1

2
1
2
0
Sample Output 1

1
*/

import java.io.*;
import java.util.stream.*;

class SinglyLinkedListNode {
    public int data;
    public SinglyLinkedListNode next;

    public SinglyLinkedListNode(int nodeData) {
        this.data = nodeData;
        this.next = null;
    }
}

class SinglyLinkedList {
    public SinglyLinkedListNode head;
    public SinglyLinkedListNode tail;

    public SinglyLinkedList() {
        this.head = null;
        this.tail = null;
    }

    public void insertNode(int nodeData) {
        SinglyLinkedListNode node = new SinglyLinkedListNode(nodeData);

        if (this.head == null) {
            this.head = node;
        } else {
            this.tail.next = node;
        }

        this.tail = node;
    }
}

class SinglyLinkedListPrintHelper {
    public static void printList(SinglyLinkedListNode node, String sep) {
        while (node != null) {
            System.out.print(node.data);

            node = node.next;

            if (node != null) {
                System.out.print(sep);
            }
        }
    }
}

class ResultOnePassRemoval {
    public static SinglyLinkedListNode removeKthNodeFromEnd(SinglyLinkedListNode head, int k) {
        if (head == null) {
            return null;
        }

        SinglyLinkedListNode dummy = new SinglyLinkedListNode(0);
        dummy.next = head;

        SinglyLinkedListNode fast = dummy;
        SinglyLinkedListNode slow = dummy;

        for (int i = 0; i <= k; i++) {
            if (fast.next == null) {
                return head;
            }
            fast = fast.next;
        }
        
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }

        slow.next = slow.next.next;
        return dummy.next;
    }
}

public class OnePassRemoval {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        SinglyLinkedList head = new SinglyLinkedList();

        int headCount = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, headCount).forEach(i -> {
            try {
                int headItem = Integer.parseInt(bufferedReader.readLine().trim());

                head.insertNode(headItem);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        int k = Integer.parseInt(bufferedReader.readLine().trim());

        SinglyLinkedListNode result = ResultOnePassRemoval.removeKthNodeFromEnd(head.head, k);

        SinglyLinkedListPrintHelper.printList(result, "\n");
        System.out.println();

        bufferedReader.close();
    }
}

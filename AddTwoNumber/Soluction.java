package AddTwoNumber;
/**
 * Definition for singly-linked list.
public class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

You are given two non-empty linked lists representing two non-negative integers. 
The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 */

class Soluction {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {       

        ListNode dummy = new ListNode();     
        
        ListNode current = dummy;

        while (l1 != null || l2 != null){
            if (l1 == null){
                current.val += l2.val;              
                l2 = l2.next;                
            }
            else if (l2 == null){
                current.val += l1.val;           
                l1 = l1.next;                
            }
            else {
                current.val = l1.val + l2.val;                
                l1 = l1.next;
                l2 = l2.next;
            }

            if (current.val < 10){
                current.next = new ListNode(0);
            }
            else {
                current.val = current.val % 10;
                current.next = new ListNode(1);
            }
            current = current.next;
        }

        return dummy.next;        
    }
    
}

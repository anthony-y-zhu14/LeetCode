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

class Solution {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {       

        ListNode dummy = new ListNode();     
        
        ListNode current = dummy;

        int carry  = 0;

        while (l1 != null || l2 != null){
            int val1, val2;
            val1 = l1 != null ? l1.val : 0;
            val2 = l2 != null ? l2.val : 0;
            
            int val = val1 + val2 + carry;
            carry = val / 10;
            val %= 10;
            
            current.next = new ListNode(val);
            if (carry > 0){
                current.next.next = new ListNode(1);
            }

            current = current.next;
            l1 = l1 != null ? l1.next : null;
            l2 = l2 != null ? l2.next : null;
        }

        return dummy.next;        
    }
    
}

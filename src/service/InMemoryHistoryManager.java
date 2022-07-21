package service;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    CustomLinkedList<Task> taskCustomLinkedList = new CustomLinkedList<>();
    HashMap<Integer, Node> taskHashMap = new HashMap<>();

    public CustomLinkedList<Task> getTaskCustomLinkedList() {
        return taskCustomLinkedList;
    }

    public HashMap<Integer, Node> getTaskHashMap() {
        return taskHashMap;
    }

    public class CustomLinkedList<Task> {
        private Node head;
        private Node tail;

        public void insert(Task data) {
            if (head == null) {
                head = new Node((tasks.Task) data, null, null);
                tail = head;
            } else {
                Node oldTail = tail;
                tail = new Node((tasks.Task) data, null, oldTail);
            }
        }

        private Node getLastNode() {
            if (this.head == null) {
                return null;
            } else {
                Node currentNode = head;
                while (currentNode.getNextNode() != null) {
                    currentNode = currentNode.getNextNode();
                }
                return currentNode;
            }
        }


        public Node removeNode(Node node) {
            Node oldPrevious = node.getPreviousNode();
            Node oldNext = node.getNextNode();
            if (oldPrevious == null) {
                oldNext.setPreviousNode(null);
            } else {
                oldPrevious.setNextNode(oldNext);
                oldNext.setPreviousNode(oldPrevious);
            }

            return node;
        }

        public Node getHead() {
            return head;
        }

        public void setHead(Node head) {
            this.head = head;
        }

        public Node getTail() {
            return tail;
        }

        public void setTail(Node tail) {
            this.tail = tail;
        }
    }

    public class Node {
        private Task data;
        private Node next;
        private Node previous;

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + data +
                    ", next=" + next +
                    ", previous=" + previous +
                    '}';
        }

        public Node getPreviousNode() {
            return previous;
        }

        public void setPreviousNode(Node previous) {
            this.previous = previous;
        }

        public Node(Task task, Node next, Node previous) {
            this.data = task;
            this.next = next;
            this.previous = previous;
        }

        public Task getData() {
            return data;
        }

        public void setData(Task data) {
            this.data = data;
        }

        public Node getNextNode() {
            return next;
        }

        public void setNextNode(Node nextNode) {
            this.next = nextNode;
        }

    }


    private ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskHistory = new ArrayList<>();
        if(taskCustomLinkedList.getHead() == null){
            return null;
        }else {
            Node currentNode = taskCustomLinkedList.getHead();
            while (currentNode != null) {
                taskHistory.add(currentNode.getData());
                currentNode = currentNode.getNextNode();
            }

        }
        return taskHistory;
    }

    private void linkLast(Task task) {
        add(task);
    }

    @Override
    public void add(Task task) {
        if(taskHashMap.containsKey(task.getId())) {
            taskCustomLinkedList.removeNode(taskHashMap.get(task.getId()));
        }
        taskCustomLinkedList.insert(task);
        taskHashMap.put(task.getId(), taskCustomLinkedList.getLastNode());
    }

    @Override
    public void remove(int id) {
        taskHashMap.remove(id);
        taskCustomLinkedList.removeNode(taskHashMap.get(id));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return this.getAllTasks();
    }


    @Override
    public void printTaskHistory() {
        for(Task task : this.getHistory()) {
            System.out.println("Task history: " + task.toString());
        }
    }

}

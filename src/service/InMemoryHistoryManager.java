package service;

import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> taskCustomLinkedList;
    private HashMap<Integer, Node> taskHashMap;

    public InMemoryHistoryManager() {
        taskCustomLinkedList = new CustomLinkedList<>();
        taskHashMap = new HashMap<>();
    }

    public CustomLinkedList<Task> getTaskCustomLinkedList() {
        return taskCustomLinkedList;
    }

    public HashMap<Integer, Node> getTaskHashMap() {
        return taskHashMap;
    }

    public class CustomLinkedList<Task> {
        private Node head;
        private Node tail;

        public void insert(tasks.Task data) {
            if (head == null) {
                head = new Node(data, null, null);
                tail = head;
            } else {
                Node oldTail = tail;
                tail = new Node(data, null, oldTail);
                oldTail.setNextNode(tail);
            }
        }

        private Node getLastNode() {
            if (head == null) {
                return null;
            } else {
                Node currentNode = head;
                while (currentNode.getNextNode() != null) {
                    currentNode = currentNode.getNextNode();
                }
                return currentNode;
            }
        }


        public void removeNode(Node node) {
            Node oldPrevious = node.getPreviousNode();
            Node oldNext = node.getNextNode();
            if (node == head) {
                oldNext.setPreviousNode(null);
                head = oldNext;
            } else if(oldNext != null) {
                oldPrevious.setNextNode(oldNext);
                oldNext.setPreviousNode(oldPrevious);
            } else {
                oldPrevious.setNextNode(null);
                tail = oldPrevious;
            }
            System.out.println("Node was removed");
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
                    "task=" + data;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(data, node.data) && Objects.equals(next, node.next) && Objects.equals(previous, node.previous);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data, next, previous);
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
        if(taskHashMap.containsKey(task.getId())) { // не понимаю, почему сюда приходит пустая мапа и пустой линкедлист и возникает NullPointerException. На предыдущем шаге мапа и лист еще полные
            taskCustomLinkedList.removeNode(taskHashMap.get(task.getId()));
            System.out.println("Previous node removed");
        }
        taskCustomLinkedList.insert(task);
        taskHashMap.put(task.getId(), taskCustomLinkedList.getTail());
        System.out.println("Task inserted to CustomLinkedList, id and node added to TaskHashMap");
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
            System.out.println("Task history: id " + task.getId() + " " + task.toString());
        }
    }

    @Override
    public void printTaskHashMap() {
        for(Integer id : taskHashMap.keySet()) {
            System.out.println("Id " + id + ":" + taskHashMap.get(id).toString());
        }
    }

}

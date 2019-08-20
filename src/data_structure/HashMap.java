package data_structure;

public class HashMap<Key, Value> {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_TABLE_SIZE = 16;
    private static final int TREEIFY_THRESHOLD = 8;

    private int size; // HashMap 大小，即存储键值对的个数
    private float loadFactor; // 负载因子
    private int threshold; // 当前 table 长度和 loadFactor 负载因子下，能容下的最大键值对数
    private Node[] table; // 存储键值对的数组

    public HashMap() {
        this(DEFAULT_TABLE_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initSize, float initLoadFactor) {
        this.loadFactor = initLoadFactor;
        // table 的长度必须是 2^n
        int tableSize = tableSizeFor(initSize);
        this.table = new Node[tableSize];
        // 初始化阈值
        this.threshold = (int) (this.table.length * this.loadFactor);
    }

    /**
     * 节点数据结构，存储 Key 和 Value
     */
    private static class Node<K, V> {
        int hash;
        K key;
        V value;
        Node next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 红黑树数据结构
     */
    private static class TreeNode<K, V> extends Node<K, V> {
        TreeNode<K, V> parent;
        TreeNode<K, V> left;
        TreeNode<K, V> right;

        public TreeNode(int hash, K key, V value) {
            super(hash, key, value);
        }

        /**
         * 将一个节点插入当前的红黑树
         */
        TreeNode<K, V> putTreeVal(Node<K, V> node) {
            return (TreeNode) node;
        }
    }

    /**
     * 求大于等于 cap 且为 2^n 的数
     */
    static final int tableSizeFor(int cap) {
        return 0;
    }

    public void put(Key key, Value value) {
        int hashCode = key.hashCode();
        int hash = hash(hashCode);
        int index = hash & (table.length - 1);
        Node head = table[index];
        // 如果当前位置还没有存储，则直接创建节点
        if (head == null) {
            table[index] = new Node<>(hashCode, key, value);
        }
        // 红黑树则插入
        else if (head instanceof TreeNode) {
            ((TreeNode<Key, Value>) head).putTreeVal(new Node<>(hash, key, value));
        }
        // 否则要遍历当前链表，为其找到合适位置
        else {
            int nodeCount = 0;
            Node tail = null;
            while (head != null) {
                nodeCount++;
                // 如果 key 已经存在，则直接替换 value，直接 return，不会 size++
                if (head.hash == hashCode && head.key.equals(key)) {
                    head.value = value;
                    return;
                }
                // 找到尾结点
                if (head.next == null) {
                    tail = head;
                }
                head = head.next;
            }
            // 循环结束，表示遍历到尾，未找到 key 存在，则新建一个节点
            tail.next = new Node<>(hashCode, key, value);
            // 链表长度超过阈值，转为红黑树
            if (nodeCount > TREEIFY_THRESHOLD) {
                treeifyBin(index);
            }
        }
        // 最后记录键值对个数增加
        size++;
    }

    /**
     * 将 index 所在的链表转换为红黑树
     */
    private void treeifyBin(int index) { }

    /**
     * 扰动函数：目的是增大
     */
    private int hash(int hashCode) {
        return 0;
    }

    private void resize() {
        if (size <= threshold) {
            return;
        }
        Node<Key, Value>[] oldTable = table;
        int oldCap = oldTable.length;
        // 计算新数组长度，并初始化新数组
        int newCap = oldCap << 1;
        table = new Node[newCap];
        threshold = (int) (newCap * loadFactor);
        // 遍历每个桶
        for (int i = 0; i < oldCap; i++) {
            Node<Key, Value> head = oldTable[i];
            if (head == null) {
                continue;
            }
            // 红黑树
            if (head instanceof TreeNode) {
                reassignTreeNode(head);
            }
            // 遍历链表，重新分配
            else {
                reassignNodeChain(head, i, oldCap);
            }
        }
    }

    /**
     * 将链表上的节点重新分配
     */
    private void reassignNodeChain(Node<Key, Value> head, int currentIndex, int oldCap) {
        // 将一条链表拆成两条链表
        Node<Key, Value> lowHead = null, lowTail = null;
        Node<Key, Value> highHead = null, highTail = null;
        do {
            // 低位链，后面解释为什么这样判断
            if ((head.hash & oldCap) == 0) {
                if (lowHead == null) {
                    lowHead = head;
                }
                if (lowTail != null) {
                    lowTail.next = head;
                }
                lowTail = head;
            }
            // 高位链
            else {
                if (highHead == null) {
                    highHead = head;
                }
                if (highTail != null) {
                    highTail.next = head;
                }
                highTail = head;
            }
        } while ((head = head.next) != null);
        // 将两条链表放入新 table，后面解释为什么这样放
        table[currentIndex] = lowHead;
        table[currentIndex + oldCap] = highHead;
    }

    /**
     * 将红黑树上的节点重新分配
     */
    private void reassignTreeNode(Node<Key, Value> head) { }
}
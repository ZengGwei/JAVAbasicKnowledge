#  HashMap源码
  hashMap 的默认大小16 ，默认加载因子 0.75 
## 1.数据结构
* hashMap 数据结构是由 数组+链表（红黑树） 
* 是由Node<K,V>[] 存储 ，相同index 就是链表结构，当链表>8 ,转换成红黑树
   
        class Node<K,V> implements Map.Entry<K,V> {
            final int hash;
            final K key;
             Node<K,V> next;
            //get/set
        }
## 2.方法
     public V put(K key, V value) {
            return putVal(hash(key), key, value, false, true);
        }
>key的hash值是由32位hashCode 异或 高16位  ？原因是：确定数组index 是 容量-1 & hashcode,为了减少碰撞
 这样把hashcode的高16位也利用起来。

     static final int hash(Object key) {
            int h;
            return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        } 
>
       
     final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                       boolean evict) {
            Node<K,V>[] tab; Node<K,V> p; int n, i;
            if ((tab = table) == null || (n = tab.length) == 0)
                n = (tab = resize()).length;                       // 1.初始化table
            if ((p = tab[i = (n - 1) & hash]) == null)             //   取出tab[index]  
                tab[i] = newNode(hash, key, value, null);          // hash不冲突  2.创建节点存入tab[]
            else {                                                // hash 冲突
                Node<K,V> e; K k;
                if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k)))) // key 相等
                    e = p;
                else if (p instanceof TreeNode)         //红黑树
                    e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
                else {    //链表
                    for (int binCount = 0; ; ++binCount) {//循环链表 存入tail
                        if ((e = p.next) == null) {
                            p.next = newNode(hash, key, value, null);
                            if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st   //判断链表长度
                                treeifyBin(tab, hash); //转换
                            break;
                        }
                        if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                            break;
                        p = e;
                    }
                }
                if (e != null) { // existing mapping for key
                    V oldValue = e.value;
                    if (!onlyIfAbsent || oldValue == null)
                        e.value = value;
                    afterNodeAccess(e);
                    return oldValue;
                }
            }
            ++modCount;
            if (++size > threshold)    //大小与 阈值判断 是否扩容
                resize();
            afterNodeInsertion(evict);
            return null;
        }
>  链表转红黑树  ，链表转树 若容器大小小于64 选择扩容 

     final void treeifyBin(Node<K,V>[] tab, int hash) {
            int n, index; Node<K,V> e;
            if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)   //链表转树 若容器大小小于64 扩容 
                resize();
            else if ((e = tab[index = (n - 1) & hash]) != null) {
                TreeNode<K,V> hd = null, tl = null;
                do {
                    TreeNode<K,V> p = replacementTreeNode(e, null);
                    if (tl == null)
                        hd = p;
                    else {
                        p.prev = tl;
                        tl.next = p;
                    }
                    tl = p;
                } while ((e = e.next) != null);
                if ((tab[index] = hd) != null)
                    hd.treeify(tab);
            }
        }  
> 扩容 1.若原先tab==null  初始化 tab            
        2. 采用高低链表，低链表 的index 不变，高链表的index=old_index+old_resize;

     final Node<K,V>[] resize() {
            Node<K,V>[] oldTab = table;
            int oldCap = (oldTab == null) ? 0 : oldTab.length;
            int oldThr = threshold;
            int newCap, newThr = 0;
            if (oldCap > 0) {
                if (oldCap >= MAXIMUM_CAPACITY) {
                    threshold = Integer.MAX_VALUE;
                    return oldTab;
                }
                else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                         oldCap >= DEFAULT_INITIAL_CAPACITY)
                    newThr = oldThr << 1; // double threshold
            }
            else if (oldThr > 0) // initial capacity was placed in threshold
                newCap = oldThr;
            else {               // zero initial threshold signifies using defaults
                newCap = DEFAULT_INITIAL_CAPACITY;
                newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
            }
            if (newThr == 0) {
                float ft = (float)newCap * loadFactor;
                newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                          (int)ft : Integer.MAX_VALUE);
            }
            threshold = newThr;
            @SuppressWarnings({"rawtypes","unchecked"})
                Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
            table = newTab;
            if (oldTab != null) {
                for (int j = 0; j < oldCap; ++j) {
                    Node<K,V> e;
                    if ((e = oldTab[j]) != null) {
                        oldTab[j] = null;
                        if (e.next == null)
                            newTab[e.hash & (newCap - 1)] = e;
                        else if (e instanceof TreeNode)
                            ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                        else { // preserve order
                            Node<K,V> loHead = null, loTail = null;
                            Node<K,V> hiHead = null, hiTail = null;
                            Node<K,V> next;
                            do {
                                next = e.next;
                                if ((e.hash & oldCap) == 0) {
                                    if (loTail == null)
                                        loHead = e;
                                    else
                                        loTail.next = e;
                                    loTail = e;
                                }
                                else {
                                    if (hiTail == null)
                                        hiHead = e;
                                    else
                                        hiTail.next = e;
                                    hiTail = e;
                                }
                            } while ((e = next) != null);
                            if (loTail != null) {
                                loTail.next = null;
                                newTab[j] = loHead;
                            }
                            if (hiTail != null) {
                                hiTail.next = null;
                                newTab[j + oldCap] = hiHead;
                            }
                        }
                    }
                }
            }
            return newTab;
        }        
## jdk8 hashMap 总结
1. 其数据结构是采用节点数组+链表/红黑树
2. 大小默认16，扩容因子 0.75f  ，其容量大小强制是 2的n次幂方
3. 扩容（rehash）时机：当大小小于64时 ，某链表数>8; 元素数量超过 阈值 size*0.75f 时
4. 高并发下扩容会存在数据丢失

######  与jdk7 比较：
1. 数据结构 有数据+链表----->数组+链表/红黑树 原因就是哪怕将hash的碰撞降到最低，也不能避免链表会越来越长
      >     原因：链表属于插入快，遍历慢的数据结构 
      >           完全二叉树插入慢，遍历快 
      >           红黑树插入和查询都较快  
2. jdk8中要简化hash算法 
3. 新节点插入顺序：jdk7在头部插入，jdk8在尾部插入
4. 扩容机制  jdk7 就是两个循环遍历 链表反转（问题：在多线程下当2个线程操作统一条链表就有可能出现链表闭合）
   由于jdk8是顺序拷贝，所以就不会产生jdk7的死锁
                     


2、那你能讲讲HashMap的实现原理吗？
    
3、HashMap什么时候会进行rehash？

4、HashMap什么时候会进行扩容？

5、那HashMap的初始容量设置成多少比较合适呢？

6、结合源码说说HashMap在高并发场景中为什么会出现死循环？

7、JDK1.8中对HashMap做了哪些性能优化？

8、HashMap和HashTable有何不同？

9、HashMap 和 ConcurrentHashMap 的区别？

10、ConcurrentHashMap和LinkedHashMap有什么区别？

11、为什么ConcurrentHashMap中的链表转红黑树的阀值是8？
   答：  理想情况下使用随机的哈希码，容器中节点分布在hash桶中的频率遵循泊松分布(具体可以查看http://en.wikipedia.org/wiki/Poisson_distribution)，
     按照泊松分布的计算公式计算出了桶中元素个数和概率的对照表，可以看到链表中元素个数为8时的概率已经非常小
          0:    0.60653066
          1:    0.30326533
          2:    0.07581633
          3:    0.01263606
          4:    0.00157952
          5:    0.00015795
          6:    0.00001316
          7:    0.00000094
          8:    0.00000006
         more: less than 1 in ten million
12、什么是ConcurrentSkipListMap？他和ConcurrentHashMap有什么区别？

13、还看过其他的源码吗？Spring的源码有了解吗？

14、SpringBoot的源码呢？知道starter是怎么实现的吗？

#  HashMap源码
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
    putVal()    







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

12、什么是ConcurrentSkipListMap？他和ConcurrentHashMap有什么区别？

13、还看过其他的源码吗？Spring的源码有了解吗？

14、SpringBoot的源码呢？知道starter是怎么实现的吗？
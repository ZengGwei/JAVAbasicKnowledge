import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 〈〉
 * create by zgw on 2019/6/28
 */
public class PryIntoHashMap {

    public static void main(String[] args) {

        Map<String,Object> map = new HashMap<>(15);
        //有参数构造器  只做了两件事：1.确认扩容因子默认0.75    this.loadFactor = loadFactor;
        //   初始化大小为2的n次幂方，默认16；      2.  this.threshold = tableSizeFor(initialCapacity);


        for (int i = 0; i <31 ; i++) {
            map.put("key"+i,"value"+i);
        }

        System.out.println(map.get("key1"));

        Map<String,Object> table  = new Hashtable<>();
        for (int i = 0; i <10 ; i++) {
            table.put("key"+1,"val"+i);
        }
        table.get("key1");

        Map<String,Object> linkedHashMap = new LinkedHashMap();
        for (int i = 0; i <10 ; i++) {
            linkedHashMap.put("key"+1,"val"+i);//继承  hashMap  ,

        }
        linkedHashMap.get("key1");

        Map<String,Object> concurrentHashMap = new ConcurrentHashMap<>() ;

        Map<String,Object> skipListMap = new ConcurrentSkipListMap<>();
        for (int i = 0; i <10 ; i++) {
            skipListMap.put("key"+1,"val"+i);//继承  hashMap  ,
        }
        skipListMap.get("key1");

    }





}

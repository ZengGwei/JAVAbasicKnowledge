import java.util.HashMap;
import java.util.Map;

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







    }





}

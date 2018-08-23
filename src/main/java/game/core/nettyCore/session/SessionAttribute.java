package game.core.nettyCore.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionAttribute {
    private Map<String, String> attributes = new ConcurrentHashMap<>();

//    public int getInt(String key) {
//        return Integer.parseInt();
//    }

    public void put(String key, String val) {
        attributes.put(key, val);
    }

    public String get(String key) {
        return attributes.get(key);
    }


}

package game.core.nettyCore.http;

import game.core.nettyCore.HandlerAnnotation;
import game.core.nettyCore.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nullzZ
 */
public class HttpHandlerManager {
    private static final Logger logger = LoggerFactory.getLogger(HttpHandlerManager.class);
    @SuppressWarnings("rawtypes")
    private Map<Integer, IHttpHandler> hanlers = new HashMap<>();
    private Map<Integer, Class<?>> messageClazz = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public void init(String packageName) throws Exception {
        List<Class<?>> clazzs = ClassUtil.getClasses(packageName);
        for (Class<?> c : clazzs) {
            HandlerAnnotation ann = c.getAnnotation(HandlerAnnotation.class);
            if (ann != null) {
                Object obj = c.newInstance();
                // Field f = c.getSuperclass().getDeclaredField("message");
                // f.setAccessible(true);
                // f.set(obj, ann.messageClass());
                messageClazz.put(ann.id(), ann.messageClass());
                hanlers.put(ann.id(), (IHttpHandler) obj);
                logger.debug("加载handler:--" + c.getName());

            }
        }
    }

    @SuppressWarnings("rawtypes")
    public IHttpHandler getHandler(int id) {
        return hanlers.get(id);
    }

    public Class<?> getMessageClazz(int id) {
        return messageClazz.get(id);
    }

}

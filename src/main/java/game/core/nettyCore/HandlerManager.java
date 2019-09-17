package game.core.nettyCore;

import game.core.nettyCore.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nullzZ
 */
public class HandlerManager {
    private static final Logger logger = LoggerFactory.getLogger(HandlerManager.class);
    @SuppressWarnings("rawtypes")
    private Map<Short, IHandler> handlers = new HashMap<>();
    private Map<Short, Class<?>> messageClazz = new HashMap<>();
    private Map<Short, String> handlersName = new HashMap<>();
    private boolean isSpring;

    @SuppressWarnings("rawtypes")
    public void init(String packageName, boolean isSpring) throws Exception {
        this.isSpring = isSpring;
        List<Class<?>> clazzs = ClassUtil.getClasses(packageName);
        for (Class<?> c : clazzs) {
            HandlerAnnotation ann = c.getAnnotation(HandlerAnnotation.class);
            if (ann != null) {
                Object obj = c.newInstance();
                handlers.put(ann.id(), (IHandler) obj);
                handlersName.put(ann.id(), toLowerCaseFirstOne(obj.getClass().getSimpleName()));
                logger.info("加载handler:--" + c.getName());
                for (Type type : c.getGenericInterfaces()) {
                    if (type instanceof ParameterizedType) {
                        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                        messageClazz.put(ann.id(), Class.forName(actualTypeArguments[1].getTypeName()));
                    }
                }


            }
        }
    }

    @SuppressWarnings("rawtypes")
    public IHandler getHandler(short id) {
        String name = handlersName.get(id);
        IHandler handler = null;
        if (isSpring) {
//            handler = (IHandler) SpringContextUtil.getBean(name);
        } else {
            handler = handlers.get(id);
        }
        return handler;
    }

    public String getHandlerName(short id) {
        return handlersName.get(id);
    }

    public Class<?> getMessageClazz(short id) {
        return messageClazz.get(id);
    }


    //首字母转小写
    private String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }


}

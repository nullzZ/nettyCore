package game.core.nettyCore;

import game.core.nettyCore.spring.SpringContextUtil;
import game.core.nettyCore.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
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
    private Map<Integer, IHandler> hanlers = new HashMap<>();
    private Map<Integer, Class<?>> messageClazz = new HashMap<>();
    private boolean isSpring;

    @SuppressWarnings("rawtypes")
    public void init(String packageName, boolean isSpring) throws Exception {
        List<Class<?>> clazzs = ClassUtil.getClasses(packageName);
        for (Class<?> c : clazzs) {
            HandlerAnnotation ann = c.getAnnotation(HandlerAnnotation.class);
            if (ann != null) {
                Object obj = c.newInstance();
                // Field f = c.getSuperclass().getDeclaredField("message");
                // f.setAccessible(true);
                // f.set(obj, ann.messageClass());
//                messageClazz.put(ann.id(), ann.messageClass());
                hanlers.put(ann.id(), (IHandler) obj);
                logger.debug("加载handler:--" + c.getName());
                for (Type type : c.getGenericInterfaces()) {
                    if (type instanceof ParameterizedType) {
                        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                        messageClazz.put(ann.id(), Class.forName(actualTypeArguments[1].getTypeName()));
//                        for (Type type2 : actualTypeArguments) {
//
//                            System.out.println("泛型参数类型：" + type2);
//                            Class cc = Class.forName(type2.getTypeName());
//                            System.out.println("@" + cc);
//                        }
                    }
                }


            }
        }
    }

    @SuppressWarnings("rawtypes")
    public IHandler getHandler(int id) {
        IHandler handler = hanlers.get(id);
        if (isSpring) {
            SpringContextUtil.getBean(toLowerCaseFirstOne(handler.getClass().getSimpleName()));
        }
        return handler;
    }

    public Class<?> getMessageClazz(int id) {
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

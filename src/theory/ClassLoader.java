package theory;

import java.util.Map;

public class ClassLoader {
    /**
     * 父加载器
     */
    ClassLoader parent;
    /**
     * 已经加载的 class
     * 这个属性是我假想的，为了配合 {@link #findLoadedClass} 的实现
     */
    Map<String, Class> loadedClasses;

    /**
     * 尝试从已经加载的类中寻找，如果没有返回 null
     * 真实的实现是 native 方法，我猜想应该是存了一个类名到类实例的映射，如下
     */
    Class<?> findLoadedClass(String name) {
        return loadedClasses.get(name);
    }

    /**
     * 当前类加载器尝试去加载一个类，内部实现就是类的加载过程
     * 为什么说尝试？因为当前类加载器可能无法加载这个类，会返回 null
     */
    Class<?> findClass(String name) {
        return null;
    }

    /**
     * 双亲委派模型的实现，也是类加载的直接入口
     */
    Class<?> loadClass(String name) {
        // 自己检查
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass == null) {
            // 尝试让父类加载
            if (parent == null) {
                // 后面介绍 Java 类加载器的层级结构
            } else {
                loadedClass = parent.loadClass(name);
            }
            // 如果父类没有加载，自己加载
            if (loadedClass == null) {
                loadedClass = findClass(name);
            }
        }
        // 返回最终结果，此时还有可能为 null
        // 如果当前类加载器有子加载器，则子加载器还会尝试加载，否则就会包 ClassNotFoundException
        return loadedClass;
    }
}

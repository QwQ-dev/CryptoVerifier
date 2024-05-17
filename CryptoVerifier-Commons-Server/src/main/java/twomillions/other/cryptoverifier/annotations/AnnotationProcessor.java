package twomillions.other.cryptoverifier.annotations;

import lombok.experimental.UtilityClass;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 用于处理注解的工具类。
 *
 * <p>
 * 该类使用 {@link UtilityClass} 自动生成私有构造函数，并自动处理类、字段、内部类等。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/27
 */
@UtilityClass
@SuppressWarnings("unused")
public class AnnotationProcessor {
    /**
     * 获取指定包下带有指定注解的类集合。
     *
     * @param packageName 指定包
     * @param annotation 要查找的注解类的 {@link Class} 对象
     * @return 包含所有带有指定注解的类的集合
     */
    public static Set<Class<?>> getClassesWithAnnotation(String packageName, Class<? extends Annotation> annotation) {
        return new Reflections(packageName, Scanners.TypesAnnotated)
                .getTypesAnnotatedWith(annotation);
    }
}

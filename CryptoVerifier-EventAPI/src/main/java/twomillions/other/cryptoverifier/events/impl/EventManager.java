package twomillions.other.cryptoverifier.events.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import twomillions.other.cryptoverifier.events.impl.annotations.EventHandler;
import twomillions.other.cryptoverifier.events.impl.enums.EventPriority;
import twomillions.other.cryptoverifier.events.impl.interfaces.Event;
import twomillions.other.cryptoverifier.events.impl.interfaces.EventAPI;
import twomillions.other.cryptoverifier.events.impl.interfaces.Listener;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件管理器。提供注册监听器与触发事件的方法。
 *
 * <p>
 * 使用单例模式实现，需通过静态方法 {@link EventManager#getEventManager()} 获取实例。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventManager implements EventAPI {
    /**
     * 单例模式下的事件管理器实例。
     */
    @Getter
    private static final EventManager eventManager = new EventManager();

    /**
     * 事件类型与已注册监听器的并发哈希表。
     */
    @Getter
    private final Map<Class<? extends Event>, List<RegisteredListener>> eventListeners = new ConcurrentHashMap<>();

    /**
     * 注册监听器。
     *
     * @param listener {@link Listener}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void registerListener(Listener listener) {
        Class<?> listenerClass = listener.getClass();
        Method[] methods = listenerClass.getDeclaredMethods();

        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(EventHandler.class))
                .filter(method -> {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    return parameterTypes.length == 1 && Event.class.isAssignableFrom(parameterTypes[0]);
                })
                .forEach(method -> {
                    Class<? extends Event> eventType = (Class<? extends Event>) method.getParameterTypes()[0];
                    EventHandler annotation = method.getAnnotation(EventHandler.class);
                    EventPriority priority = annotation.priority();
                    RegisteredListener registeredListener = new RegisteredListener(listener, method, priority);

                    eventListeners.computeIfAbsent(eventType, key -> new ArrayList<>()).add(registeredListener);
                });
    }

    /**
     * 触发事件。
     *
     * @param event {@link Event}
     */
    @Override
    @SneakyThrows
    public void callEvent(Event event) {
        List<RegisteredListener> sortedListeners = new ArrayList<>(eventListeners.getOrDefault(event.getClass(), Collections.emptyList()));

        if (sortedListeners.isEmpty()) {
            return;
        }

        sortedListeners.sort(Comparator.comparing(RegisteredListener::getPriority));

        for (RegisteredListener registeredListener : sortedListeners) {
            registeredListener.getMethod().invoke(registeredListener.getListener(), event);
        }
    }
}

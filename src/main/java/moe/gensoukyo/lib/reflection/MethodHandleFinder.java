package moe.gensoukyo.lib.reflection;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Better (maybe slower) GetMethod()
 * @author ChloePrime
 */
public class MethodHandleFinder {
    /**
     * Get a method handle from a class
     * Supports dynamic generated classes
     *
     * @return MethodHandle representing the method
     */
    @Nonnull
    public static MethodHandle getMethod(@Nonnull Class<?> clazz,
                                         @Nonnull String name,
                                         Class<?>... args)
            throws IllegalAccessException, NoSuchMethodException {
        return getMethod0(clazz.getMethods(), name, args);
    }

    /**
     * More powerful version of getMethod()
     *
     * @see MethodHandleFinder#getMethod(Class, String, Class[])
     */
    @Nonnull
    public static MethodHandle getDeclaredMethod(@Nonnull Class<?> clazz,
                                                 @Nonnull String name,
                                                 Class<?>... args)
            throws IllegalAccessException, NoSuchMethodException {
        return getMethod0(clazz.getDeclaredMethods(), name, args);
    }

    @Nonnull
    private static MethodHandle getMethod0(@Nonnull Method[] methods,
                                           @Nonnull String name,
                                           Class<?>... args)
            throws IllegalAccessException, NoSuchMethodException {
        // Don't use getMethod()
        // to support dynamic classes (like script classes)

        //
        Method slowMethod = filterMethod(methods, name, args);

        if (!slowMethod.isAccessible()) {
            slowMethod.setAccessible(true);
        }
        return MethodHandles.lookup().unreflect(slowMethod);
    }

    @Nonnull
    private static Method filterMethod(@Nonnull Method[] methods,
                                       @Nonnull String name,
                                       Class<?>[] args)
            throws NoSuchMethodException {

        Method[] methodsWithName = Arrays.stream(methods)
                .filter(method -> name.equals(method.getName()))
                .toArray(Method[]::new);

        if (methodsWithName.length == 0) {
            throw new NoSuchMethodException("No method with such name");
        }
        if (args.length == 0 && methodsWithName.length == 1) {
            return methodsWithName[0];
        } else {
            return Arrays.stream(methodsWithName).filter(
                    method -> Arrays.equals(args, method.getParameterTypes())
            ).findFirst().orElseThrow(() -> fail(args.length));
        }
    }

    private static NoSuchMethodException fail(int argCount) {
        String errMsg = (argCount == 0)
                ? "There are multiple overloads, please specify arg types"
                : "No method with such args";
        return new NoSuchMethodException(errMsg);
    }

    private MethodHandleFinder() {
    }
}
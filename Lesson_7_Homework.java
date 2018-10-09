// Java 3
// Lesson 7
// Homework
// Автор: Евгений Пермяков

//------------------------------------
// "Тестирование"

// TestsExecutor.java
package ru.geekbrains.pe1999.java3.hw7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

public class TestsExecutor {

    public static void start(String cn) {
        try {
            start(Class.forName(cn));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void start(Class c) {
        try {
            executeTests(c);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void executeTests(Class c) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        Method[] methods = c.getDeclaredMethods();

        // Проверка аннотаций
        boolean isBeforeSuitePresent = false;
        boolean isAfterSuitePresent = false;
        for (int i = 0; i < methods.length; i++) {

            if(isMoreThanOneTestAnnotationPresent(methods[i]))
                throw new RuntimeException("У метода " + methods[i].getName() +
                        " одновременно присутствует больше, чем одна аннотация из перечня: @BeforeSuite, @Test, @AfterSuite");

            if(methods[i].isAnnotationPresent(Test.class) &&
                    (methods[i].getAnnotation(Test.class).priority() < 1 ||
                     methods[i].getAnnotation(Test.class).priority() > 10))
                throw new RuntimeException("Приоритет в аннотации @Test для метода " + methods[i].getName() +
                        " выходит за пределы 1...10");

            if(isBeforeSuitePresent & methods[i].isAnnotationPresent(BeforeSuite.class))
                throw new RuntimeException("Больше, чем один метод с аннотацией @BeforeSuite");

            if(isAfterSuitePresent & methods[i].isAnnotationPresent(AfterSuite.class))
                throw new RuntimeException("Больше, чем один метод с аннотацией @AfterSuite");

            isBeforeSuitePresent |= methods[i].isAnnotationPresent(BeforeSuite.class);
            isAfterSuitePresent |= methods[i].isAnnotationPresent(AfterSuite.class);
        }

        // Сортировка методов по порядку выполнения
        Arrays.sort(methods, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                if(!isAnyTestsAnnotationsPresent(o1))
                    return  1;
                if(!isAnyTestsAnnotationsPresent(o2))
                    return  -1;
                if(o1.isAnnotationPresent(BeforeSuite.class))
                    return -1;
                if(o2.isAnnotationPresent(BeforeSuite.class))
                    return 1;
                if(o1.isAnnotationPresent(AfterSuite.class))
                    return 1;
                if(o2.isAnnotationPresent(AfterSuite.class))
                    return -1;
                return o1.getAnnotation(Test.class).priority() - o2.getAnnotation(Test.class).priority();
            }
        });

        // Выполнение методов с аннотациями
        Object tc = c.newInstance();

        for (int i = 0; i < methods.length; i++) {
            if(!isAnyTestsAnnotationsPresent(methods[i]))
                break;
            methods[i].invoke(tc);
        }
    }

    private static boolean isAnyTestsAnnotationsPresent(Method m) {
        return m.isAnnotationPresent(Test.class) ||
               m.isAnnotationPresent(BeforeSuite.class) ||
               m.isAnnotationPresent(AfterSuite.class);
    }

    private static boolean isMoreThanOneTestAnnotationPresent(Method m) {
        int annotationsCounter = 0;
        if(m.isAnnotationPresent(Test.class)) annotationsCounter++;
        if(m.isAnnotationPresent(BeforeSuite.class)) annotationsCounter++;
        if(m.isAnnotationPresent(AfterSuite.class)) annotationsCounter++;
        return annotationsCounter > 1;
    }
}
// End of TestsExecutor.java

// Test.java
package ru.geekbrains.pe1999.java3.hw7;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    int priority() default 10;
}
// End of Test.java

// BeforeSuite.java
package ru.geekbrains.pe1999.java3.hw7;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeSuite {
}
// End of BeforeSuite.java

// AfterSuite.java
package ru.geekbrains.pe1999.java3.hw7;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterSuite {
}
// End of AfterSuite.java

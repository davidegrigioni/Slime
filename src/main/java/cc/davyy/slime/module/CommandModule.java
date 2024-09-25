package cc.davyy.slime.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.reflections.Reflections;

import java.util.Set;

public class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        registerCommandBindings();
    }

    private void registerCommandBindings() {
        Reflections reflections = new Reflections("cc.davyy.slime.commands");
        Set<Class<?>> singletonClasses = reflections.getTypesAnnotatedWith(Singleton.class);

        for (Class<?> clazz : singletonClasses) {
            bind(clazz).in(Singleton.class);
        }
    }

}
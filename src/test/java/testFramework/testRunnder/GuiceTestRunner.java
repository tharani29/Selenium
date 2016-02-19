package testRunner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class GuiceTestRunner extends BlockJUnit4ClassRunner {
    /**
     * The Guice Injector.
     */
    private final transient Injector injector;

    /**
     * Contructor.
     *
     * @param klass The in test.
     * @throws InitializationError If something goes wrong.
     * @checkstyle ThrowsCountCheck
     */
    public GuiceTestRunner(final Class<?> klass)
            throws InitializationError {
        super(klass);
        this.injector = this.createInjectorFor(this.getModulesFor(klass));
    }

    @Override
    public final Object createTest() throws Exception {
        final Object obj = super.createTest();
        this.injector.injectMembers(obj);
        return obj;
    }

    /**
     * Create a Guice Injector for the class under test.
     * @param classes Guice Modules
     * @return A Guice Injector instance.
     * @throws InitializationError If couldn't instantiate a module.
     */
    private Injector createInjectorFor(final Class<?>[] classes)
            throws InitializationError {
        final List<Module> modules = new ArrayList<>(classes.length);
        for (final Class<?> module : Arrays.asList(classes)) {
            try {
                modules.add((Module) module.newInstance());
            } catch (final ReflectiveOperationException exception) {
                throw new InitializationError(exception);
            }
        }
        return Guice.createInjector(modules);
    }

    /**
     * Get the list of Guice Modules request by GuiceModules annotation in the
     * class under test.
     * @param klass Class under test.
     * @return A Class Array of Guice Modules required by this class.
     * @throws InitializationError If the annotation is not present.
     */
    private Class<?>[] getModulesFor(final Class<?> klass)
            throws InitializationError {
        final GuiceModules annotation = klass.getAnnotation(GuiceModules.class);
        if (annotation == null) {
            final String message = String.format(
                    "Missing @GuiceModules annotation for unit test '%s'",
                    klass.getName()
            );
            throw new InitializationError(message);
        }
        return annotation.value();
    }
}

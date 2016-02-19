package testRunner;

import com.google.inject.AbstractModule;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the Guice Modules in use in the test class.
 *
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GuiceModules {
    /**
     * The Guice Modules classes needed by the class under test.
     */
    Class<? extends AbstractModule>[] value();
}

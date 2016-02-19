package testFramework.aspects;

import com.google.inject.Inject;
import com.google.inject.internal.util.Stopwatch;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Timer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import testFramework.annotations.LogParametersAs;
import testFramework.annotations.LogSteps;
import testFramework.logging.MyLog;

public class LogPageObjectStepsAspect  implements MethodInterceptor {

    @Inject
    private MyLog logger;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();

        final String stepName =
                methodInvocation.getThis().getClass().getSimpleName().split("\\$\\$")[0] + " - " + method.getName();

        Arrays.stream(method.getDeclaredAnnotations())
                .filter(a -> a.annotationType().equals(LogParametersAs.class))
                .map(a -> (LogParametersAs)a)
                .forEach(l -> writeParametersToLog(stepName, methodInvocation, l));

        long startTime = System.nanoTime();
        Object result = methodInvocation.proceed();
        writeToLog(stepName,  (System.nanoTime() - startTime) / 1000000 + " ms elapsed.");

        return result;
    }

    private void writeParametersToLog(String stepName, MethodInvocation invocation, LogParametersAs logParametersAs) {
        Object[] arguments = invocation.getArguments();
        boolean hasSpecificFormat = !("".equals(logParametersAs.logParameterFormat()));

        if (hasSpecificFormat) {
            writeToLog(stepName, logParametersAs.logParameterFormat(), arguments);
            return;
        }

        if (arguments.length == 0 ) {
            return;
        }

        Parameter[] parameters = invocation.getMethod().getParameters();

        for (int i = 0; i < parameters.length; i++) {
            writeToLog(stepName, "\t Argument {} = {}",  parameters[i].getName(), arguments[i]);
        }
    }

    private void writeToLog(String stepName, String format, Object... arguments) {
        logger.logInfo(stepName, format, arguments);
    }
}

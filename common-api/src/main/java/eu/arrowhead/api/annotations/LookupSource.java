package eu.arrowhead.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LookupSource {

    Source value() default Source.ALL;

    enum Source {
        ORCHESTRATION_SERVICE,
        SERVICE_REGISTRY,
        CACHE,
        ALL
    }
}

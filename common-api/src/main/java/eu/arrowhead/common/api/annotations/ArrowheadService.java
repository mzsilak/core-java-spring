package eu.arrowhead.common.api.annotations;

import eu.arrowhead.common.api.ApiMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static eu.arrowhead.common.api.annotations.LookupPolicy.Policy.ON_ERROR;
import static eu.arrowhead.common.api.annotations.LookupSource.Source.ALL;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ArrowheadService {

    String serviceDef();

    ApiMethod method();

    LookupPolicy lookupPolicy() default @LookupPolicy(ON_ERROR);

    LookupSource lookupSource() default @LookupSource(ALL);
}

package eu.arrowhead.common.api.annotations;

import eu.arrowhead.common.api.Protocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ArrowheadApi {

    LookupPolicy.Policy lookupPolicy() default LookupPolicy.Policy.ON_ERROR;

    LookupSource.Source lookupSource() default LookupSource.Source.ALL;

    Protocol[] protocols() default {Protocol.HTTP};

}

package eu.arrowhead.api.transport;

import eu.arrowhead.api.annotations.MetaParam;
import eu.arrowhead.api.annotations.OptionParam;
import eu.arrowhead.api.annotations.UriParam;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

class HttpParameters {

    private final Map<String, String> metaParams = new HashMap<>();
    private final Map<String, Object> optionParams = new HashMap<>();
    private final Map<String, Object> uriParams = new HashMap<>();
    private Object payload;

    HttpParameters() {}

    public static HttpParameters from(final Annotation[][] annotations, final Object[] args) {

        final HttpParameters parameters = new HttpParameters();

        if (annotations.length != args.length) {
            throw new IllegalArgumentException("Annotations do not match arguments size");
        }

        for (int i = 0; i < args.length; i++) {
            Annotation[] objectAnnotations = annotations[i];
            Object object = args[i];

            if (objectAnnotations.length == 0) { // payload annotation can be omitted
                parameters.payload = object;
            } else {
                for (Annotation annotation : objectAnnotations) {

                    if (MetaParam.class.equals(annotation.annotationType())) {
                        add(parameters.metaParams, (MetaParam) annotation, object.toString());
                        break;
                    }
                    if (OptionParam.class.equals(annotation.annotationType())) {
                        add(parameters.optionParams, (OptionParam) annotation, object);
                        break;
                    }
                    if (UriParam.class.equals(annotation.annotationType())) {
                        add(parameters.uriParams, (UriParam) annotation, object);
                        break;
                    }
                }
            }
        }
        return parameters;
    }

    private static void add(final Map<String, String> map, final MetaParam metaParam, final String string) {
        map.put(metaParam.value(), string);
    }

    private static void add(final Map<String, Object> map, final OptionParam optionParam, final Object object) {
        map.put(optionParam.value(), object);
    }

    private static void add(final Map<String, Object> map, final UriParam uriParam, final Object object) {
        map.put(uriParam.value(), object);
    }

    public Object getPayload() {
        return payload;
    }

    public Consumer<HttpHeaders> getHeadersConsumer() {
        return (headers) -> metaParams.forEach(headers::add);
    }

    public UriComponents adaptUri(final UriComponents components) {
        final UriComponentsBuilder builder = UriComponentsBuilder.newInstance().uriComponents(components);

        builder.uriVariables(uriParams);
        optionParams.forEach(builder::queryParam);

        return builder.build();
    }
}

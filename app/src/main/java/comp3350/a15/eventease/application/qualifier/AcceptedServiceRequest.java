package comp3350.a15.eventease.application.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface AcceptedServiceRequest {
    // You can add elements to the annotation if needed
}

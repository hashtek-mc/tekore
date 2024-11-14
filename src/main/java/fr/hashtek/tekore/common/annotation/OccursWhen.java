package fr.hashtek.tekore.common.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * Indicates the condition(s) under which the annotated code is expected to execute.
 * <p>
 * This annotation is used for documentation purposes and can specify the scenarios
 * when certain behavior may occur.
 * <p>
 * It is also used for cases where we except an exception but can ignore it for
 * some reasons, because the exception can be thrown under some circumstances.
 * <p>
 * It must be used in a comment : {@code // @OccursWhen    Some conditions...}
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface OccursWhen {}

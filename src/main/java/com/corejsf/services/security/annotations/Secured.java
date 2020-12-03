/**
 *
 */
package com.corejsf.services.security.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

import com.corejsf.services.security.Role;

@NameBinding
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
/**
 * @author yogeshverma
 *
 */
public @interface Secured {
    Role[] value() default {};
}

package com.github.nort3x.backendchallenge2.configuration.perms

import org.springframework.security.access.prepost.PreAuthorize
import java.lang.annotation.Inherited

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@PreAuthorize("isAnonymous()")
@Inherited
@MustBeDocumented
annotation  class OnlyAnonymous {
}
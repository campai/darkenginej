package mna.thedarkenginej.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

@Documented
@Nonnull
@TypeQualifierDefault({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonnullByDefault {
}

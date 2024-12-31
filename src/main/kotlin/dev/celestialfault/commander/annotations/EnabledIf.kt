package dev.celestialfault.commander.annotations

import org.jetbrains.annotations.ApiStatus.Internal
import java.util.function.Supplier
import kotlin.reflect.KClass

/**
 * Only register the current [Command] or [Group] if the provided [predicate] succeeds;
 * the provided class **must** be an `object` class
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@MustBeDocumented
public annotation class EnabledIf(val predicate: KClass<out Supplier<Boolean>>)

@Internal
public fun EnabledIf.get() = predicate.objectInstance!!.get()

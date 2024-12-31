package dev.celestialfault.commander.annotations

import com.mojang.brigadier.suggestion.SuggestionProvider
import net.minecraft.command.CommandSource
import kotlin.reflect.KClass

/**
 * Attach a [SuggestionProvider] to a provided [String] argument
 *
 * The [provider] **must** be an `object` class
 */
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Suggestions(val provider: KClass<out SuggestionProvider<out CommandSource>>)

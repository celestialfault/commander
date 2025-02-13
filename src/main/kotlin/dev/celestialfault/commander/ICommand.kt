package dev.celestialfault.commander

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KFunction

/**
 * Generic command interface
 *
 * **You should not be directly implementing this!** Breaking changes may occur to this interface with no warning
 * or any mention in changelogs.
 *
 * Instead, inherit from the abstract implementation classes.
 *
 * @see AbstractCommand
 * @see AbstractCommandGroup
 */
@ApiStatus.NonExtendable
public interface ICommand<S : CommandSource> {
	public val name: String
	public val aliases: List<String>
	public val enabled: Boolean

	public fun create(name: String, commander: Commander<S>): LiteralArgumentBuilder<S>
	public fun execute(ctx: CommandContext<S>): Int

	public fun createChild(function: KFunction<*>): AbstractCommand<S>
	public fun createChildGroup(instance: Any): AbstractCommandGroup<S>

	public fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<S, T>
	public fun literal(name: String): LiteralArgumentBuilder<S>
}

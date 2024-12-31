package dev.celestialfault.commander

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import kotlin.reflect.KParameter

/**
 * Light wrapper around Brigadier's [ArgumentType]
 */
public interface ArgumentHandler<T, S : CommandSource> {
	public fun argument(parameter: KParameter): ArgumentType<T>
	public fun parse(ctx: CommandContext<S>, name: String): T
}

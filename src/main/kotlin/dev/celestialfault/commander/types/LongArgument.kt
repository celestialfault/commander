package dev.celestialfault.commander.types

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.celestialfault.commander.ArgumentHandler
import dev.celestialfault.commander.annotations.AllowedRange
import net.minecraft.command.CommandSource
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

public class LongArgument<S : CommandSource> : ArgumentHandler<Long, S> {
	override fun argument(parameter: KParameter): ArgumentType<Long> {
		parameter.type.findAnnotation<AllowedRange.Long>()?.let {
			return LongArgumentType.longArg(it.min, it.max)
		}
		return LongArgumentType.longArg()
	}

	override fun parse(ctx: CommandContext<S>, name: String): Long =
		LongArgumentType.getLong(ctx, name)
}

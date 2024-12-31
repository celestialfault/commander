package dev.celestialfault.commander.types

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.celestialfault.commander.ArgumentHandler
import dev.celestialfault.commander.annotations.AllowedRange
import net.minecraft.command.CommandSource
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

public object LongArgument : ArgumentHandler<Long, CommandSource> {
	override fun argument(parameter: KParameter): ArgumentType<Long> {
		parameter.findAnnotation<AllowedRange.Long>()?.let {
			return LongArgumentType.longArg(it.min, it.max)
		}
		return LongArgumentType.longArg()
	}

	override fun parse(ctx: CommandContext<CommandSource>, name: String): Long =
		LongArgumentType.getLong(ctx, name)
}

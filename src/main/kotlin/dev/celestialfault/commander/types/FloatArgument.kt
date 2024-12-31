package dev.celestialfault.commander.types

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.celestialfault.commander.ArgumentHandler
import dev.celestialfault.commander.annotations.AllowedRange
import net.minecraft.command.CommandSource
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

public object FloatArgument : ArgumentHandler<Float, CommandSource> {
	override fun argument(parameter: KParameter): ArgumentType<Float> {
		parameter.type.findAnnotation<AllowedRange.Float>()?.let {
			return FloatArgumentType.floatArg(it.min, it.max)
		}
		return FloatArgumentType.floatArg()
	}

	override fun parse(ctx: CommandContext<CommandSource>, name: String): Float =
		FloatArgumentType.getFloat(ctx, name)
}

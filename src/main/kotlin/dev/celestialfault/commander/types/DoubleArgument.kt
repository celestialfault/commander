package dev.celestialfault.commander.types

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.celestialfault.commander.ArgumentHandler
import dev.celestialfault.commander.annotations.AllowedRange
import net.minecraft.command.CommandSource
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

public object DoubleArgument : ArgumentHandler<Double, CommandSource> {
	override fun argument(parameter: KParameter): ArgumentType<Double> {
		parameter.findAnnotation<AllowedRange.Double>()?.let {
			return DoubleArgumentType.doubleArg(it.min, it.max)
		}
		return DoubleArgumentType.doubleArg()
	}

	override fun parse(ctx: CommandContext<CommandSource>, name: String): Double =
		DoubleArgumentType.getDouble(ctx, name)
}
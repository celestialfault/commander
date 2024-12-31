package dev.celestialfault.commander.types

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.celestialfault.commander.ArgumentHandler
import dev.celestialfault.commander.annotations.AllowedRange
import net.minecraft.command.CommandSource
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

public object IntArgument : ArgumentHandler<Int, CommandSource> {
	override fun argument(parameter: KParameter): ArgumentType<Int> {
		parameter.findAnnotation<AllowedRange.Int>()?.let {
			return IntegerArgumentType.integer(it.min, it.max)
		}
		return IntegerArgumentType.integer()
	}

	override fun parse(ctx: CommandContext<CommandSource>, name: String): Int = IntegerArgumentType.getInteger(ctx, name)
}

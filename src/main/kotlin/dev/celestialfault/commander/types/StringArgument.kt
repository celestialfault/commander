package dev.celestialfault.commander.types

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.celestialfault.commander.ArgumentHandler
import dev.celestialfault.commander.annotations.Greedy
import net.minecraft.command.CommandSource
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation

public class StringArgument<S : CommandSource> : ArgumentHandler<String, S> {
	override fun argument(parameter: KParameter): ArgumentType<String> {
		if(parameter.type.hasAnnotation<Greedy>()) {
			return StringArgumentType.greedyString()
		}
		return StringArgumentType.string()
	}

	override fun parse(ctx: CommandContext<S>, name: String): String = StringArgumentType.getString(ctx, name)
}

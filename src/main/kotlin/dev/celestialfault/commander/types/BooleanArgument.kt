package dev.celestialfault.commander.types

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.celestialfault.commander.ArgumentHandler
import net.minecraft.command.CommandSource
import kotlin.reflect.KParameter

public class BooleanArgument<S : CommandSource> : ArgumentHandler<Boolean, S> {
	override fun argument(parameter: KParameter): ArgumentType<Boolean> = BoolArgumentType.bool()
	override fun parse(ctx: CommandContext<S>, name: String): Boolean = BoolArgumentType.getBool(ctx, name)
}

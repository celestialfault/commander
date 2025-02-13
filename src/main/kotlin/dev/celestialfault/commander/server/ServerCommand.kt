package dev.celestialfault.commander.server

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.celestialfault.commander.AbstractCommand
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import kotlin.reflect.KFunction

public open class ServerCommand(function: KFunction<*>, instance: Any?) : AbstractCommand<ServerCommandSource>(function, instance) {
	override fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<ServerCommandSource, T> =
		CommandManager.argument(name, type)

	override fun literal(name: String): LiteralArgumentBuilder<ServerCommandSource> =
		CommandManager.literal(name)
}

package dev.celestialfault.commander.client

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.celestialfault.commander.AbstractCommand
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import kotlin.reflect.KFunction

@Environment(EnvType.CLIENT)
public open class ClientCommand(function: KFunction<*>, instance: Any?) : AbstractCommand<FabricClientCommandSource>(function, instance) {
	override fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<FabricClientCommandSource, T> =
		ClientCommandManager.argument(name, type)

	override fun literal(name: String): LiteralArgumentBuilder<FabricClientCommandSource> =
		ClientCommandManager.literal(name)
}

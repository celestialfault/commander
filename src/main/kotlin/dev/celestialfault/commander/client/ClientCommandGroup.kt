package dev.celestialfault.commander.client

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.celestialfault.commander.AbstractCommand
import dev.celestialfault.commander.AbstractCommandGroup
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import kotlin.reflect.KFunction

@Environment(EnvType.CLIENT)
public open class ClientCommandGroup(instance: Any) : AbstractCommandGroup<FabricClientCommandSource>(instance) {
	override fun createChild(function: KFunction<*>): AbstractCommand<FabricClientCommandSource> =
		ClientCommand(function, instance)

	override fun createChildGroup(instance: Any): AbstractCommandGroup<FabricClientCommandSource> =
		ClientCommandGroup(instance)

	override fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<FabricClientCommandSource, T> =
		ClientCommandManager.argument(name, type)

	override fun literal(name: String): LiteralArgumentBuilder<FabricClientCommandSource> =
		ClientCommandManager.literal(name)
}

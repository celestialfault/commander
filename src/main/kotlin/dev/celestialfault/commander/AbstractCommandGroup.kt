package dev.celestialfault.commander

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.celestialfault.commander.AbstractCommand.Companion.isCommand
import dev.celestialfault.commander.annotations.EnabledIf
import dev.celestialfault.commander.annotations.Group
import dev.celestialfault.commander.annotations.get
import net.minecraft.command.CommandSource
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

/**
 * Command source-agnostic command group implementation
 *
 * @see dev.celestialfault.commander.client.ClientCommandGroup
 * @see dev.celestialfault.commander.server.ServerCommandGroup
 */
public abstract class AbstractCommandGroup<S : CommandSource>(protected val instance: Any) : ICommand<S> {
	protected val children: List<ICommand<S>> by lazy {
		buildList {
			instance::class.memberFunctions
				.filter { it.isCommand() }
				.map(::createChild)
				.also { require(it.count { it.isRoot } <= 1) { "The provided object has more than one @RootCommand" } }
				.also { addAll(it) }
			instance::class.nestedClasses
				.filter { it.hasAnnotation<Group>() }
				.map { it.objectInstance!! }
				.map(::createChildGroup)
				.also { addAll(it) }
			instance::class.memberProperties
				.filter { it.returnType.isSubclassOf(ICommand::class) }
				.map {
					@Suppress("UNCHECKED_CAST")
					it.getter.call(instance) as ICommand<S>
				}
				.also { addAll(it) }
		}
	}

	protected val root: AbstractCommand<S>?
		get() = children
			.firstOrNull { (it as? AbstractCommand)?.isRoot == true }
			?.let { it as AbstractCommand }
			?.takeIf { it.enabled }

	override val name: String = instance::class.findAnnotation<Group>()?.name?.takeIf { it.isNotBlank() } ?: instance::class.simpleName!!.lowercase()
	override val aliases: List<String> = instance::class.findAnnotation<Group>()?.aliases?.toList() ?: emptyList()
	override val enabled: Boolean
		get() = instance::class.findAnnotation<EnabledIf>()?.get() != false

	override fun create(name: String): LiteralArgumentBuilder<S> {
		val builder = literal(name)
		children
			.filter { it != root }
			.filter { it.enabled }
			.forEach {
				val names = listOf(it.name, *it.aliases.toTypedArray())
				names.forEach { name -> builder.then(it.create(name)) }
			}
		root?.takeIf { it.enabled }?.build(builder)
		return builder
	}

	override fun execute(ctx: CommandContext<S>): Int {
		val root = this.root
		if(root == null) throw UnsupportedOperationException("This group does not have a root command")
		return root.execute(ctx)
	}
}

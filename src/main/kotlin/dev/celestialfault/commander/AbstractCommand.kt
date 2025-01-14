package dev.celestialfault.commander

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.celestialfault.commander.annotations.Command
import dev.celestialfault.commander.annotations.EnabledIf
import dev.celestialfault.commander.annotations.EnabledIf.Companion.get
import dev.celestialfault.commander.annotations.RootCommand
import dev.celestialfault.commander.annotations.Suggests
import dev.celestialfault.commander.mixin.CommandContextAccessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.minecraft.command.CommandSource
import java.lang.reflect.InvocationTargetException
import kotlin.collections.contains
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.extensionReceiverParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.valueParameters

/**
 * Command source-agnostic command implementation
 *
 * @see dev.celestialfault.commander.client.ClientCommand
 * @see dev.celestialfault.commander.server.ServerCommand
 */
public abstract class AbstractCommand<S : CommandSource>(protected val function: KFunction<*>, protected val instance: Any?) : ICommand<S> {
	init {
		require(function.isCommand()) {
			"The provided function must be annotated with either @Command or @RootCommand"
		}
		require(function.hasAnnotation<Command>() != function.hasAnnotation<RootCommand>()) {
			"@Command and @RootCommand are mutually exclusive"
		}
		require(function.valueParameters.none { it.isVararg }) {
			"Commands must not have vararg parameters"
		}
		require(function.extensionReceiverParameter == null) {
			"Commands must not be extension methods"
		}
	}

	private val commandAnnotation = function.findAnnotation<Command>()
	private val contextParam by lazy { function.valueParameters.firstOrNull { it.type.isSubclassOf(CommandContext::class) } }
	private val instanceParam by lazy { function.instanceParameter }

	@Suppress("UNCHECKED_CAST")
	private val params: List<KParameter> by lazy {
		function.valueParameters.filter { it != contextParam }
	}
	private lateinit var commander: Commander<S>

	private val handledParams: Map<KParameter, ArgumentHandler<*, S>> get() =
		params.associate { it to commander.types.getHandler(it) }

	override val name: String = commandAnnotation?.name?.takeIf { it.isNotBlank() } ?: function.name.lowercase()
	override val aliases: List<String> = commandAnnotation?.aliases?.toList() ?: emptyList()
	override val enabled: Boolean
		get() = function.findAnnotation<EnabledIf>()?.get() != false

	internal val isRoot: Boolean = function.hasAnnotation<RootCommand>()

	internal fun build(builder: LiteralArgumentBuilder<S>, commander: Commander<S>) {
		parameterSanityCheck(params)

		if(!this::commander.isInitialized) {
			this.commander = commander
		}
		require(commander === this.commander) { "Commands cannot be reused with a different Commander instance" }

		val params = handledParams

		if(params.keys.all { it.isOptional }) builder.executes(this::execute)
		if(params.isEmpty()) return

		// if we're at this point then we know we have arguments of some kind
		if(function.findAnnotation<RootCommand>()?.iKnowWhatImDoingAddingArgumentsToARootCommandNowPleaseBeQuiet == false) {
			Commander.LOGGER.warn("@RootCommand annotated function {} has command arguments; this is strongly discouraged", function)
		}

		var tree: ArgumentBuilder<S, *>? = null
		var last: KParameter? = null
		params.entries.toList().asReversed().forEach { (param, handler) ->
			val arg = argument(param.name!!, handler.argument(param))
			param.type.findAnnotation<Suggests>()?.let {
				@Suppress("UNCHECKED_CAST")
				arg.suggests(it.provider.objectInstance!! as SuggestionProvider<S>)
			}
			if(tree == null || last?.isOptional == true) {
				arg.executes(this::execute)
			}

			tree?.let { arg.then(tree) }
			last = param
			tree = arg
		}
		builder.then(tree)
	}

	override fun create(name: String, commander: Commander<S>): LiteralArgumentBuilder<S> =
		literal(name).also { build(it, commander) }

	override fun execute(ctx: CommandContext<S>): Int {
		val args = buildMap<KParameter, Any?> {
			instanceParam?.let { put(it, instance) }
			contextParam?.let { put(it, ctx) }

			val commandArgs = (ctx as CommandContextAccessor).arguments
			for((param, handler) in handledParams) {
				val name = param.name ?: continue
				if(param.isOptional && name !in commandArgs) continue
				put(param, handler.parse(ctx, name))
			}
		}

		if(function.isSuspend) {
			runAsync {
				try {
					function.callSuspendBy(args)
				} catch(e: Throwable) {
					onError(if(e is InvocationTargetException) e.targetException else e)
				}
			}
		} else {
			try {
				function.callBy(args)
			} catch(e: Throwable) {
				onError(if(e is InvocationTargetException) e.targetException else e)
			}
		}

		return 0
	}

	/**
	 * Called when an error is caught from a command function; you may want to override this to do
	 * special error handling.
	 *
	 * Note that you should take care to not swallow [com.mojang.brigadier.exceptions.CommandSyntaxException],
	 * as the game will typically send a better error message if its thrown.
	 *
	 * By default, this method simply re-`throw`s the error provided.
	 */
	protected open fun onError(error: Throwable) {
		throw error
	}

	final override fun createChild(function: KFunction<*>): AbstractCommand<S> = throw UnsupportedOperationException()
	final override fun createChildGroup(instance: Any): AbstractCommandGroup<S> = throw UnsupportedOperationException()

	/**
	 * Used to wrap `suspend fun` functions in a coroutine; you may want to override this
	 * to use your [CoroutineScope] instead.
	 */
	protected open fun runAsync(runnable: suspend (CoroutineScope) -> Unit): Job = Commander.coroutineScope.launch(block = runnable)

	internal companion object {
		fun KFunction<*>.isCommand(): Boolean = hasAnnotation<Command>() || hasAnnotation<RootCommand>()
	}
}

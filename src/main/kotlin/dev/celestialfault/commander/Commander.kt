package dev.celestialfault.commander

import com.mojang.brigadier.CommandDispatcher
import com.mojang.logging.LogUtils
import dev.celestialfault.commander.types.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import net.minecraft.command.CommandSource
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

/**
 * Utility methods for using Commander. This is also where you register any custom [ArgumentHandler]s.
 */
@Suppress("UNCHECKED_CAST")
public class Commander<S : CommandSource> {
	/**
	 * Map of [KType]s to their associated [ArgumentHandler]; the type should be a [starProjectedType].
	 *
	 * @see addHandler
	 */
	public val types: MutableMap<KType, ArgumentHandler<*, S>> = mutableMapOf()

	init {
		addHandler(String::class, StringArgument())
		addHandler(Int::class, IntArgument())
		addHandler(Long::class, LongArgument())
		addHandler(Double::class, DoubleArgument())
		addHandler(Float::class, FloatArgument())
		addHandler(Boolean::class, BooleanArgument())
	}

	/**
	 * Type-safe utility method for registering a given [ArgumentHandler]
	 *
	 * ## Example
	 *
	 * ```kt
	 * class MyTypeHandler : TypeHandler<Type, ServerCommandSource> {
	 *     override fun argument(parameter: KParameter): ArgumentType<T> = TypeArgumentType.type()
	 *     override fun parse(ctx: CommandContext<S>, name: String): T = TypeArgumentType.getType(ctx, name)
	 * }
	 *
	 * commander.addHandler(Type::class, MyTypeHandler())
	 * ```
	 */
	public fun <T : Any> addHandler(cls: KClass<T>, handler: ArgumentHandler<T, S>) {
		types.put(cls.starProjectedType, handler)
	}

	/**
	 * Utility method to register a root [ICommand] using the provided [dispatcher]
	 */
	public fun register(root: ICommand<S>, dispatcher: CommandDispatcher<S>) {
		if(!root.enabled) return
		val names = listOf(root.name, *root.aliases.toTypedArray())
		names.forEach { dispatcher.register(root.create(it, this)) }
	}

	internal companion object {
		internal val LOGGER = LogUtils.getLogger()
		private val supervisorJob = SupervisorJob()
		private val errorHandler = CoroutineExceptionHandler { ctx, error ->
			LOGGER.error("Encountered unhandled error in coroutine", error)
		}
		internal val coroutineScope = CoroutineScope(CoroutineName("commander") + supervisorJob + errorHandler)
	}
}

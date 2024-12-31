package dev.celestialfault.commander

import com.mojang.brigadier.CommandDispatcher
import com.mojang.logging.LogUtils
import dev.celestialfault.commander.types.*
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
public object Commander {
	internal val LOGGER = LogUtils.getLogger()

	/**
	 * Map of [KType]s to their associated [ArgumentHandler]; the type should be a [starProjectedType].
	 *
	 * @see register
	 */
	public val TYPES: MutableMap<KType, ArgumentHandler<*, *>> = mutableMapOf()

	init {
		register(String::class, StringArgument)
		register(Int::class, IntArgument)
		register(Long::class, LongArgument)
		register(Double::class, DoubleArgument)
		register(Float::class, FloatArgument)
		register(Boolean::class, BooleanArgument)
	}

	/**
	 * Type-safe utility method for registering a given [ArgumentHandler]
	 */
	public fun <T : Any, S : CommandSource> register(cls: KClass<T>, handler: ArgumentHandler<T, S>) {
		TYPES.put(cls.starProjectedType, handler)
	}

	private val supervisorJob = SupervisorJob()
	internal val coroutineScope = CoroutineScope(CoroutineName("commander") + supervisorJob)

	/**
	 * Utility method to register a root [ICommand] using the provided [dispatcher]
	 */
	public fun <S : CommandSource> register(root: ICommand<S>, dispatcher: CommandDispatcher<S>) {
		if(!root.enabled) return
		val names = listOf(root.name, *root.aliases.toTypedArray())
		names.forEach { dispatcher.register(root.create(it)) }
	}
}

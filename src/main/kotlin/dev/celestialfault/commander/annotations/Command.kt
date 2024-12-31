package dev.celestialfault.commander.annotations

/**
 * Annotate a function in a class with this method to make it a command usable in-game.
 *
 * Using [RootCommand] alongside this annotation will result in an error at runtime.
 *
 * The annotated function may be a `suspend fun`, and will automatically be wrapped in an async context
 * as necessary.
 *
 * ## Example
 *
 * ```kt
 * // ctx can be omitted if you don't use it for anything
 * @Command
 * fun hello(ctx: Context, name: String) {
 *     ctx.source.sendFeedback(Text.literal("Hello, $name!"))
 * }
 * ```
 *
 * @param name The name used for the generated command; by default, this is the lowercased function name
 * @param aliases Optional aliases to also register this command under
 */
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
public annotation class Command(val name: String = "", vararg val aliases: String)

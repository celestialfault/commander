package dev.celestialfault.commander.annotations

/**
 * Use on a [String] argument to make it greedy (consume the remainder of the command input)
 *
 * This **must** be the final parameter of the command it's used on
 *
 * ## Example
 *
 * ```kt
 * fun command(ctx: Context, string: @Greedy String) {
 *     // ...
 * }
 * ```
 */
@Target(AnnotationTarget.TYPE)
public annotation class Greedy

package dev.celestialfault.commander.annotations

public class AllowedRange private constructor() {
	/**
	 * Used on [Int] arguments to control the allowed range
	 *
	 * ## Example
	 *
	 * ```kt
	 * fun command(ctx: Context, number: @AllowedRange.Int(1, 100) Int) {
	 *     // ...
	 * }
	 * ```
	 */
	@Target(AnnotationTarget.TYPE)
	@Retention(AnnotationRetention.RUNTIME)
	public annotation class Int(val min: kotlin.Int = kotlin.Int.MIN_VALUE, val max: kotlin.Int = kotlin.Int.MAX_VALUE)

	/**
	 * Used on [Long] arguments to control the allowed range
	 *
	 * ## Example
	 *
	 * ```kt
	 * fun command(ctx: Context, number: @AllowedRange.Long(1, 100) Long) {
	 *     // ...
	 * }
	 * ```
	 */
	@Target(AnnotationTarget.TYPE)
	@Retention(AnnotationRetention.RUNTIME)
	public annotation class Long(val min: kotlin.Long = kotlin.Long.MIN_VALUE, val max: kotlin.Long = kotlin.Long.MAX_VALUE)

	/**
	 * Used on [Double] arguments to control the allowed range
	 *
	 * ## Example
	 *
	 * ```kt
	 * fun command(ctx: Context, number: @AllowedRange.Double(1, 100) Double) {
	 *     // ...
	 * }
	 * ```
	 */
	@Target(AnnotationTarget.TYPE)
	@Retention(AnnotationRetention.RUNTIME)
	// Double.MIN_VALUE refers to the smallest possible *positive* value
	public annotation class Double(val min: kotlin.Double = -kotlin.Double.MAX_VALUE, val max: kotlin.Double = kotlin.Double.MAX_VALUE)

	/**
	 * Used on [Float] arguments to control the allowed range
	 *
	 * ## Example
	 *
	 * ```kt
	 * fun command(ctx: Context, number: @AllowedRange.Float(1, 100) Float) {
	 *     // ...
	 * }
	 * ```
	 */
	@Target(AnnotationTarget.TYPE)
	@Retention(AnnotationRetention.RUNTIME)
	// Float.MIN_VALUE refers to the smallest possible *positive* value
	public annotation class Float(val min: kotlin.Float = -kotlin.Float.MAX_VALUE, val max: kotlin.Float = kotlin.Float.MAX_VALUE)
}

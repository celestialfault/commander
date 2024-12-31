package dev.celestialfault.commander.annotations

/**
 * Attach to **at most one** command function in a [Group] to make it process the root command
 * for the given group.
 *
 * Adding this annotation to more than one method in a [Group] will result in an error being thrown.
 *
 * Using [Command] alongside this annotation will also result in an error at runtime.
 *
 * Note that while arguments are allowed in root commands, doing so is **strongly** discouraged,
 * and will result in a warning message being logged at runtime.
 *
 * You should only add arguments if you know full well the implications of doing so, and are willing
 * to work around any issues that may arise **on your own**.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class RootCommand(val iKnowWhatImDoingAddingArgumentsToARootCommandNowPleaseBeQuiet: Boolean = false)

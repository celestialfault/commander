# Commander

This is a Kotlin library which does the tedious work of building commands for you from function definitions.

This project is built against Minecraft 1.21.4, but *should* work on the vast majority of versions that use Brigadier.

## Usage

Add this to your `build.gradle(.kts)`:

```groovy
repositories {
	maven("https://maven.celestialfault.dev/releases")
}

dependencies {
	include(modImplementation("dev.celestialfault:commander:1.0.1"))
}
```

Then, add your commands as such:

```kotlin
// ... imports ...

@Group("group")
object Group {
	// note that while @RootCommand supports arguments, this is heavily discouraged unless you know
	// exactly what doing so entails and are willing to work around any issues that arise on your own
	@RootCommand
	fun root(ctx: CommandContext<ServerCommandSource>) {
		ctx.source.sendFeedback(Text.literal("Hi!"))
	}

	// run as '/group hello <name>'
	@Command
	fun hello(ctx: CommandContext<ServerCommandSource>, name: String) {
		ctx.source.sendFeedback(Text.literal("Hello, $name!"))
	}
}

// wherever you register commands:

Commander.register(ServerCommandGroup(Group), dispatcher)
```

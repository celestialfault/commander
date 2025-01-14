# Commander

This is a Fabric library mod written in Kotlin which does the tedious work of building Brigadier commands for you
from basic function definitions.

This project is built against Minecraft 1.21.4, but *should* work on the vast majority of versions that use Brigadier.

## Usage

Add this to your `build.gradle(.kts)`:

[![commander version badge](https://maven.celestialfault.dev/api/badge/latest/releases/dev/celestialfault/commander?color=40c14a&name=commander)](https://maven.celestialfault.dev/)

```groovy
repositories {
	maven { url = "https://maven.celestialfault.dev/releases" }
}

dependencies {
	include(modImplementation("dev.celestialfault:commander:${project.commander_version}"))
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

val commander = Commander<ServerCommandSource>()
commander.register(ServerCommandGroup(Group), dispatcher)
// you can also register an individual root command like so
commander.register(ServerCommand(Group::hello, Group), dispatcher)
// there also exists ClientCommand & ClientCommandGroup; note that these depend on Fabric API

// Commander ships with basic primitive types already supported; you will need to manually add support
// for other argument types with an ArgumentHandler (see the documentation of Commander#addHandler for examples).
```

## Current Limitations

This library does not support the complex command structures that Brigadier allows for; one notable limitation is that
you may not have required arguments after optional arguments. If any required arguments are specified after an optional
argument, the command will throw an error at runtime.

The target use case of this library is anything that can be represented with simple `object` class and
function definitions; anything more complex (e.g. `/execute`) is out of scope.

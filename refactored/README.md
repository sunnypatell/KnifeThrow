# Knife Throw (Legacy class-per-file archive)

This module is a preservation copy of Sunny Patel's original Knife Throw
submission from four years ago – a 3,700+ line Swing game that predates
ChatGPT and the wave of AI coding assistants. The goal of this archive is to
keep the source exactly as it shipped in 2022 while presenting it in a
package-per-class layout that is easier to explore, document, and maintain
alongside the untouched `src/knifeThrow.java` monolith.

Every inner class from the legacy file was extracted wholesale (including the
comments that chronicle the original development journey) and wrapped with
lightweight JavaDoc so new contributors can immediately understand each file's
role without altering any runtime behaviour.

## Layout

* `com.knifethrow.game.knifeThrow` – entry point, shared constants, helper
  utilities, and loader routines.
* `com.knifethrow.game.tile` – base physics object used by every entity.
* `com.knifethrow.game.knife` – player and enemy knife logic copied verbatim
  from the inner `knife` class.
* `com.knifethrow.game.target` – target behaviour and scoring rules.
* `com.knifethrow.game.player` – player rendering, animation, and movement
  logic.
* `com.knifethrow.game.particles` – particle effects (with the nested
  `particle` type).
* `com.knifethrow.game.panel` – main game panel containing the update loop and
  collision code.
* `com.knifethrow.game.startGame` – start menu, player select, and knife shop
  screens.

In total the package weighs in at roughly 4,400 lines of code – a near
one-to-one mirror of the original single file – so you retain the same feel,
quirks, and gameplay polish that went into the 2022 build.

## Building and running

Compile every class in the package and launch the `knifeThrow` class. The
refactored output reuses the root assets (`sprites`, `sounds`, `data.txt`,
etc.), so no additional setup is required:

```bash
javac $(find refactored/src/main/java/com/knifethrow/game -name "*.java")
java -cp refactored/src/main/java com.knifethrow.game.knifeThrow
```

## Verification checklist

* Each extracted file is byte-for-byte identical to the matching inner class
  body from `src/knifeThrow.java`; only the surrounding `static` modifier and
  indentation changed to satisfy the compiler.
* Class-level JavaDoc now documents the purpose of each legacy class without
  touching the gameplay logic.
* `src/knifeThrow.java` itself remains untouched so you can always fall back to
  the 2022 monolithic source if desired.

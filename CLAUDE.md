# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

**T.E.B.L. Slashblades** (modId `tcpl`) — a Minecraft 1.20.1 Forge mod for the [SlashBlade](https://github.com/TartaricAcid/SlashBlade) ecosystem. It is a proper Forge **Java mod** (Gradle + ForgeGradle, `modLoader = "javafml"`), but its content is **purely data-driven**: the Java side (`src/main/java`) is only an empty `@Mod("tcpl")` entrypoint (`com.tebl.tcpl.TCPL`) that registers nothing. All gameplay content is JSON + PNG in `src/main/resources`, defining 14 named blades themed on "最后战线" (the end battle line).

Targets Forge **1.20.1-47.4.21**, official Mojang mappings, Java 17, Gradle wrapper 8.1.1.

## Commands

The Gradle wrapper (`./gradlew` / `gradlew.bat`) is used for everything — there is no global Gradle dependency. On this machine the wrapper's `gradle-wrapper.properties` points at the Tencent mirror (`mirrors.cloud.tencent.com`) because `services.gradle.org` redirects the download to GitHub, which is unreachable directly on this network (GFW). A system-installed Gradle 8.8 (`gradle build`) also works if the wrapper distribution is unavailable.

- `./gradlew build` — compile Java, reobfuscate, and package the mod jar to `build/libs/tcpl-0.3.0.jar`.
- `./gradlew runClient` — launch the Minecraft client with this mod. **Runtime-only mods (see below) must be in `run/mods/` first.**
- `./gradlew runServer` — launch a dedicated server (`--nogui`).
- `./gradlew runData` — run data generation (writes to `src/generated/resources/`; currently unused, the pack is hand-written).
- `./gradlew clean` — wipe `build/` and `run/`.

First build downloads the full Gradle 8.1.1 + Forge toolchain (~500 MB) and takes several minutes.

## Runtime dependencies (not needed to compile)

The mod's recipes reference items from other mods, but none are required to build. To actually play with the blades, install into a Forge 47.4+ instance (or the `run/` folder for dev runs):

- **Mandatory**: `slashblade` (≥1.1.5), `slashblade_addon` (≥1.1.6)
- **Optional but used by some recipes**: `mekanism` (≥10.4), `srelic` (≥3.0)

Drop the jars into `run/mods/` for `runClient`/`runServer`, or into the `mods/` folder of a normal instance. Recipes referencing missing optional mods' items simply do not craft.

## Architecture

The mod's data lives under `src/main/resources/`. Every blade is defined across **four layers that must agree on the same snake_case name** (all under namespace `tcpl`):

1. **Blade definition** — `src/main/resources/data/tcpl/slashblade/named_blades/<name>.json` — stats and rendering. Key fields: `name` (`tcpl:<name>`), `enchantments` (standard set: `minecraft:power` 7–10, `looting` 3, `fire_aspect` 3, `unbreaking` 10), `properties` (`attack_base`, `max_damage`, optional `slash_art`, `sword_type` e.g. `["bewitched"]`), `render` (`model`, `texture`, `summon_sword_color` as a decimal int).
2. **Recipe** — `src/main/resources/data/tcpl/recipes/<name>.json` — a `slashblade:shaped_blade` recipe. `category: "equipment"`, pattern + key, `result: {item: "slashblade:slashblade"}`, `show_notification: true`.
3. **Texture** — `src/main/resources/assets/tcpl/model/named/<name>.png` (the `sange/` subfolder holds the 三日月-series textures, e.g. `death_blade.png`).
4. **Localization** — `item.tcpl.<name>` must be added to **both** `src/main/resources/assets/tcpl/lang/zh_cn.json` and `en_us.json` (names use § color codes).

`src/main/resources/META-INF/mods.toml` is the mod manifest: modId `tcpl`, version `0.3.0`, authors, and dependency ranges (kept in sync with `gradle.properties`). `src/main/resources/pack.mcmeta` documents the pack format (pack_format 15 / server data pack format 12 = MC 1.20.1).

## Recipe conventions

- **Ingredient kinds**: plain items (`slashblade:proudsoul_trapezohedron`, `proudsoul_ingot`, `proudsoul_crystal`, minecraft items, etc.) and `type: "slashblade:blade"` entries that require a *specific evolved blade* via `request: {kill, refine, name}` (e.g. `slashblade_addon:moonlight_cherry`, `slashblade:sange`, `tcpl:death_blade`).
- **Upgrade chain**: several recipes consume another `tcpl:` blade as an ingredient (e.g. `daybreak_celestial` requires `tcpl:daybreak` at kill 4000/refine 20; `evil_157` requires `tcpl:death_blade` + `tcpl:death_image` + `slashblade:sange`). A new blade's recipe must have its prerequisites craftable.
- **Optional-mod items**: recipes freely reference `mekanism:alloy_atomic`, `mekanism:ultimate_control_circuit`, `srelic:srelic_slashblade`, etc. even though those mods are optional dependencies — those recipes silently do nothing without the mod installed.

## Adding a new blade

Create the four files above with the same base name (blade def, recipe, PNG texture, both lang entries). .obj models come from the base mods — reuse an existing one (`slashblade:model/blade.obj`, `slashblade:model/named/sange/sange.obj`, `slashblade_addon:model/blademaster/blademaster.obj`) rather than adding new ones. Then run `./gradlew build` and check the jar.

## Git note

Running git from this repo on this machine may fail with "detected dubious ownership". Use `git -c safe.directory='F:/备份区/minecraft/工作区/TEBLSlashBlades' <cmd>`, or add that path to `safe.directory` in git config. Commit messages are conventionally in Chinese.

# Mob Arena Extension

Extend the functionality of Mob Arena by using pre-coded extensions, or code one yourself with provided API.

## Getting Started
* Download the plugin from [releases](https://github.com/Wertik/MobArenaExtension/releases)
* Install the plugin (into your /plugins/ folder of course)
* Enable wanted extension in [config.yml](https://github.com/Wertik/MobArenaExtension/blob/master/src/main/resources/config.yml)

## Supported Plugins

### MythicMobs

Mythic mobs can be spawned in arena by using their internal MythicMobs name in MobArena/[waves.yml](https://github.com/garbagemule/MobArena/blob/master/src/main/resources/res/waves.yml) monsters section.

### PlaceholderAPI

Key | description
------------|-------------
%mobarena_total_enabled% | amount of arenas is enabled
%mobarena_arena_name% | name of arena that player is in
%mobarena_arena_wave% | current wave number of the arena
%mobarena_arena_wave_final% | the final wave number of the arena 
%mobarena_arena_mobs% | amount of mobs alive in arena
%mobarena_arena_statistic_(statName)% | player statistic fetched from Mob Arena

Available statistics:
* `kills` -- amount of mob kills the player has
* `dmgDone` -- amount of damage the player has done
* `dmgTaken` -- amount of damage the player has taken
* `swings` -- I have no idea, try it out
* `hits` -- amount of hits? I guess?
* `lastWave` -- don't know

`DiscordSRV`

If in chat isolated arena, messages wont be sent to discord.

## Other extensions

### Commands

Send commands on different Arena events.
Examples in [config.yml](https://github.com/Wertik/MobArenaExtension/blob/master/src/main/resources/config.yml)

## Known Issues
* MythicMobs allows using some non-living entities (armor stand for ex.), which MobArena does not allow.
* Adding, renaming or removing mobs in MythicMobs wont get synced into MobArena, you should restart the server.
* Using similar mythic mob name is not compatible, like `Hero brine`, `hero-brine`, `Hero brines`.
* This is a bug from MythicMob itself, when mob A spawn a minion B --> B has parent that is A. This is intended behaviour, but when using skill `Summon` without radius or with radius = 0, A wont be B's parent. Always use summon radius higher than 0 if you plan to use that in MobArena.

## Extension API

Extend [Extension](https://github.com/Wertik/MobArenaExtension/blob/master/src/main/java/me/sait/mobarena/extension/extensions/Extension.java), implement required methods and register it somewhere using ``YourExtension().register()``.

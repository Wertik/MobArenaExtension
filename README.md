# Mob Arena Extension  [![Build status](https://ci.appveyor.com/api/projects/status/npoql7iisagunifw/branch/master?svg=true)](https://ci.appveyor.com/project/SaitDev/mobarenaextension/branch/master)

Extend the functionality of Mob Arena by using pre-coded extensions, or code one yourself with provided API.

## Getting Started
* Download the plugin from [releases](https://github.com/Wertik/MobArenaExtension/releases)
* Install the plugin (into your /plugins/ folder of course)
* Enable wanted extension in [config.yml](https://github.com/Wertik/MobArenaExtension/blob/master/src/main/resources/config.yml)

### Supported Plugins

`MythicMobs`
* Install MythicMobs. Then create cool mobs ([docs](https://mythicmobs.net/manual/) on their offical website)
* Restart your server (see [Known issues](#Known-Issues))
* Use mythic mob's name in arena waves config

`PlaceholderAPI`

Placeholders:

Key | description
------------|-------------
mobarena_total_enabled | amount of arenas is enabled
mobarena_arena_name | name of arena that player is in
mobarena_arena_wave | current wave number of the arena
mobarena_arena_wave_final | the final wave number of the arena 
mobarena_arena_mobs | amount of mobs alive in arena
mobarena_statistic_(statName) | player statistic fetched from Mob Arena

Available statistics:
* `kills` -- amount of mob kills the player has
* `dmgDone` -- amount of damage the player has done
* `dmgTaken` -- amount of damage the player has taken
* `swings` -- I have no idea, try it out
* `hits` -- amount of hits? I guess?
* `lastWave` -- don't know

`DiscordSRV`

If in chat isolated arena, messages wont be sent to discord.

### Other extensions

`Commands`

Send commands on different Arena events.
Examples in [config.yml](https://github.com/Wertik/MobArenaExtension/blob/master/src/main/resources/config.yml)

### Known Issues
* MythicMobs allow using some non-living entity (armor stand) but MobArena only allow living entity. Which mean you can not use non-living entity mythic mob in MobArena `yet`
* Adding new mob, rename or remove mob in MythicMobs wont get sync to MobArena, you should restart server
* Using similar mythic mob name is not compatible, like `Hero brine`, `hero-brine`, `Hero brines`
* This is a bug from MythicMob itself, when mob A spawn a minion B --> B has parent that is A. This is intended behaviour, but when using skill `Summon` without radius or with radius = 0, A wont be B's parent. Always use summon radius higher than 0 if you plan to use that in MobArena

3 first issues will need to wait for the next MobArena [major patch](https://github.com/garbagemule/MobArena/projects/5)

## Extension API

Extend [Extension](https://github.com/Wertik/MobArenaExtension/blob/master/src/main/java/me/sait/mobarena/extension/extensions/Extension.java), implement required methods and register it somewhere using ``YourExtension().register()``.

## License
The [license](/LICENSE) does not apply to files inside the /lib folder.

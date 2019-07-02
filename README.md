# PingAPI [<img src="https://img.shields.io/badge/minecraft-1.14.2-important.svg">](README.md) [<img src="https://img.shields.io/badge/craftbukkit-1.7.5%20--%201.14.2-critical.svg">](README.md) [<img src="https://img.shields.io/badge/version-1.3.3-blue.svg">](README.md) [<img src="https://img.shields.io/badge/license-MIT-blue.svg">](https://github.com/henry-anderson/PingAPI/blob/master/LICENSE) [<img src="http://badge.henrya.org/spigotbukkit/downloads.php?spigot=3829&bukkit=89296">](https://www.spigotmc.org/resources/pingapi.3829/)

[<img src="https://i.imgur.com/CZm3X3M.gif">](README.md)

PingAPI gives Bukkit developers more control over how they reply to ping requests. Simply put it provides an improvement to Bukkit's ServerListPingEvent. You can download it [here](http://www.spigotmc.org/resources/pingapi.3829/).

It has been a few years since I have worked on this project, but I decided to update it for newer versions of CraftBukkit and keep backwards compatibility. I will try to keep it maintained and up to date from now on. The most important change to note is that creating animations is no longer possible for servers running newer versions of CraftBukkit.

[Plugins Using PingAPI](plugins.md)

## Features [<img src="https://img.shields.io/badge/animations-1.7.5%20--%201.8.3-blueviolet.svg">](README.md)
- Modify the online player count and the max players
- Change the player sample (list of players shown when hovering over the player count)
- Fake the protocol name and version to display text instead of the player count
- Create server list animations (1.8.3 or before)

## Javadocs
[The Javadocs can be found here](http://henry-anderson.github.io/PingAPI/)

## Usage
- [Creating a basic listener](usage/listener.md)
- [Changing the player count](usage/player_count.md)
- [Changing the player count to text](usage/player_count_to_text.md)
- [Changing the player sample (hover message)](usage/player_sample.md)
- [Creating animations](usage/animations.md)

## How It Works
If you aren't interested in using PingAPI but are curious about how it works, it is documented [here](https://github.com/henry-anderson/PingAPI/tree/master/PingAPI/src/org/henrya/pingapi#pingapi).

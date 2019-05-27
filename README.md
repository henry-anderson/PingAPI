# PingAPI <img src="https://img.shields.io/badge/minecraft-1.14.1-important.svg"> <img src="https://img.shields.io/badge/spigot-1.7.5%20--%201.14.1-critical.svg"> <img src="https://img.shields.io/badge/release-v1.1.1-blue.svg"> <a href="https://github.com/henry-anderson/PingAPI/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-MIT-blue.svg"></a> ![Spiget Downloads](https://img.shields.io/spiget/downloads/3829.svg?color=blue)

<img src="http://i.imgur.com/vIWvOUv.gif"></img>

PingAPI gives Bukkit developers more control over how they reply to ping requests. You can download it <a href="http://www.spigotmc.org/resources/pingapi.3829/">here</a>.  

It has been a few years since I have worked on this project, but I decided to update it for newer versions of CraftBukkit and keep backwards compatibility. I will try to keep it maintained and up to date from now on. The most important change to note is that creating animations is no longer possible for servers running newer versions of CraftBukkit.

## Features
- Modify the online player count and the max players
- Change the player sample (list of players shown when hovering over the player count)
- Fake the protocol name and version to display text instead of the player count
- Hide the player count
- Create server list animations (1.8 or before)

## Javadocs
<a href="http://henry-anderson.github.io/PingAPI/">The Javadocs can be found here</a>

## Usage
- [Creating a basic listener](usage/listener.md)
- [Changing the player count](usage/player_count.md)
- [Changing the player count to text](usage/player_count_to_text.md)
- [Changing the player sample (hover message)](usage/player_sample.md)
- [Creating animations](usage/animations.md)

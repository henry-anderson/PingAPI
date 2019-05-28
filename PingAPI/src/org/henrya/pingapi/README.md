# PingAPI
If you aren't interested in using PingAPI this document will go over how I created it. The API itself is simple and easy to use, but what happens behind the scenes is more complicated.

## NMS
You can tell by looking above that there are a number of different packages with version numbers. I had to dig into NMS itself to listen for and modify outgoing packets. NMS classes are version dependent because the package changes for each major update. [This](https://bukkit.org/threads/safeguarding-against-unchecked-and-potentially-damaging-plugins.116749/) thread explains why that was decided upon.

### Abstraction
I made use of abstraction so that I could handle the changes from each version of CraftBukkit accordingly. NMS classes are obfuscated so there are small changes that occur from version to version such as the method "a()" being changed to "c()." That is the reason I included a package for every major CraftBukkit update. I used reflection to instantiate the appropriate classes for the version of CraftBukkit that is being run and downcasted to the abstract class I created for the API.

## Packet Sniffing
In order to listen for outgoing packets I created a class called PingInjector. This class gets the list of NetworkManager instances and injects a subclass of ChannelDuplexHandler I created into each NetworkManager's pipeline. In my DuplexHandler class I overrode the write() method which sends the packet to the client. This way I was able to check whether the packet being sent was a PacketStatusOutServerInfo packet and modify the data appropriately

# jb-twitch-chat

![Build](https://github.com/avvie/jb-twitch-chat/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)


<!-- Plugin description -->
A plugin to show twitch chat in intellij and rider.
Its for personal use for the time being. 

It currently is NOT handling its http server for OAuth lifecycle well or at all. Use with expected risks
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "jb-twitch-chat"</kbd> >
  <kbd>Install</kbd>

## Usage
- Create a twitch app and grab the twitch ClientId for your app
- Add the twitch channel name and your  client id to the plugin settings
  (they can be found <kbd>Settings/Other Settings/Twitch Chat Plugin</kbd>)
  

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation

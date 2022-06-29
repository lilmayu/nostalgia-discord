# Nostalgia Discord
I don't really know what to write here, but I've made this CraftBukkit plugin for #saveminecraft Discord ([invite link](https://discord.gg/saveminecraft)) Nostalgia Survival 1.6.4 Minecraft server.

The code is... ugly. I was kinda in hurry, and I was not thinking about making it pretty or well-structured - but it works well, I hope.

## Features
- Sending Minecraft server chat into Discord channel
- Sending player joins/quits/deaths into Discord channel
- Sending Console output into Discord channel
- Executing commands from Discord on Minecraft server
- Sending Discord messages into Minecraft server chat

## Configuration
There is a config file, which is pretty simple. 

`debug` -> Determines if `[DEBUG]` log should be printed
`discord`
 - `bot-token` -> Bot token to log into
 - `text-channel-id` -> Text channel which will be used to send chat, player joins/quits/deaths into and read Discord messages to send into Minecraft chat (can be setup by Discord command `/channel`)
 - `console-channel-id` -> Text channel which will be used to send console output every 10 seconds (if any) (can be setupo by Discord command `/channel-console`)

## Commands
### Minecraft
There are no Minecraft commands at this time.

### Discord
`/ping` -> Pong!
`/channel [channel]` -> Sets specified Text Channel (or current channel, if not specified) as channel which will be used for chat, player joins/quits/deaths, etc. forwarding and reading (requires ADMINISTRATOR permission)
`/channel-console [channel]` -> Sets specified Text Channel (or current channel, if not specified) as channel which will be used for console output (requires ADMINISTRATOR permission)
`/console-log` -> Sends last 10 logs
`/minecraft-command <command>` -> Executes specified command on server as console. Slash (/) is not needed same like typing commands into console. For example: `/minecraft-command say Hello, World!` executes `say Hello, World!` on server as console


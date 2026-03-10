# **🏷️ AdvancedTags**
A modern, **high-performance tag management system** for Minecraft servers. Built with a focus on aesthetics and performance, it offers a premium experience for both players and administrators.

## 🌐 Multi-Language Support
AdvancedTags comes with built-in support for **28 different languages**, allowing your community to interact with the plugin in their native tongue:

**🇺🇸 English (en) | 🇹🇷 Turkish (tr) | 🇩🇪 German (de) | 🇪🇸 Spanish (es) | 🇷🇺 Russian (ru) | 🇨🇳 Chinese (zh) | 🇯🇵 Japanese (ja) | 🇦🇿 Azerbaijani (az) | 🇫🇷 French (fr) | 🇸🇦 Arabic (ar) | 🇳🇱 Dutch (nl) | 🇮🇩 Indonesian (id) | 🇦🇲 Armenian (hy) | 🇮🇹 Italian (it) | 🏴󠁧󠁢󠁳󠁣󠁴󠁿 Scottish Gaelic (gd) | 🇸🇪 Swedish (sv) | 🇰🇬 Kyrgyz (ky) | 🇰🇷 Korean (ko) | 🇭🇺 Hungarian (hu) | 🇨🇿 Czech (cs) | 🇬🇷 Greek (el) | 🇮🇷 Persian (fa) | 🇵🇱 Polish (pl) | 🇷🇴 Romanian (ro) | 🇻🇳 Vietnamese (vi) | 🇵🇹 Portuguese (pt) | 🇹🇭 Thai (th) | 🇺🇦 Ukrainian (uk)**

The language can be easily selected from the **config file**, and all translation files are located in: `/plugins/AdvancedTags/lang`

## 🚀 Features

- **Modern Visuals (MiniMessage)**
  - Full support for **RGB colors, HEX codes, and Gradients.**
  - Create eye-catching titles like `<gradient:#FFD700:#FFA500>KING</gradient>`
- **Smart GUI System**
  - **Infinite Pagination:** Automatically divides your tags into pages.
  - **Selection Glow:** Active titles have a magical enchantment glow in the menu.
  - **Reset Button:** Players can easily clear their current tag with a dedicated button.
- **Performance & Stability**
  - **Folia Ready:** 100% compatible with multi-threaded server environments.
  - **Fully Asynchronous:** Zero impact on server TPS.
  - **JSON Storage:** Fast and reliable player data management.
- **PlaceholderAPI Integration**
  - Display tags anywhere (Chat, Tab, Scoreboards) using `%advancedtags_unvan%`.

## 🛠️ Configuration
The `config.yml` file allows you to customize every aspect of the plugin, from menu layouts to individual tag permissions.
### 🌍 General Settings
- **lang:** Set your preferred language (e.g., `en`,`tr`, `de`). The plugin automatically generates all 28 language files in the `/lang` folder.
- **cooldown:** Defines the wait time (in seconds) between tag changes to prevent server spam. Administrators with `advancedtags.bypass.cooldown` are unaffected. 
![settings](https://cdn.modrinth.com/data/cached_images/147692ca84fed58b9a27ba4e498b0bc9dde4c8c7_0.webp)
### 📦 Menu Customization 
You have full control over the Tag Selection GUI:
- **size:** Define the inventory size (must be a multiple of 9: `9, 18, 27, 36, 45, 54`).
- **title:** Supports full **MiniMessage** for beautiful gradients and HEX colors.
- **fill_material:** Custom item to fill empty slots in the menu.
- **Buttons:** Fully customizable materials and display names for "Next Page", "Previous Page", and "Reset Tag" buttons.
![menu_settings](https://cdn.modrinth.com/data/cached_images/440b8d47bedc28e9352f8a6ca691b13103bab234.jpeg)
![menu_settings](https://cdn.modrinth.com/data/cached_images/6ffb8b8833c64d45d01c8148b5cd228d9ed43367.jpeg)

### 🔑 Creating Tags & Permissions 
The system is dynamic. The ID you give to a tag in the config automatically determines its permission node.

```
tags:
  tag1: # ID: tag1
    display_name: "<#FF7F50>[✈]"
    material: PAPER
    lore:
      - "&7For beginners."
      - "&7Everyone can select."
```
> **Permission:** To use the tag above, give the player: `advancedtags.tag.tag1`
- **Visuals:** You can use HEX (`<#RRGGBB>`), Gradients (`<gradient:color1:color2>`), and traditional color codes (`&6`).
- **Locked State:** If a player doesn't have permission, the item automatically switches to the `locked` material (default: Gray Dye) defined in your config. 
![locket_state](https://cdn.modrinth.com/data/cached_images/ad56510f9d8245d816650c2ad7d0a83d7a2c8a3e.jpeg)

## ⚡ Commands

| Command | Permission | Description | Aliases Usage |
|---------|------------|-------------|---------------|
| `/tags` | advancedtags.command | Opens the tag selection menu | `/unvan` `/ünvan` |
| `/advancedtags reload` | advancedtags.admin | Reloads config and lang files | `/at reload` |

## 🛡️ Permissions

| Permission                  | Description                                           |
|------------------------------|------------------------------------------------------|
| `advancedtags.command`       | Permission to open the menu (Available to everyone). |
| `advancedtags.admin`         | Permission to use the reload command (OP) and bypass the cooldown. |
| `advancedtags.bypass.cooldown` | Permission to bypass the cooldown.               |
| `advancedtags.tag.<id>`      | Permission to access a specific tag (e.g., `advancedtags.tag.tag1`). |
| `advancedtags.tag.*`         | Wildcard permission granting access to all tags.   |

## ⚙️ Supported Forks

| Fork / Build | Support Status  |
|--------------|----------------|
| ✅ Paper     | Fully Supported |
| ✅ Purpur    | Fully Supported |
| ✅ Folia     | Fully Supported |
| ❌ Spigot    | Not Supported |
| ❌ Bukkit    | Not Supported |

- Optimized for **modern server software**. Fully compatible with **Paper**, **Folia**, and all their fork (**Purpur**, **Pufferfish**, etc.).
- 
## 🧩 Placeholders
Make sure that **PlaceholderAPI** is installed to use the **AdvancedTags** plugin.
- `%advancedtags_unvan%` - Returns the player's active tag (with colors).

## ⚖️ License
Licensed under CC BY-NC 4.0 (Attribution-NonCommercial 4.0 International).
Commercial use and resale are strictly prohibited

[![Discord](https://cdn.modrinth.com/data/cached_images/4de86371cc7bcf3818924b198f31baacc304700f.png)
](https://discord.gg/62TzJBpm6C)

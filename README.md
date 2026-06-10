# Text Styles

**"Bring your text to life with dynamic animations and effects!"**

**An advanced, modular, and highly personalizable text effect system for Minecraft.**

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/6NmpSmZ-qAE" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>

## 📖 Description

**Text Styles** is a powerful refactor and expansion of the original text effects concept. It introduces a robust, registry-based system that allows for deeply personalizable text animations using simple tags. Whether you're writing in a book, on a sign, or just chatting, you can now add movement, color cycles, and unique reveal animations to your text.

Unlike other simple effect mods, **Text Styles** is designed for compatibility and extensibility, allowing other mods to register their own styles and parameters easily.

## 🌟 Features

- **Registry-Based System**: Every style is a registered object, making it easy to extend and maintain.
- **Deeply Personalizable**: Almost every aspect of an effect (speed, distance, frequency, etc.) can be adjusted via tag parameters.
- **Short Aliases**: Support for short tag names and parameter aliases (e.g., `<wb d=5>` instead of `<wobble distance=5>`), perfect for space-constrained areas like books and signs.
- **Minecraft Native Integration**: Styles are serialized using Codecs, ensuring they work perfectly with Minecraft's text system, NBT, and JSON.
- **Rich Animation Library**: Over 15 built-in styles including Wave, Rainbow, Glitch, Typewriter, and more.
- **Per-Glyph Rendering**: Advanced rendering that allows for smooth gradients and per-character animations.

## ⚙️ Compatibility

- **Minecraft Versions**:
  - Fully compatible with **26+**.
- **Modding Platforms**:
  - **Fabric**: ✅ Fully supported!
  - **NeoForge**: Soon!
- **Mod Compatibility**:
  - Works with any mod that uses standard Minecraft Components.

## 📦 Installation

1. **Download the mod**: Grab the latest version from [GitHub](https://github.com/RazorPlay01/Text-Styles) or your favorite mod hosting site.
2. **Install Fabric or NeoForge**: Ensure you have Fabric Loader or Neoforge Loader installed for your Minecraft version.
3. **Add the mod**: Place the `.jar` file into the `mods` folder of your Minecraft instance.
4. **Launch the game**: Start Minecraft and start experimenting with styles!

## 💡 How to Use

Simply wrap your text in tags like `<style_name params>your text</style_name>`. You can use full names or short aliases.

### ✅ Vanilla Formatting Support

You can now use **all official Minecraft formatting codes** inside the `< >` system:

#### **Colors**
- `<red>`, `<blue>`, `<gold>`, `<green>`, `<aqua>`, `<yellow>`, `<white>`, etc.
- `<dark_red>`, `<dark_blue>`, `<light_purple>`, etc.
- Legacy codes: `<§c>`, `<§4>`, `<§e>`, `<§l>`, etc.
- **Hex colors**: `<#ff0000>`, `<#00ffaa>`, `<#ffffff>`

#### **Styles**
- `<bold>` or `<b>` → **Negrita**
- `<italic>` or `<i>` → *Cursiva*
- `<underline>` or `<u>` → Subrayado
- `<strikethrough>` or `<s>` → Tachado
- `<obfuscated>` or `<k>` → Ofuscado
- `<reset>` or `<r>` → Resetear formato

### 🛠️ Available Styles & Parameters

| Style | Alias | Parameters (Full / Short) | Description |
| :--- | :--- | :--- | :--- |
| **Wobble** | `wb` | `distance`/`d`, `speed`/`s` | Vertical bobbing motion. |
| **Shake** | `sh` | `x`, `y`, `frequency`/`f` | Random jittery movement. |
| **Wave** | `wv` | `amplitude`/`a`, `frequency`/`f`, `speed`/`s`, `vertical`/`v` | Fluid wave-like motion. |
| **Rainbow** | `rb` | `speed`/`s`, `frequency`/`f`, `saturation`/`sat`, `brightness`/`bri` | Cycles through HSB colors. |
| **Bounce** | `bn` | `amplitude`/`a`, `speed`/`s`, `delay`/`dl` | Jumping/bouncing effect. |
| **Pulse** | `pl` | `intensity`/`i`, `speed`/`s`, `delay`/`dl`, `color`/`c` | Pulses in scale and color. |
| **Fade** | `fd` | `min_alpha`/`m`, `max_alpha`/`x`, `speed`/`s`, `delay`/`dl` | Fades in and out. |
| **Swing** | `sw` | `amplitude`/`a`, `speed`/`s`, `delay`/`dl` | Pendulum-like rotation. |
| **Glitch** | `gl` | `intensity`/`i`, `frequency`/`f` | Random offsets and color flashes. |
| **Turbulence** | `tb` | `intensity`/`i`, `speed`/`s` | Erratic but smooth movement. |
| **Gradient** | `gr` | `color1`/`c1`, `color2`/`c2`, `speed`/`s`, `frequency`/`f` | Smooth transition between two colors. |
| **Typewriter** | `tw` | `delay`/`dl`, `land`/`l`, `snap`/`sn` | Reveals characters one by one. |
| **Marquee** | `mq` | `min`/`m`, `max`/`x`, `vertical`/`v` | Scrolling marquee effect. |
| **Shadow** | `sd` | `color`/`c`, `x`, `y` | Custom offset shadow. |
| **Show After** | `sa` | `delay`/`dl` | Hides text for a set duration. |

### 🔍 Showcase Command

You can see all these effects in action in-game by running:
`/textstyles showcase`

## 🛠️ Contributing & Feedback

Contributions, ideas, and bug reports are always welcome!
- Found a bug? Report it on our [GitHub](https://github.com/RazorPlay01/Text-Styles/issues).
- Want to contribute? Feel free to fork the project and submit a pull request.

## 📄 License

This mod is licensed under the **MIT License**.

## ❤️ Acknowledgments

**Text Styles** was created by **[RazorPlay01]** to bring more expressiveness to Minecraft's text. Thank you for using the mod! If you enjoy it, please consider leaving a star on the repository!
Thanks also to the **Text Effects** team for allowing me to maintain the MIT license

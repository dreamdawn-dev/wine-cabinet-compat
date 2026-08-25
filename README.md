# Wine Cabinet Compat

[![License: GPL-3.0](https://img.shields.io/badge/License-GPL--3.0-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green.svg)](https://www.minecraft.net)

一个基于标签驱动的兼容性模组，让你能把不同模组的酒瓶互相放入对方的酒柜、酒架和展示架中，不再被单一模组的酒瓶生态所限制。

## 支持模组

| 模组 | 是否必需 |
|------|---------|
| [Kaleidoscope Tavern](https://www.curseforge.com/minecraft/mc-mods/kaleidoscope-tavern) | 否（可选） |
| [Vinery](https://www.curseforge.com/minecraft/mc-mods/lets-do-vinery) | 否（可选） |
| [Youkai's Feasts](https://modrinth.com/mod/gensokyo-delight-youkais-feasts) | 否（可选） |

本模组支持以上模组的任意组合——只装一个、两个或全部三个都可以正常工作。

## 功能特性

- **跨模组酒瓶放置**——把 Vinery 的酒瓶放进 Kaleidoscope Tavern 的酒柜，把 Youkai's Homecoming 的酒瓶放进 Vinery 的酒架，任何组合皆可
- **标签驱动**——兼容性通过物品标签控制，你（或整合包作者）可以轻松添加自定义酒瓶，无需改动代码
- **空手取酒**——采用 Vinery 风格的交互方式：空手右键取出酒瓶，手持酒瓶右键放入(森罗酒馆的取放逻辑被替换)
- **智能渲染**——其他模组的酒瓶在酒柜和展示架中能正确渲染，大小和位置都经过适配
- **安全 Mixin 加载**——只有当目标模组存在时，对应的 Mixin 才会生效，缺少某个模组不会导致崩溃

## 标签说明

本模组提供三个物品标签来控制兼容性：

| 标签 | 用途 |
|------|------|
| `dreamdawn:small_bottle_wine` | 标记为小酒瓶的物品 |
| `dreamdawn:large_bottle_wine` | 标记为大酒瓶的物品 |
| `dreamdawn:wine_bottle` | 右键方块时禁止默认放置行为的物品，建议给加上上述两个标签之一的物品也加上这个标签（保证和原模组的酒瓶放置逻辑和表现一致，防止出现诸如以下的问题：1、玩家对着酒柜背部也能放进酒柜；2、将酒放进酒柜时，虽能成功放置，但是会闪烁一下） |

默认情况下这些标签为空——你需要通过数据包将物品添加到对应标签中来启用兼容性。

## 从源码构建

### 为什么需要依赖 JAR？

本模组使用 Mixin 直接引用了目标模组的类（如 `AbstractStorageBlock`、`WineBottleBlock` 等），Java 编译器在编译时必须能解析这些类才能生成 `.class` 文件和 Mixin refmap。因此**必须下载对应模组的 JAR 文件才能编译**。

这些 JAR 仅用于编译（`compileOnly`），**不会被打包进最终产物**，也不会被提交到 Git 仓库。

### 构建步骤

1. 克隆仓库：
   ```bash
   git clone https://github.com/dreamdawn-dev/wine-cabinet-compat.git
   cd wine-cabinet-compat
   ```

2. 下载以下依赖模组的 JAR 文件，放入 `libs/` 目录：

   | 文件名 | 来源模组 |
   |--------|---------|
   | `kaleidoscope_tavern.jar` | Kaleidoscope Tavern |
   | `vinery.jar` | Vinery |
   | `youkaisfeasts.jar` | Youkai's Feasts |
   | `youkaishomecoming.jar` | Youkai's Homecoming（`youkaisfeasts` 的前置依赖） |
   | `l2library.jar` | L2 Library（Kaleidoscope Tavern 的前置依赖） |
   | `l2serial.jar` | L2 Serial（`l2library` 的前置依赖） |
   | `l2modularblock.jar` | L2 Modular Block（`l2library` 的前置依赖） |

3. 构建：
   ```bash
   ./gradlew build
   ```

   构建产物位于 `build/libs/wine-cabinet-compat-1.0.0.jar`。

## 许可证

本项目基于 **GNU General Public License v3.0** 开源协议发布，详见 [LICENSE](LICENSE) 文件。
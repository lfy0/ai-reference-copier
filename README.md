# AI Reference Copier

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

面向 IntelliJ Platform IDE 的轻量插件，IDE与AI工具协作时复制AI可理解的路径，提供给AI做代码定位。

## 功能

- 编辑器中复制当前行或所选代码范围。
- Project 视图中复制单个文件或文件夹。
- 兼容 PyCharm 2023.2+、Android Studio 2025.1+ 和 DevEco Studio 6.0 的动作上下文。
- 默认使用相对于 IDE 项目根目录的路径，也可在设置中切换为绝对路径，统一使用 `/` 分隔符。
- 根据 IDE 界面语言自动显示中文或英文。
- 支持自定义代码、文件、文件夹三类模板。
- 不读取代码正文、不联网、不调用 AI 服务。

```text
@src/Main.kt:12-20
@src/config.xml
@src/example/
```

在 **Settings/Preferences > AI Reference Copier** 中选择路径类型，代码、文件和文件夹引用会使用同一设置。选择绝对路径后，输出示例为 `@C:/workspace/project/src/Main.kt:12-20`。设置页可预览三类引用，也支持恢复默认模板和相对路径设置。

## 构建

需要 JDK 17 和 Gradle 8.x：

```shell
gradle test
gradle buildPlugin
```

如需使用本地安装的 IDE 作为构建目标，可通过 `localIdePath` 指定其安装目录：

```shell
gradle buildPlugin -PlocalIdePath="<IDE_INSTALLATION_PATH>"
```

ZIP 直接生成在 `build/`，通过 **Settings > Plugins > Install Plugin from Disk** 安装。


## License

Copyright 2026 lfy0. Licensed under the [Apache License 2.0](LICENSE).

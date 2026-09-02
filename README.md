# AI Reference Copier

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

面向 IntelliJ Platform IDE 的轻量插件，用于复制可直接粘贴给 Codex 等 AI 工具的绝对路径和代码行号。

## 功能

- 编辑器中复制当前行或所选代码范围。
- Project 视图中复制单个文件或文件夹。
- 兼容 PyCharm 2023.2+、Android Studio 2025.1+ 和 DevEco Studio 6.0 的动作上下文。
- 使用 `/` 分隔的绝对路径，方便 Codex 直接定位本机文件。
- 根据 IDE 界面语言自动显示中文或英文。
- 支持自定义代码、文件、文件夹三类模板。
- 不读取代码正文、不联网、不调用 AI 服务。

```text
@C:/workspace/project/src/Main.kt:12-20
@C:/workspace/project/src/config.xml
@C:/workspace/project/src/example/
```

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

项目维护者可参考 [JetBrains Marketplace 发布指南](MARKETPLACE.md) 构建并发布新版本。

## License

Copyright 2026 lfy0. Licensed under the [Apache License 2.0](LICENSE).

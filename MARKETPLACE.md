# JetBrains Marketplace 发布指南

本文档供项目维护者发布 AI Reference Copier 使用。

## 发布前检查

- 更新 `build.gradle.kts` 中的插件版本号。
- 更新 `plugin.xml` 中的 `<change-notes>`，确保内容与当前版本一致。
- 确认英文插件说明位于其他语言说明之前。
- 确认发布者信息、插件图标和 Apache License 2.0 保持有效。
- 运行自动化测试和 JetBrains 插件配置校验。
- 使用 Plugin Verifier 检查计划支持的 JetBrains IDE 版本。
- 在目标 IDE 中安装最终 ZIP，验证菜单、快捷键和剪贴板功能。

## 构建发布包

项目需要 JDK 17 和 Gradle 8.x：

```shell
gradle clean test buildPlugin verifyPluginConfiguration
```

生成的安装包位于：

```text
build/ai-reference-copier-<version>.zip
```

## 首次发布

JetBrains Marketplace 要求首次发布通过网页手动完成：

1. 登录 <https://plugins.jetbrains.com/>，在账户菜单中选择 **Upload plugin**。
2. 接受 Marketplace Developer Agreement，并选择对应的 Vendor Profile。
3. 上传构建生成的 ZIP 安装包。
4. 选择 **Apache 2.0** 许可证并填写公开源码仓库地址。
5. 设置合适的标签和发布渠道，然后提交审核。

正式版本使用默认发布渠道。Alpha、Beta 或 EAP 版本应使用对应的自定义渠道。

## 发布后续版本

插件条目创建后，可在 Marketplace 的 **My Tokens** 页面生成 Personal Access Token，并通过 Gradle 发布更新：

```powershell
$env:ORG_GRADLE_PROJECT_intellijPlatformPublishingToken = "YOUR_TOKEN"
gradle publishPlugin
```

Marketplace 不接受相同版本号的重复安装包，因此每次发布前都必须更新版本号。

## 安全注意事项

Personal Access Token、签名私钥、证书密码及其他凭据只能通过环境变量或安全的密钥存储提供，不得提交到源码仓库。

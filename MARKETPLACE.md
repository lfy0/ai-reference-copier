# JetBrains Marketplace 发布清单

## 首次上传前必须补充

- [x] 发布者为 `lfy0`，联系邮箱为 `453742431@qq.com`，项目地址为 <https://github.com/lfy0/ai-reference-copier>。
- [x] 使用 Apache License 2.0，并在项目根目录提供完整的 `LICENSE`。
- [ ] 在 PyCharm、Android Studio 和 DevEco Studio 中安装最终 ZIP，完成一次菜单、快捷键和剪贴板回归测试。
- [ ] 使用 JetBrains Plugin Verifier 检查准备在 Marketplace 声明支持的 JetBrains IDE 版本。
- [ ] 如需作者签名，使用环境变量或安全的密钥存储提供证书和私钥，不能提交到源码仓库。

## 首次手动上传

1. 登录 <https://plugins.jetbrains.com/>，在账户菜单选择 **Upload plugin**。
2. 接受 Marketplace Developer Agreement，创建 Vendor Profile，并声明 Trader 或 Non-trader 状态。
3. 上传 `build/ai-reference-copier-0.5.3.zip`。
4. 填写许可证、源码地址、标签和发布渠道。正式版使用默认渠道；预发布版可以使用 `beta`。
5. 提交审核，并根据 Marketplace 的验证与人工审核反馈进行修改。

## 后续版本自动发布

首次版本必须手动上传。插件条目创建后，在 Marketplace 的 **My Tokens** 页面生成 Personal Access Token，然后执行：

```powershell
$env:ORG_GRADLE_PROJECT_intellijPlatformPublishingToken = "YOUR_TOKEN"
gradle publishPlugin
```

每次上传前必须修改 `build.gradle.kts` 中的版本号，并同步更新 `plugin.xml` 的 `<change-notes>`。Token、签名私钥和密码不能写入项目文件。

上传前确认 GitHub 仓库可以在未登录状态下访问，然后在 Marketplace 表单中选择 **Apache 2.0** 并填写该仓库地址。

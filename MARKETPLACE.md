# JetBrains Marketplace 发布清单

## 首次上传前必须补充

- [ ] 将 `plugin.xml` 中的 `<vendor>` 改为真实发布者，并填写有效的 `email` 和 `url` 属性。
- [ ] 确定插件许可证。若选择开源许可证，在项目根目录加入对应的 `LICENSE`，并准备可公开访问的源码仓库地址。
- [ ] 在 PyCharm、Android Studio 和 DevEco Studio 中安装最终 ZIP，完成一次菜单、快捷键和剪贴板回归测试。
- [ ] 使用 JetBrains Plugin Verifier 检查准备在 Marketplace 声明支持的 JetBrains IDE 版本。
- [ ] 如需作者签名，使用环境变量或安全的密钥存储提供证书和私钥，不能提交到源码仓库。

`plugin.xml` 中的发布者格式示例：

```xml
<vendor email="your-email@example.com" url="https://your-site.example.com">Your Name</vendor>
```

## 首次手动上传

1. 登录 <https://plugins.jetbrains.com/>，在账户菜单选择 **Upload plugin**。
2. 接受 Marketplace Developer Agreement，创建 Vendor Profile，并声明 Trader 或 Non-trader 状态。
3. 上传 `build/ai-reference-copier-0.5.2.zip`。
4. 填写许可证、源码地址、标签和发布渠道。正式版使用默认渠道；预发布版可以使用 `beta`。
5. 提交审核，并根据 Marketplace 的验证与人工审核反馈进行修改。

## 后续版本自动发布

首次版本必须手动上传。插件条目创建后，在 Marketplace 的 **My Tokens** 页面生成 Personal Access Token，然后执行：

```powershell
$env:ORG_GRADLE_PROJECT_intellijPlatformPublishingToken = "YOUR_TOKEN"
gradle publishPlugin
```

每次上传前必须修改 `build.gradle.kts` 中的版本号，并同步更新 `plugin.xml` 的 `<change-notes>`。Token、签名私钥和密码不能写入项目文件。

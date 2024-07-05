# Webdroid Javascript Interface
![JSAPI version](https://img.shields.io/badge/JSAPI-3-blue)

简体中文


**`[]`内的为可选参数**
```javascript
// 基本
  wd.getVersion()
  // int: 获取内核版本
  wd.toast(String text)
  // void: 弹出2秒提示
  wd.toastLong(String text)
  // void: 弹出3秒提示
  wd.alert([String 标题,]String 内容[, Boolean 能否取消])
  // void: 弹出普通弹窗
  wd.getAssetsUrl()
  // String: 获取assets地址(file:///android_asset/)
  wd.getStorageUrl()
  // String: 返回储存根目录地址(file:///storage/emulated/0/)

// 文件
  wd.getStoragePath()
  // String: 获取储存根目录(/storage/emulated/0)
  wd.isFileExists(String filePath)
  // Boolean: 文件是否存在
  wd.listFile(String dir)
  // String: 列出dir目录下的所有文件(返回JSONArray字符串)

// 软件包
  wd.getPackageName()
  // String: 获取当前应用的包名
  wd.getAppName([String packageName])
  // String: 获取指定应用的名称(packageName为空则本应用)
  wd.getVersionName([String packageName])
  // String: 获取指定应用的版本名(packageName为空则本应用)
  wd.getVersionCode([String packageName])
  // int: 获取指定应用的版本号(packageName为空则本应用)
  wd.isInstalled(String packageName)
  // Boolean: 指定应用是否已安装
  wd.appSettings(String packageName)
  // Boolean: 打开指定应用的详情界面，返回是否能打开

// 系统
  wd.finish()
  // void: 结束当前Activity(假退出)
  wd.finishAndRemoveTask()
  // void: 结束当前Activity并清除任务(退出)
  wd.exit([int exitCode])
  // void: 闪退(exitCode默认为0)
  wd.startActivity(String packageName[, String activityName])
  // Boolean: 启动指定应用(activityName可空)，返回是否启动成功
  wd.requestPermission(String permissionName)
  // void: 申请指定权限
  wd.hasPermission(String permissionName)
  // Boolean: 指定权限是否存在
  wd.isVpnConnected()
  // (实验) Boolean: 是否存在VPN连接

// 网络
  wd.httpGet(String url, String callback, int symbo)
  /**
   * void(async): 向url发送GET请求，请求结束后执行JS:
    当请求成功时:
    callback(String urlencode后的请求结果, int HTTP状态码, int symbo)
    当请求失败时:
    callback(false, 0, int symbo)
   */
```
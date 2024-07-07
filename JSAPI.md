# Webdroid Javascript Interface
![JSAPI version](https://img.shields.io/badge/JSAPI-5-blue)

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
  wd.alert([String 标题,]String 内容[, boolean 能否取消])
  // void: 弹出普通弹窗
  wd.getAssetsUrl()
  // String: 获取assets地址(file:///android_asset/)
  wd.getStorageUrl()
  // String: 返回储存根目录地址(file:///storage/emulated/0/)

// 设置
  wd.setIndexUrl(String url)
  // void: 设置启动url(默认为file:///android_asset/index.html)
  wd.getIndexUrl()
  // String: 获取启动url

// 文件
  wd.getStoragePath()
  // String: 获取储存根目录(/storage/emulated/0)
  wd.isFileExists(String filePath)
  // boolean: 文件是否存在
  wd.listFile(String dir)
  // String: 列出dir目录下的所有文件(返回JSONArray字符串)
  wd.isDir(String filePath)
  // boolean: 文件是否为文件夹

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
  // boolean: 指定应用是否已安装
  wd.appSettings(String packageName)
  // boolean: 打开指定应用的详情界面，返回是否能打开

// 配置
  wd.putString(String key, String value)
  // void: 保存key的值为value
  wd.getString(String key)
  // String: 读取key的值
  wd.putInt(String key, int value)
  // void: 保存key的值为value
  wd.getInt(String key)
  // int: 读取key的值
  wd.putFloat(String key, float value)
  // void: 保存key的值为value
  wd.getFloat(String key)
  // float: 读取key的值
  wd.putLong(String key, long value)
  // void: 保存key的值为value
  wd.getLong(String key)
  // long: 读取key的值
  wd.putBoolean(String key, boolean value)
  // void: 保存key的值为value
  wd.getBoolean(String key)
  // boolean: 读取key的值

// 系统
  wd.finish()
  // void: 结束当前Activity(假退出)
  wd.finishAndRemoveTask()
  // void: 结束当前Activity并清除任务(退出)
  wd.exit([int exitCode])
  // void: 闪退(exitCode默认为0)
  wd.startActivity(String packageName[, String activityName])
  // boolean: 启动指定应用(activityName可空(实验))，返回是否启动成功
  wd.requestPermission(String permissionName)
  // void: 申请指定权限
  wd.hasPermission(String permissionName)
  // boolean: 指定权限是否已拥有
  wd.isVpnConnected()
  // (实验) boolean: 是否存在VPN连接
  wd.isDarkMode()
  // boolean: 是否为深色模式，安卓10以下直接返回false
  wd.regDarkModeChangeEvent(String callback)
  // void(async): 监听深色模式状态变化事件，深色模式执行js: callback(true)，反之执行callback(false)

// 网络
  wd.isNetworkAvailable()
  // boolean: 网络是否连接
  wd.regNetworkChangeEvent(String callback)
  // void(async): 监听网络状态变化事件，网络已连接执行js: callback(true)，反之执行callback(false)
  wd.httpGet(String url, String callback, int symbo)
  /**
   * void(async): 向url发送GET请求，请求结束后执行JS:
    当请求成功时:
    callback(String urlencode后的请求结果, int HTTP状态码, int symbo)
    当请求失败时:
    callback(false, 0, int symbo)
   */
```
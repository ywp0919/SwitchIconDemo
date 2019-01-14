# 遇到的坑
这里我把做这个功能中遇到的一些问题写在前面，是为了大家能先了解有什么问题存在，遇到这些问题的时候就不慌了，这里我把应用图标和名称先统一使用icon代替进行说明。

1、动态替换icon，只能替换内置的icon，无法从服务器端获取来更新icon；

2、动态替换icon以后，应用内更新的时候必须要切换到原始icon），否则可能导致更新安装失败(AS上表现为adb运行会失败)，或者升级后应用图标出现多个甚至应用图标都不显示的情况（这些问题都可以通过下面我推荐的开发规则解决掉，所以这是一个坑点，不是肯定会发生的问题，只不过大多数人会遇到。）；

3、Android系统动态替换app icon会有延迟，在不同的手机系统上刷新icon的时间不一样，大概在10秒左右，在这个时间内点击icon会提示应用未安装(提示可能会有差别，目前我的小米就不会提示任何信息，点了没有反应）；

4、更换icon的代码运行后一会应用就闪退了，或者导致显示中的Dialog和PopupWindow报错崩溃（这个问题和第二个问题有很大的相关性，按我下面给出的规则实行的话是可以解决的。



# 多入口配置
多入口配置，字面意思就是应用程序的多个入口配置，在AndroidManifest.xml中有一个叫activity-alias的标签，这个标签从字面上看就能理解是activity别名的意思，这里我给出一个示例作下相应的说明。

activity-alias例子说明：
```
        <activity-alias
            android:name="NewActivity1"   // 注册这个组件的名字，不需要生成文件
            android:enabled="false"       // 是否显示这个启动项
            android:label="Alias1"        // 名称，也就是对应这个启动项显示在桌面上的app名称
            android:icon="@mipmap/ic_launcher_round"    //图标，也就是对应这个启动项显示在桌面上的app图标
            android:targetActivity=".MainActivity"      //对应的原来的Activity组件，这里路径要跟注册的Activity对应。
            >
            <intent-filter>  // LAUNCHER 启动入口
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity-alias>
```


## 显示多个启动入口
然后这里我先做一个多个启动入口全部显示的app示例，这里需要写的代码都在清单文件中，代码如下：
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.wepon.switchicondemo">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher_round"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">

        <!--原Activity-->
        <activity
            android:enabled="true"
            android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!--别名1-->
        <activity-alias
            android:name="NewActivity1"
            android:enabled="true"
            android:label="Alias1"
            android:icon="@mipmap/ic_launcher_round"
            android:targetActivity=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity-alias>

        <!--别名2-->
        <activity-alias
            android:name="NewActivity2"
            android:enabled="true"
            android:label="Alias2"
            android:icon="@mipmap/ic_launcher"
            android:targetActivity=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity-alias>

    </application>

</manifest>
```
运行后的效果如下：

![](https://user-gold-cdn.xitu.io/2019/1/10/16836d51c1b77260?w=435&h=219&f=jpeg&s=18713)
可以看到桌面上显示了三个图标，进入的都是MainActivity这个页面，图标我用的自动生成的，就懒的去找图标了，效果上能看出来就行。

当然了，实际项目中我们只会显示一个图标，这里我们只需要把"别名1"和"别名2"的android:enabled="true"改为"false"就行了，这样就只显示一个图标了，就不放效果图了。

## 代码控制切换不同的应用图标显示
马上春节了，我们产品说到哪个时间点我们的应用图标就要换成春节用的图标了，当然，前面说了这些图标要先在应用写好，不是通过服务器动态拿的，而是应用内已经写好的。那这个时候我们就需要通过代码进行应用图标的动态切换了，这里我给出Demo里面布局如图：

![](https://user-gold-cdn.xitu.io/2019/1/10/16836dab820c7fe1?w=436&h=404&f=jpeg&s=22412)

这里三个按钮点击后切换到相应的应用图标和名称，"原ACTIVITY"代表只显示MainActivity这个原来的启动入口，"ALIAS_1"代表别名1，以此类推。

这三个按钮点击对应的代码如下：
```
 /**
     * 设置Activity为启动入口
     * @param view
     */
    public void setActivity(View view) {
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                ".NewActivity1"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
                .DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                ".NewActivity2"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
                .DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                ".MainActivity"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager
                .DONT_KILL_APP);
    }

    /**
     * 设置别名1为启动入口
     * @param view
     */
    public void setAlias1(View view) {
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                        ".NewActivity1"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                ".NewActivity2"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
                .DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                ".MainActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
                .DONT_KILL_APP);
    }
    /**
     * 设置别名2为启动入口
     * @param view
     */
    public void setAlias2(View view) {
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                        ".NewActivity1"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                ".NewActivity2"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager
                .DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                ".MainActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
                .DONT_KILL_APP);
    }

```


！！！这里要注意一个点，就是ComponentName里面的路径一定要写全了，如果在报错日志看到类似找不到这个路径的日志的话，那十有八九就是这个问题了。

切换的代码其实很少，大家看了基本上也都明白了，这里就不做过多解释了。这里我基于隐藏所以别名的情况下，也就是只显示原来的一个APP图标的情况，点一下"ALIAS_1"这个按钮，也就是将图标切换到"别名1"，最终效果如下：

![](https://user-gold-cdn.xitu.io/2019/1/10/16836e2b8c1e44fd?w=440&h=192&f=jpeg&s=15913)

可以看到只显示这一个入口了，但是如果大家在点了"ALIAS_1"之后，马上就返回到主页看盯着这个app的图标，我们会发现在它在大概10s内是没有变化的，在大概10s后才更新成我们切换的那个图标，还有，在它没更新成功的时候如果我们点这个原来的图标，一般会吐司一条“未安装”之类的信息（华为是未安装），这里我的小米是点了没有反应，要等大概10s秒后更新成功了才能点这个图标进入应用。所以，通过代码我们"已经做到了"图标的切换，但是！！！

那是不是这样就完了呢？？显然不是的，问题还挺多的，我一一道来。

不知道大家在点了切换的按钮后有没有一直停在app里面，没有的话我们尝试点完后在app里面不要回到桌面，如果停在app里面的话，我们会在大概10s，也就是更新成功的时候，应用就会发生闪退了，也就是坑4这个问题。这个问题我做了很多测试，总结了一下原因和规避的方法，原因是我们在代码里面设置了我们原来的真实的那个MainActiviy的enable为false，代码如下：
```
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                ".MainActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
                .DONT_KILL_APP);
```
只要代码设置了真实的那个Activity的enable为false，也就是代码对应的PackageManager.COMPONENT_ENABLED_STATE_DISABLED，那就会导致我们的应用闪退，那是不是我们不设置这个就好了呢？那我们不设置这个的话怎么隐藏真实的MainActivity的图标呢？这个解决方法后面我会提出来。

但是，你以为只有这个问题吗？其实还有坑，只是这个坑不容易发现，这个时候我们回到我们当前的情况，也就是当前我们已经切换到"别名1"了，桌面上也只有这个图标了，我们也能点击这个图标正常使用我们的应用，这些都没有问题，我们以为都是正常的了。但是，这个时候，如果我们通过adb，使用Android Studio运行项目的时候，会提示launch app失败，失败的信息如下：
```

01/10 16:48:54: Launching app
$ adb shell am start -n "com.wepon.switchicondemo/com.wepon.switchicondemo.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
Error while executing: am start -n "com.wepon.switchicondemo/com.wepon.switchicondemo.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.wepon.switchicondemo/.MainActivity }
Error type 3
Error: Activity class {com.wepon.switchicondemo/com.wepon.switchicondemo.MainActivity} does not exist.

Error while Launching activity

```

同样导致的问题还有一个，就是我们代码动态切换了app图标之后，应用升级，也就是更新应用的时候，会导致安装失败，或者是安装完成后出现多个图标甚至是没有图标出现在桌面上了！！这些问题是要遇到运行，或者升级包的时候才会发现的，但是那时候发现就晚了，所以这是一个比较大的坑，这里对应的坑就是我在前面提到的坑2这个点。

这里还有一种情况也会导致坑2的发生，例如，我们Demo现在是一个MainActivity和两个别名，如果我们在下一个版本把这两个别名删除了，或者删除了我们当前安装包正在显示的别名，那么安装的新版本可能就不会有应用图标显示了，那就会导致我们应用安装成功了，但是却没有入口！

类似的问题还有一些，主要都是在应用升级后发生，而且不管是导致安装失败、安装后没有图标或者安装后产生多个图标，这些现象都是非常严重的，但是这些问题我们都是可以避免的，这里我总结了一些规则，按这些规则进行操作的话是不会产生以上这些问题的，当然，如果还有其他问题的话欢迎交流，因为我们的app也在做这个功能。

# 动态修改图标的开发规则，防坑专用

1、Activity的android:enabled属性，一定不要在代码里面去设置enabled这个值，否则会在切换图标的过程导致应用闪退，目前测试了小米、华为和官方模拟器都有在这个问题。

2、清单文件中设置Activity的android:enabled="false”,这个在之后的版本就固定这个值，如果设置为true了，则有可能在应用升级后出现多个图标；

3、然后为我们的应用设置一个默认的Activity-alias用来显示图标（也是唯一一个显示的，一般我们也只需要显示一个图标），也是用来代替第一点设置Activity的android:enabled="false”可能导致的桌面上没有应用图标的问题；

4、Activity-alias的android:enabled="true"的默认显示的项尽可能不要中途进行变动，如果确实需要使用新的默认值，则使用代码进行动态变换；

5、Activity-alias的android:enabled="true"的不要设置为多个，否则会出现多个图标，如果试图通过代码进行隐藏其中的一个或者几个，可能会出现图标消失的情况，这个第2点已经有提过了；

6、后面新的版本如果要加新的Activity-alias，那么都要设置android:enabled=“false”，这个清单文件中的值要设置成false，然后再通过代码动态变换；

7、后面新的版本的Activity-alias必须包含上一个版本的所有Activity-alias，主要是防止覆盖安装后应用图标消失的情况；

update:2019年1月14日下午5:09 新发现需要注意的问题
8、设置enabled为false的Activity无法在代码中通过显式intent打开，会报错。例如：我在应用里面推送服务推送了一条指定打开页面SplashActivity的通知消息，而这个SplashActivity刚好设置了enabled为false的话，是打不开的，会有错误日志如下，其它同理（所以在项目里我将启动入口的Activity单独写出来了，除了作为启动入口用，就没有别的地方再用到这个Activity了。）：

![](https://user-gold-cdn.xitu.io/2019/1/14/1684ba4c10c1eae1?w=2826&h=506&f=jpeg&s=268865)


以上就是我在做这个功能的过程中总结出来的规则，目前没有发现在其它的问题，有别的问题的朋友欢迎留言讨论，还有，按照这些规则做的话，覆盖安装后的应用图标也会是你上一次通过代码动态修改成功的图标，因为手机的Launcher会有记录，也就是我们通过代码会修改这个在Launcher中的记录。

对了，我们在清单文件中配置的Activity和Activity-alias的icon和label信息在新的版本中都是可以换的，这些跟代码无关了，也就是跟我们平常换下app图标名称是一样的操作，希望大家不要误解了这里 -_-!!!。


# 最后

最后，可能有的同学会想，我现在的应用入口就是默认的一个Activity，默认的enable也是true，也没有配置任何的Activity-alias，而我在上面说的规则中都是建议清单文件中的Activity的android:enabled="false”，那有人可能就会想我的新版本设置成false会不会导致我的图标入口不见了呢？那么我告诉你，如果按照我上面说的规则对你的新版本（可以动态切换图标的版本）进行设置的话，是不会有以上情况产生的，这里我给一个针对这种情况进行升级的版本的清单文件的示例：
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.wepon.switchicondemo">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher_round"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">

        <!--原Activity enabled固定为false，且不通过代码进行设置 -->
        <activity
            android:enabled="false"
            android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- 固定设置一个默认的别名，用来替代原Activity-->
        <activity-alias
            android:name="DefaultAlias"
            android:enabled="true"
            android:label="@string/app_name"
            android:icon="@mipmap/ic_launcher_round"
            android:targetActivity=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity-alias>

        <!--别名1  春节，双11，双12，51，国庆等等，都可以给配置一个别名在清单文件，这里我只示例了一个。-->
        <activity-alias
            android:name="NewActivity1"
            android:enabled="false"
            android:label="Alias1"
            android:icon="@mipmap/ic_launcher"
            android:targetActivity=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity-alias>

    </application>

</manifest>
```

# 简单示例Demo

这里放一个简单的示例demo仅供参考

[https://github.com/ywp0919/SwitchIconDemo](https://github.com/ywp0919/SwitchIconDemo)













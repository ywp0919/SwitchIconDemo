package com.wepon.switchicondemo;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 设置Activity为启动入口
     * @param view
     */
    public void setActivity(View view) {
        Toast.makeText(this, "直接设置Activity是会有很多问题的，不要这么做哦！", Toast.LENGTH_SHORT).show();

        // 这种方式不要用。
//        PackageManager packageManager = getPackageManager();
//        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
//                ".NewActivity1"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
//                .DONT_KILL_APP);
//        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
//                ".NewActivity2"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
//                .DONT_KILL_APP);
//        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
//                ".MainActivity"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager
//                .DONT_KILL_APP);
    }

    /**
     * 设置默认的别名为启动入口
     * @param view
     */
    public void setDefaultAlias(View view) {
        Toast.makeText(this, "已切换到默认别名，大概10秒后生效", Toast.LENGTH_SHORT).show();

        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                        ".DefaultAlias"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                        ".NewActivity1"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                ".NewActivity2"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
                .DONT_KILL_APP);

    }

    /**
     * 设置别名1为启动入口
     * @param view
     */
    public void setAlias1(View view) {

        Toast.makeText(this, "已切换到别名1，大概10秒后生效", Toast.LENGTH_SHORT).show();

        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                        ".DefaultAlias"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                        ".NewActivity1"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                ".NewActivity2"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
                .DONT_KILL_APP);


        // 不要操作Activity
//        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
//                ".MainActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
//                .DONT_KILL_APP);
    }
    /**
     * 设置别名2为启动入口
     * @param view
     */
    public void setAlias2(View view) {

        Toast.makeText(this, "已切换到别名2，大概10秒后生效", Toast.LENGTH_SHORT).show();

        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                        ".DefaultAlias"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                        ".NewActivity1"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
                ".NewActivity2"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager
                .DONT_KILL_APP);


        // 不要操作Activity
//        packageManager.setComponentEnabledSetting(new ComponentName(this, getPackageName() +
//                ".MainActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager
//                .DONT_KILL_APP);
    }

}

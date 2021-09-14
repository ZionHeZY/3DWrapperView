# 3DWrapperView
## 背景
>[自如团队分享](https://juejin.cn/post/6989227733410644005#comment)
由于自己实现时效果并不是很理想（出现较多的抖动），换了个方式。
---
## 介绍
原理就是上面链接分享的
---
## 使用
将需要3d展示效果的组件包裹起来即可
```
    <com.hezy.sensorview.SensorView
        android:id="@+id/sensor_view2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:sensorScale="1.1"
        app:sensorStyle="foreground">

        <ImageView
            android:id="@+id/iv_fore2"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:scaleType="fitXY"
            android:scaleX="1"
            android:scaleY="1"
            android:src="@drawable/fore" />
    </com.hezy.sensorview.SensorView>
   ```
   ---
   ## 注意
   1.没有写绑定声明周期部分，需要在页面销毁时调用下unregister()方法
   2.传感器采集部分只选择了陀螺仪，将时间设置为固定的值，这些都是为了scroll过程中画面效果稳定考虑，一般写法如下：
   
    ```
        // 将纳秒转化为秒
        private static final float NS2S = 1.0f / 1000000000.0f;
    ...
        angleX += event.values[0] * (event.timestamp - endTimestamp) * NS2S;
        angleY += event.values[1] * (event.timestamp - endTimestamp) * NS2S;
     ```
   

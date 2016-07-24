# SubmitButton
A practical, cool and elegant Submit Button

## Demo
![Demo](https://github.com/SparkYuan/SubmitButton/blob/master/pic/submitbutton.gif)

## Attributes

```xml
 <com.spark.submitbutton.SubmitButton
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:text="Submit"
        android:textColor="@color/gray"
        app:sub_btn_background="@color/white"
        app:sub_btn_duration="3000"
        app:sub_btn_line_color="@color/green"
        app:sub_btn_ripple_color="@color/green"
        app:sub_btn_tick_color="@color/white" />

```
### Notice
- SubmitButton is a subclass of TextView, so almost all attributes of TextView can be used for SubmitButton.
- The width and height of this SubmitButton is measured by the text size and its **layout_width** and **layout_height** must be **match_parent**.
- The default gravity of the text in this SubmitButton is **center** and can not be changed.


## Download

Step 1. Configure your project-level build.gradle to include the follow repository:

```gradle
repositories {
    maven {
        url 'https://dl.bintray.com/spark/maven'
    }
}
```

Step 2. Add the dependency:

```gradle
dependencies {
  compile 'me.spark:submitbutton:1.0.1'
}
```


##**Lincense**

```lincense
The MIT License (MIT)

Copyright (c) 2016 Spark

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


[![Download](https://api.bintray.com/packages/thefuntastyops/donut/donut/images/download.svg) ](https://bintray.com/thefuntastyops/donut/donut/_latestVersion)
[![Build Status](https://app.bitrise.io/app/e9f4fbbcc143c212/status.svg?token=LK6EaX0H10eB3wjz5k-HlQ&branch=master)](https://app.bitrise.io/app/e9f4fbbcc143c212)

# Donut 🍩
`DonutProgressView` is a configurable doughnut-like graph view capable of displaying multiple datasets with assignable colors. It supports animations and features a gap at the top, which makes it look like a gauge (or tasty bitten-off donut - that's why the name).

![Header](imgs/readme-header.png)

The view automatically scales it's datasets proportionally to their values once it gets filled up. This allows you to show your users their daily progresses, reached goals, etc.  

## How to use
Place the view in your layout

```xml
<app.futured.donut.DonutProgressView
    android:id="@+id/donut_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:donut_bgLineColor="@color/cloud"
    app:donut_gapWidth="20"
    app:donut_gapAngle="270"
    app:donut_strokeWidth="16dp"/>
```

Submit data to the view

```kotlin
val dataset1 = DonutDataset(
    name = "dataset_1",
    color = Color.parseColor("#FB1D32"),
    amount = 1f
)

val dataset2 = DonutDataset(
    name = "dataset_2",
    color = Color.parseColor("#FFB98E"),
    amount = 1f
)

donut_view.cap = 5f
donut_view.submitData(listOf(dataset1, dataset2))
```

You'll get something like this:

![View with cap unexceeded](imgs/readme_intro_nocap.png)

### About the data cap
Once the sum of all dataset values exceeds view's `cap` property, the view starts to scale it's datasets proportionally to their amounts along it's length. E.g. if we, in the upper example, set cap to `donut_view.cap = 1f` (`dataset1.amount + dataset2.amount > 1f`), we would get something like this:

![View with cap exceeded](imgs/readme_intro_cap.png)

## Submitting data
The view accepts list of `DonutDataset` objects that define data to be displayed.  
Each `DonutDataset` object holds dataset's unique identifier (string), it's color (color int) and dataset's value. *(Note: the view uses unique ID to resolve it's internal state and animations, and has undefined behavior if more datasets with the same ID are provided)*

```kotlin
val waterAmount = DonutDataset(
    name = "drink_amount_water",
    color = Color.parseColor("#03BFFA"),
    amount = 1.2f
)
```

You have to submit new list of datasets everytime you want to modify displayed data, as `DonutDataset` object is immutable.

```kotlin
donut_view.submitData(listOf(waterAmount))
```
Once you call the `submitData` method, the view **automatically resolves and animates to the new state**.

If you want to get currently displayed data, call `getData()` method. To clear displayed data, call `clear()` method.

## Customization
The view allows you to configure various properties to let you create a unique style that fits your needs. They can be changed either via XML attributes, or at runtime via property access.

### XML attributes
|Name|Description|
|---|---|
| `donut_cap`| View's cap property |
| `donut_strokeWidth` | Width of background and dataset lines in dp |
| `donut_bgLineColor`| Color of background line |
| `donut_gapWidth` | Width of the line gap in degrees |
| `donut_gapAngle` | Position of the line gap around the view in degrees |
| `donut_animateChanges` | Animation enabled flag, if `true`, the will animate it's state changes (enabled by default) |
| `donut_animationInterpolator` | Interpolator to be used in state change animations |
| `donut_animationDuration` | Duration of state change animations in ms |

In addition to these XML attributes, the view features `masterProgress` property (`0f to 1f`) that can be changed programatically. It controls percentual progress of all lines, including the background line, which allows you to get creative with startup animations, etc.

### Sample app

The quickest way to explore different styles is to try the [sample](sample/) app, which contains an interactive playground with buttons and sliders to fiddle with.


![Playground](imgs/playground.gif)

## Download
`build.gradle`:

```groovy
allprojects {
    repositories {
        jcenter()
    }
}
```

`module/build.gradle`:

```groovy
dependencies {
    implementation("app.futured.donut:library:$version")
}
```
---
Made with 🖤 in [Futured](http://futured.app)

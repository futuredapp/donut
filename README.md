[ ![Download](https://api.bintray.com/packages/thefuntastyops/donut/donut/images/download.svg) ](https://bintray.com/thefuntastyops/donut/donut/_latestVersion)
[![Build Status](https://app.bitrise.io/app/e9f4fbbcc143c212/status.svg?token=LK6EaX0H10eB3wjz5k-HlQ&branch=master)](https://app.bitrise.io/app/e9f4fbbcc143c212)

# Donut 🍩
`DonutProgressView` is a configurable doughnut-like graph view capable of displaying multiple datasets with assignable colors. It features a gap at the top, which makes it look like a gauge (or tasty bitten-off donut - that's why the name).

The view uses a `cap` property to determine when it should start to scale down it's datasets once their sum gets above it.

## Usage
Place the view in your layout:

```xml
<app.futured.donut.DonutProgressView
    android:id="@+id/donut_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:donut_cap="10"
    app:donut_strokeWidth="12dp"/>
```

Submit data to the view:

```kotlin
val apples = DonutDataset(
    name = "chocolate",
    color = Color.parseColor("#e03e28"),
    amount = 4f
)

val oranges = DonutDataset(
    name = "candy",
    color = Color.parseColor("#e09928"),
    amount = 2f
)

donut_view.submitData(listOf(chocolate, candy))
```

### Customization
You can use various view properties to define it's appearance and behavior.

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

<img align="right" src="imgs/donut.svg">

# Donut

[![Download](https://img.shields.io/maven-central/v/app.futured.donut/donut)](https://search.maven.org/search?q=app.futured.donut)
[![Publish snapshot](https://github.com/futuredapp/donut/actions/workflows/publish_snapshot.yml/badge.svg)](https://github.com/futuredapp/donut/actions/workflows/publish_snapshot.yml)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Donut-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/8015)
[![Android Weekly](https://androidweekly.net/issues/issue-449/badge)](https://androidweekly.net/issues/issue-449)
![License](https://img.shields.io/github/license/futuredapp/donut?color=black)
![Jetpack Compose](https://img.shields.io/badge/Compose%20Multiplatform-Ready-green)

Donut is a Compose Multiplatform library for creating beautiful doughnut-like charts. It also contains a legacy View-based implementation for Android only.

## Installation

### Compose Multiplatform

#### Kotlin Multiplatform Project

`module/build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("app.futured.donut:donut-compose:$version")
            }
        }
    }
}
```

### Legacy View-based (Android only)

`module/build.gradle`:

```groovy
dependencies {
    implementation("app.futured.donut:donut:$version")
}
```

### Snapshot installation

Snapshots are build from each commit to master branch.

Add new Maven repo in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        // ...
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    }
}
```

Snapshots are grouped based on major version of library, so for version 2.x, use:
```kotlin
implementation("app.futured.donut:donut-compose:2.X.X-SNAPSHOT")
```

## Features

Donut provides configurable doughnut-like charts capable of displaying multiple sections with assignable colors. It supports animations and features a gap at the top, which makes it look like a gauge (or tasty bitten-off donut - that's why the name).

![Header](imgs/readme-header.png)

The charts automatically scale their sections proportionally to their values once they get filled up. This allows you to show your users their daily progresses, reached goals, etc.

## Usage

### Compose Multiplatform

```kotlin
@Composable
fun Sample() {
    DonutProgress(
        model = DonutModel(
            cap = 2.2f,
            masterProgress = 1f,
            gapWidthDegrees = 25f,
            gapAngleDegrees = 270f,
            strokeWidth = 40f,
            backgroundLineColor = Color(0xFFE7E8E9),
            sections = listOf(
                DonutSection(amount = 0.4f, color = Color(0xFFFB1D32)),
                DonutSection(amount = 0.4f, color = Color(0xFFFFB98E)),
            )
        ),
        config = DonutConfig.create(
            layoutAnimationSpec = tween(
                durationMillis = 1000,
                easing = CubicBezierEasing(0.18f, 0.7f, 0.16f, 1f),
            ),
            colorAnimationSpec = tween(durationMillis = 1000),
        )
    )
}
```

You'll get something like this:

![View with cap unexceeded](imgs/readme_intro_nocap.png)

### About the data cap

Once the sum of all section values exceeds the `cap` property, the chart starts to scale its sections proportionally to their amounts along its length. E.g. if we set cap to `1f` and total amount > 1f, we would get something like this:

![View with cap exceeded](imgs/readme_intro_cap.png)

### Legacy View-based (Android only)

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
val section1 = DonutSection(
    name = "section_1",
    color = Color.parseColor("#FB1D32"),
    amount = 1f
)

val section2 = DonutSection(
    name = "section_2",
    color = Color.parseColor("#FFB98E"),
    amount = 1f
)

donut_view.cap = 5f
donut_view.submitData(listOf(section1, section2))
```

You'll get something like this:

![View with cap unexceeded](imgs/readme_intro_nocap.png)

### Submitting data

Both implementations work differently when it comes to data management.

#### Compose Multiplatform

The Compose version uses simple `DonutSection` objects with color and amount:

```kotlin
@Composable
fun MyDonutChart() {
    DonutProgress(
        model = DonutModel(
            cap = 5f,
            sections = listOf(
                DonutSection(color = Color.Cyan, amount = 1.2f),
                DonutSection(color = Color.Red, amount = 2f),
            )
        )
    )
}
```

**Important**: The Compose version does not support adding or removing sections dynamically. The number of sections must remain the same throughout the component's lifecycle. To simulate adding/removing sections, initialize with empty sections and set their amounts to zero.

#### Legacy View-based

The View-based version requires unique names for each section and supports dynamic changes:

```kotlin
val waterAmount = DonutSection(
    name = "drink_amount_water",
    color = Color.parseColor("#03BFFA"),
    amount = 1.2f
)

donut_view.submitData(listOf(waterAmount))
```

*(Note: the view uses unique name for each section to resolve its internal state and animations, and throws `IllegalStateException` if multiple sections with same name are submitted.)*

The view also provides methods for more granular control over displayed data. You can use `addAmount`, `setAmount` and `removeAmount` methods to add, set or remove specified amounts from displayed sections.

##### Adding amount

```kotlin
donut_view.addAmount(
    sectionName = "drink_amount_water",
    amount = 0.5f,
    color = Color.parseColor("#03BFFA") // Optional, pass color if you want to create new section
)
```

The `addAmount` adds specified amount to section with provided name. What if section does not yet exist? This method has one optional `color` parameter (default value is `null`) - when called, and there isn't already displayed any section with provided name and `color` parameter was specified, the new `DonutSection` with provided name, amount and color will be automatically created internally for you. If you leave the `color` param `null` while trying to add value to non-existent section, nothing happens.

##### Setting amount

```kotlin
donut_view.setAmount(
    sectionName = "drink_amount_water",
    amount = 2.5f
)
```

The `setAmount` methods sets specified amount to section with provided name. If provided amount is equal or less than 0, section and corresponding progress line are automatically removed after animation. If view does not contain specified section, nothing happens.

##### Removing amount

```kotlin
donut_view.removeAmount(
    sectionName = "drink_amount_water",
    amount = 0.1f
)
```

The `removeAmount` simply subtracts specified amount from any displayed section. If resulting amount is equal or less than 0, section and corresponding progress line are automatically removed after animation. If view does not contain specified section, nothing happens.

#### Get and clear data

If you want to get currently displayed data, call `getData()` method which returns immutable list of all displayed `DonutSection` objects. To clear displayed data, call `clear()` method.

Each call to a data method (submit, add, set, remove, clear) results in view **automatically resolving and animating to the new state**.

### Customization

Both implementations provide extensive customization options to create unique donut chart styles. The Compose Multiplatform version offers more granular control over animations, while the View-based version uses traditional XML attributes.

#### Jetpack Compose version

This library is implemented as a standalone module also for Compose Multiplatform. It has (almost) the same features as the view implementation, but it supports a wider variety of animations.

```kotlin
@Composable
fun Sample() {
    DonutProgress(
        model = DonutModel(
            cap = 8f,
            masterProgress = 1f,
            gapWidthDegrees = 270f,
            gapAngleDegrees = 90f,
            strokeWidth = 40f,
            backgroundLineColor = Color.LightGray,
            sections = listOf(
                DonutSection(amount = 1f, color = Color.Cyan),
                DonutSection(amount = 1f, color = Color.Red),
                DonutSection(amount = 1f, color = Color.Green),
                DonutSection(amount = 0f, color = Color.Blue)
            )
        ),
        config = DonutConfig(
            gapAngleAnimationSpec = spring()
            // ...
        )
    )
}
```

#### XML attributes

| Name                          | Default value            | Description                                                                                      |
|-------------------------------|--------------------------|--------------------------------------------------------------------------------------------------|
| `cap`                         | `1.0f`                   | View's cap property                                                                              |
| `donut_strokeWidth`           | `12dp`                   | Width of background and section lines in dp                                                      |
| `donut_strokeCap`             | `round`                  | The paint cap used for all lines. Can be either 'round' or 'butt'                                |
| `donut_bgLineColor`           | `#e7e8e9`                | Color of background line                                                                         |
| `donut_gapWidth`              | `45°`                    | Width of the line gap in degrees                                                                 |
| `donut_gapAngle`              | `90°`                    | Position of the line gap around the view in degrees                                              |
| `donut_direction`             | `clockwise`              | Progress lines direction (`clockwise` or `anticlockwise`)                                        |
| `donut_animateChanges`        | `true`                   | Animation enabled flag, if `true`, the view will animate it's state changes (enabled by default) |
| `donut_animationInterpolator` | `DecelerateInterpolator` | Interpolator to be used in state change animations                                               |
| `donut_animationDuration`     | `1000 ms`                | Duration of state change animations in ms                                                        |

In addition to these XML attributes, the view features `masterProgress` property (`0f to 1f`) that can be changed programmatically. It controls percentual progress of all lines, including the background line, which allows you to get creative with startup animations, etc.

#### Sample apps

The quickest way to explore different styles and configurations is to try the sample apps:

- **[sample-cmp](sample-cmp/)** - **Recommended**: A Compose Multiplatform app that runs on both Android and iOS
  - **Android**: Run directly from Android Studio
  - **iOS**: Open the [iOS project](iosApp/) in Xcode and run on iOS devices or simulators
  - Features an interactive playground with buttons and sliders to experiment with different configurations
- **[sample](sample/)** - Legacy Android-only sample app with the original View-based implementation

![Playground](imgs/playground.gif)

## Contributors

Current maintainer and main contributor for the original version is [Matej Semančík](https://github.com/matejsemancik) and for Jetpack Compose version is [Martin Sumera](https://github.com/sumeramartin). Check the [PUBLISHING.md](PUBLISHING.md) document for releasing and publishing guidelines.

## Licence

Donut is available under MIT license. See [LICENSE file](LICENSE) for more information.

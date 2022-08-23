Chips Layout Manager
===================

Chips Layout Manager is a custom RecyclerView LayoutManager used to manage Chip Views and custom Chip implementations. It also supports RecyclerView's CRUD animations.

Motive
------
I worked on a project which required Material Chips and I used [ChipsLayoutManager](https://github.com/BelooS/ChipsLayoutManager/) which was extremely helpful in the overall application UI design 
but unfortunately it is written totally in Java with no documentation and I wanted a complete Kotlin codebase, so I decided to take it up as a challenge to translate the library, include inline documentations, 
create a Github branch and develop the library as a personal project. 
The repository has not been updated by the original developer BelooS since 2017.
All credits go to BelooS for developing a useful project as this. I only translated the effort from Java to Kotlin.

Notice
------
* This repository is based on BelooS' [ChipsLayoutManager](https://github.com/BelooS/ChipsLayoutManager/) but rewritten in Kotlin with inline documentations.
* This is for any developer who uses [ChipsLayoutManager](https://github.com/BelooS/ChipsLayoutManager/) but needs a Kotlin variant.
* The library is redesigned to be as similar as possible to the original, maintaining the API's overall implementation whilst including several significant updates.
* **NOTE**: This repository is active and usable but no release is ready as slight changes are going to be made and several Kotlin platform features are going to be added. Contributors are welcome.

Usage
-----
Create a ChipsLayoutManager easily using the extension function in an Activity class as follows:
```KOTLIN
chipsLayoutManager { }
```
If you are within a Fragment:
```KOTLIN
requireContext.chipsLayoutManager { }
```
Or any class that has a Context:
```KOTLIN
context.chipsLayoutManager { }
```
Then apply the necessary configurations needed to build a ChipsLayoutManager instance. 
```KOTLIN
val chipsLayoutManager = chipsLayoutManager {
    setOrientation(ChipsLayoutManager.HORIZONTAL)
    setMaxViewsInRow(3)
    setScrollingEnabled(true)
    setRowStrategy(ChipLayoutManager.STRATEGY_DEFAULT)
    setChildGravity(Gravity.TOP)
    withLastRow(true)
}
```
Then assign the ChipsLayoutManager to a RecyclerView:
```KOTLIN
val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
recyclerView.layoutManager = chipsLayoutManager
```
**NOTE**: The build() method of the returned builder is called internally. There is no need to invoke the build() method.

Alternatively you can create a ChipsLayoutManager as described in [ChipsLayoutManager](https://github.com/BelooS/ChipsLayoutManager/) as follows:
```KOTLIN
val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
    .setOrientation(ChipsLayoutManager.HORIZONTAL) 
    .setMaxViewsInRow(3) 
    .setScrollingEnabled(true) 
    .setRowStrategy(ChipLayoutManager.STRATEGY_DEFAULT)
    .setChildGravity(Gravity.TOP)
    .withLastRow(true)
    .build()
recyclerView.layoutManager = chipsLayoutManager
```
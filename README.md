Chip Layout Manager
===================

Chip Layout Manager is a custom RecyclerView LayoutManager used to manage Chip Views and custom Chip implementations. It also supports RecyclerView's CRUD animations.

Notice
------
* This repository based on BelooS' [ChipsLayoutManager](https://github.com/BelooS/ChipsLayoutManager/) but rewritten in Kotlin with inline documentations.
* This is for any developer who uses [ChipsLayoutManager](https://github.com/BelooS/ChipsLayoutManager/) but needs a Kotlin variant.
* This is an active repository and features are going to be added. Contributors are welcome.

Usage
-----
It follows the same usage pattern as described in [ChipsLayoutManager](https://github.com/BelooS/ChipsLayoutManager/).
**NOTE**: Contrary to [ChipsLayoutManager](https://github.com/BelooS/ChipsLayoutManager/), the class to access the LayoutManager is ChipLayoutManager.

```KOTLIN
val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
val chipsLayoutManager = ChipLayoutManager.newBuilder(context)
    .setOrientation(ChipLayoutManager.HORIZONTAL) 
    .setMaxViewsInRow(3) 
    .setScrollingEnabled(true) 
    .setRowStrategy(ChipLayoutManager.STRATEGY_DEFAULT)
    .setChildGravity(Gravity.TOP)
    .withLastRow(true)
    .build()
recyclerViewsetLayoutManager(chipsLayoutManager)
```
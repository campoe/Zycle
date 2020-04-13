
# Zycle :recycle:  
  
>A Kotlin DSL for Android RecyclerViews.

![Bintray](https://img.shields.io/bintray/v/coenvk/maven/zycle?label=Download)
![GitHub](https://img.shields.io/github/license/coenvk/zycle?label=License)
![GitHub stars](https://img.shields.io/github/stars/coenvk/zycle?style=social)
  
# Table of Contents  
  
- [Getting Started](#getting-started)  
- [Usage](#usage)  
	- [Basic](#basic)
	- [Mapper](#mapper)
	- [Observable List](#observable-list)
	- [Static Views](#static-views)
	- [Composite Adapter](#composite-adapter)
		- [Adapter Position Lookup](#adapter-position-lookup)
	- [Conditional Adapter](#conditional-adapter)
	- [Drag & Swipe](#drag--swipe)
- [Build](#build)
- [License](#license)  
  
## Getting Started  
  
1. Add the `jcenter()` repository to the project `build.gradle` file.  
  
```  
buildscript {  
 repositories { jcenter() }}  
```  
  
2. Add the dependency to your module's `build.gradle` file.  
  
>***Latest version:*** ![Bintray](https://img.shields.io/bintray/v/coenvk/maven/zycle?label=Download)
  
```  
dependencies {  
 implementation 'com.coenvk.android.zycle:zycle:1.0.0'}  
```  
  
3. Migrate to **AndroidX** and enable **Jetifier** in the `gradle.properties` file.  
  
```  
android.useAndroidX=true  
android.enableJetifier=true  
```  

4. Add the following dependency for extra kotlin extensions.
>***Latest version:*** ![Bintray](https://img.shields.io/bintray/v/coenvk/maven/zycle-ktx?label=Download)

```  
dependencies {  
 implementation 'com.coenvk.android.zycle:zycle-ktx:1.0.0'
}  
```  
  
## Usage  
  
### Basic
An adapter can be created by supplying a list of items and the layout resource for the item view. Bind data to the item view in the `onBind` and add extra event listeners such as the `onClick`. This can be done using the `Builder` as illustrated below or a custom `Binder` can be provided by extending the `Binder` class. To register event listeners the `Binder` needs to be `Hookable`.
<details><summary><b>Kotlin (click to expand)</b></summary>  
<p>  
  
````kotlin  
recyclerView.zycle {
	adapterOf(list, layoutRes, [viewType]) { // Binder
		onCreate { /* */ }
		onBind { item -> /* */ }
		onRecycle { /* */ }
		stableId { item, position -> /* */ }
		onClick { item, position -> /* */ }
		onLongClick { item, position -> /* */ }
		onTouch { item, position, motionEvent -> /* */ }
		onDrag { item, position, dragEvent -> /* */ }
	}
}
```` 
</p></details>
  
### Mapper
If you want to use different layouts, multiple `Binder` objects can be created and combined into a `Mapper`. 
<details><summary><b>Kotlin (click to expand)</b></summary>  
<p>  
  
````kotlin  
recyclerView.zycle {
	adapterOf(list) { // Mapper
		map(layoutRes) { // Binder
			// ...
		}
	}
}
```` 
</p></details>

### Observable List
The `ObservableList` class can be used with the adapter to automatically update the item views when a change is applied to the list. The list extends the `MutableList` class, so it can be used with the same functionality. The items in the list can be of any type. For better performance, the item class can implement `Diffable` and implement the `areItemsTheSame` and `areContentsTheSame` methods to compare two items.

### Static Views
It is also possible to add static views which don't require binding data.
<details><summary><b>Kotlin (click to expand)</b></summary>  
<p>  
  
````kotlin 
recyclerView.zycle {
	viewsOf(layoutRes...)
}
```` 
</p></details>

### Composite Adapter
A composite adapter can be created consisting of multiple adapters. This way it's easy to add headers and footers to your item list.
<details><summary><b>Kotlin (click to expand)</b></summary>  
<p>  
  
````kotlin  
recyclerView.zycle {
	adapterOf {
		viewsOf(headerRes)
		adapterOf(list) { // Mapper
			map(binder)
		}
		viewsOf(footerRes)
	}
}
```` 
</p></details>

#### Adapter Position Lookup
Since adapters can be nested in composite adapters, the adapter position from the `RecyclerView.ViewHolder` will not (necessarily) match the index in the inner adapter.
Therefore, the `AdapterPositionLookup` is provided that looks up this position.

### Conditional Adapter
An adapter can be shown or hidden based on a condition. In addition, a substitute view can be shown when the condition is not met.
<details><summary><b>Kotlin (click to expand)</b></summary>  
<p>  
  
````kotlin  
val condition = Condition()
val isEnabled by condition
recyclerView.zycle {
	adapterOf {
		adapterOf(list) {
			map(binder)
		}
		postBuild {
			showIf(condition)
			showIfElse(condition, layoutRes)
		}
	}
}
isEnabled = false // hide the list
```` 
</p></details>

### Drag & Swipe
To add simple drag functionality a drag callback can be created.
<details><summary><b>Kotlin (click to expand)</b></summary>  
<p>  
  
````kotlin  
val dragCallback = DragCallback(  
    object : OnDragListener {  
        override fun onDragged(fromPosition: Int, toPosition: Int) {  
            val fromIndex = adapterPositionLookup.innerPosition(fromPosition)  
            val toIndex = adapterPositionLookup.innerPosition(toPosition)  
            list.move(fromIndex, toIndex)  
        }  
    }  
)
recyclerView += ItemTouchHelper(dragCallback)
```` 
</p></details>

For this to work, the `Binder` should implement the `Draggable` interface.

Secondly, a swipe callback can be used to support swipe actions like swipe-to-delete.
<details><summary><b>Kotlin (click to expand)</b></summary>  
<p>  
  
````kotlin  
val swipeCallback = SwipeCallback.Builder()  
	.left {  // When swiped left
		background(context, android.R.color.holo_red_dark)  
        text(context, "Delete", spSize = 20f)  
        onSwiped {  
			val index = adapterPositionLookup.innerPosition(it)  
            list.removeAt(index)  
        }  
	}.build()
recyclerView += ItemTouchHelper(swipeCallback)
```` 
</p></details>

The `Binder` needs to be `Swipeable`.

### Sample
For more comprehensive example usages, checkout the sample application [here](https://github.com/coenvk/zycle/tree/master/app).

## Build
The project can be build locally using the following commands:
```bash
$ git clone https://github.com/coenvk/zycle.git
$ cd zycle
$ ./gradlew clean build
```
  
## License  
  
Zycle is licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0).
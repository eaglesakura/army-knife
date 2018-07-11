package com.eaglesakura.ktx.runtime.extensions

/**
 * An obj add to list when not overlaps.
 * This method returns added index or overlap object index.
 */
fun <T> MutableList<T>.addUnique(obj: T): Int {
    forEachIndexed { index, value ->
        if (value == obj) {
            return index
        }
    }
    add(obj)
    return size - 1
}

/**
 * An obj in list add to list when not overlaps.
 * This method returns added index or overlap object index.
 */
fun <T> MutableList<T>.addUniqueAll(list: Iterable<T>) {
    for (item in list) {
        addUnique(item)
    }
}

/**
 * Delete overlaps object in this list.
 */
fun <T> MutableList<T>.shrink() {
    val temp = mutableSetOf<T>()
    this.iterator().also { iterator ->
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (temp.contains(item)) {
                iterator.remove()
            } else {
                temp.add(item)
            }
        }
    }
}
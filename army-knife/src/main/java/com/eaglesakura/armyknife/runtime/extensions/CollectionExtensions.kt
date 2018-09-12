package com.eaglesakura.armyknife.runtime.extensions

/**
 * Returns true, if it was null or empty.
 */
fun <T> Collection<T>?.isNullOrEmpty(): Boolean {
    if (this == null) {
        return true
    }

    return isEmpty()
}

/**
 * An obj add to list when not overlaps.
 * This method returns added index or overlap object index.
 */
fun <T> MutableCollection<T>.addUnique(obj: T): Int {
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
fun <T> MutableCollection<T>.addUniqueAll(list: Iterable<T>) {
    for (item in list) {
        addUnique(item)
    }
}

/**
 * Delete overlaps object in this list.
 */
fun <T> MutableCollection<T>.shrink() {
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
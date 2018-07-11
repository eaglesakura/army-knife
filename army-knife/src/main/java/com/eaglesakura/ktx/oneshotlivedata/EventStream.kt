package com.eaglesakura.ktx.oneshotlivedata

/**
 * Event data with subscribe.
 *
 * @see newEventObserver
 * @see newEventObserverWithForeground
 */
class EventStream : OneshotLiveData<Event> {
    private val validateData: (id: Event) -> Boolean

    constructor(validId: (id: Event) -> Boolean) : super() {
        this.validateData = validId
    }

    constructor(vararg events: Event) : super() {
        this.validateData = fun(event: Event): Boolean {
            val supportedEvents = events.toList()
            for (ev in supportedEvents) {
                if (event == ev) {
                    return true
                }
            }
            return false
        }
    }

    override fun setOneshot(data: Event) {
        if (!validateData(data)) {
            throw IllegalArgumentException("Not Supported OneshotData[$data]")
        }

        super.setOneshot(data)
    }
}

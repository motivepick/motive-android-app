package com.motivepick.motive.model

import java.util.*

data class UpdateTaskRequest(val name: String?, val description: String?, val dueDate: Date?, val deleteDueDate: Boolean) {

    constructor(name: String?) : this(name, null, null, false)

    constructor(name: String?, description: String?) : this(name, description, null, false)

    constructor(deleteDueDate: Boolean) : this(null, null, null, deleteDueDate)

    constructor(dueDate: Date) : this(null, null, dueDate, false)
}

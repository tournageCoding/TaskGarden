package com.garden.taskgarden

/**
 * Task represents a task object
 *
 * @author Blake MacDade
 */
class Task {
    /**
     * getID
     *
     * @return the id of the object.
     */
    var iD = 0
        private set

    /**
     * getTitle getter method for the Task's title.
     *
     * @return title string of the object.
     */
    var title: String? = null
        private set

    /**
     * getDescription
     *
     * @return description of the object.
     */
    var description: String? = null
        private set

    /**
     * getCompleted
     *
     * @return the completed state of the object.
     */
    var completed = 0
        private set

    /**
     * getTimeToCompleteBy
     *
     * @return time to complete the task by.
     */
    var timeToCompletedBy: String? = null
        private set

    /**
     * Constructor for task object.
     *
     * @param title title of the task.
     * @param description description of the task.
     * @param completeBy date to complete the task by.
     */
    constructor(id:Int,title: String?, description: String?, completed: Int, timeToCompleteBy: String?) {
        this.iD = id
        this.title = title
        this.description = description
        this.completed = completed
        this.timeToCompletedBy = timeToCompleteBy
    }

    /**
     * Blank constructor for Task.
     * Instantiates all values to their defaults.
     */
    constructor() {
        // should we remove this?
    }

    /**
     * updateID updates the id of the object.
     *
     * @param ID the new ID to give the object.
     */
    fun setID(ID: Int) {
        iD = ID
    }

    /**
     * updateTitle updates the title of the object.
     *
     * @param newTitle the new title to give the object.
     */
    fun setTitle(newTitle: String?) {
        title = newTitle
    }

    /**
     * updateDescription updates the description of the object.
     *
     * @param newDescription the new description to give the object.
     */
    fun setDescription(newDescription: String?) {
        description = newDescription
    }

    /**
     * updateCompleted updates the completed state of the object.
     *
     * @param state the new completed state to give the object.
     */
    fun setCompleted(state: Int) {
        completed = state
    }

    /**
     * updateCompletedBy updates the time to complete by of the object.
     *
     * @param time the new time to complete by to give the object.
     */
    fun setCompletedBy(time: String?) {
        timeToCompletedBy = time
    }

    /**
     * toString
     *
     * @return a string representation of the task title and the task description.
     */
    override fun toString(): String {
        return "$title;$description;"
    }
}
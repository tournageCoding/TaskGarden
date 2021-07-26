package com.garden.taskgarden

import junit.framework.TestCase
import org.junit.Assert

class TaskClassTest : TestCase() {

    private var id = 1
    private var title = "Title"
    private var desc = "Description"
    private var timeToCompleteBy = "TimeToCompleteBy"

    private val testTask = Task(id, title, desc, timeToCompleteBy)

    fun testGetID() {
        Assert.assertEquals(testTask.iD, id)
    }

    fun testGetTitle() {
        Assert.assertEquals(testTask.title, title)
    }

    fun testGetDescription() {
        Assert.assertEquals(testTask.description, desc)
    }

    fun testGetCompleted() {
        Assert.assertEquals(testTask.completed, false)
    }

    fun testSetID() {
        id = 2
        testTask.setID(id)
        Assert.assertEquals(testTask.iD, id)
    }

    fun testSetTitle() {
        title = "title2"
        testTask.setTitle(title)
        Assert.assertEquals(testTask.title, title)
    }

    fun testSetDescription() {
        desc = "desc2"
        testTask.setDescription(desc)
        Assert.assertEquals(testTask.description, desc)
    }

    fun testSetCompleted() {
        testTask.setCompleted(true)
        Assert.assertEquals(testTask.completed, true)
    }
}
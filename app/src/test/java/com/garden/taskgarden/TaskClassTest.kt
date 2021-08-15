package com.garden.taskgarden

import junit.framework.TestCase
import org.junit.Assert

/**
 * A test class for the Task class.
 */

class TaskClassTest : TestCase() {

    private var id = 1
    private var title = "Title"
    private var desc = "Description"
    private var timeToCompleteBy = "TimeToCompleteBy"
    private var completed = 0
    private val test = "test"

    private val testTask = Task(id, title, desc, completed, timeToCompleteBy)

    /**
     * Test getID().
     */
    fun testGetID() {
        Assert.assertEquals(testTask.iD, id)
    }

    /**
     * Test getTitle().
     */
    fun testGetTitle() {
        Assert.assertEquals(testTask.title, title)
    }

    /**
     * Test getDescription().
     */
    fun testGetDescription() {
        Assert.assertEquals(testTask.description, desc)
    }

    /**
     * Test getCompleted().
     */
    fun testGetCompleted() {
        Assert.assertEquals(testTask.completed, 0)
    }

    /**
     * Test getTimeToComplete().
     */
    fun testGetTimeToComplete() {
        Assert.assertEquals(testTask.timeToCompletedBy, timeToCompleteBy)
    }

    /**
     * Test setID().
     */
    fun testSetID() {
        id = 2
        testTask.setID(id)
        Assert.assertEquals(testTask.iD, id)
    }

    /**
     * Test setTitle().
     */
    fun testSetTitle() {
        title = "title2"
        testTask.setTitle(title)
        Assert.assertEquals(testTask.title, title)
    }

    /**
     * Test setDescription().
     */
    fun testSetDescription() {
        desc = "desc2"
        testTask.setDescription(desc)
        Assert.assertEquals(testTask.description, desc)
    }

    /**
     * Test setCompleted().
     */
    fun testSetCompleted() {
        testTask.setCompleted(1)
        Assert.assertEquals(testTask.completed, 1)
    }

    /**
     * Test setCompletedBy().
     */
    fun testSetCompletedBy() {
        timeToCompleteBy = "03/08/2021 12:47"
        testTask.setCompletedBy(timeToCompleteBy)
        Assert.assertEquals(testTask.timeToCompletedBy, timeToCompleteBy)
    }
}
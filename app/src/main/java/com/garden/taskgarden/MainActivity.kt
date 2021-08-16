package com.garden.taskgarden

//refactor to deal with all imports of DBInterface!
import android.content.Context
import android.content.Intent
import android.os.Build
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basgeekball.awesomevalidation.validators.ColorationValidator
import com.garden.taskgarden.DBInterface.findTask
import com.garden.taskgarden.DBInterface.removeTask
import com.garden.taskgarden.DBInterface.updateTask
import com.garden.taskgarden.RecyclerView.PaddingItemDecoration
import com.garden.taskgarden.RecyclerView.RecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_popout.view.*
import kotlinx.android.synthetic.main.task_card_view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class for initialising and loading un completed tasks in the RecyclerView. Class also has
 * functions for updating, deleting, and editing tasks.
 */
class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {
    private var settingsTalker: SettingsTalker? = null

    // nullable type
    private lateinit var taskAdapter: RecyclerViewAdapter
    private val currencyKey = "crncyKey"
    private val currencyValuePreferenceName = "currencyValuePreference"

    //var adapter: RecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //dbInterface = new DBInterface();
        settingsTalker = SettingsTalker(this)
        initRecyclerView()
        checkDT()
        loadData()
        displayCurrencyValue()
        levelUp()
    }

    /*
     * Loads list of task objects and sends it to the task adapter.
     */
    private fun loadData(){
        val data = loadTasks()
        taskAdapter.submitList(data)
    }

    /*
     * Function that initialises the RecyclerView
     */
    private fun initRecyclerView(){
        taskList.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            val topSpacingDecoration = PaddingItemDecoration(30)
            addItemDecoration((topSpacingDecoration))
            taskAdapter = RecyclerViewAdapter(this@MainActivity)
            adapter=taskAdapter
        }

    }

    /**
     * onEditClick is called by the clickListener when the user interacts with the edit button
     * of a card in the RecyclerView.
     *
     * @param id the ID of the task to edit.
     * */
    override fun onEditClick(id: Int) {
        editTask(id)
    }

    /**
     * onCompletedClick is called by the clickListener when the user interacts with the complete button
     * of a card in the RecyclerView.
     *
     * @param id the ID of the task to mark as completed.
     * */
    override fun onCompletedClick(id: Int) {
        completedTask(id)
    }

    /**
     * onItemClick is called by the clickListener when the user interacts with the delete button
     * of a card in the RecyclerView.
     *
     * @param id the ID of the task to delete.
     * */
    override fun onItemClick(id: Int) {
        deleteTask(id)
    }

    /*
     * Updates the RecyclerView with any new data.
     * Call whenever you add tasks to the database and want to see them in the RecyclerView.
     */
    private fun updateRecyclerView(){
        loadData()
        taskAdapter.notifyDataSetChanged()
    }

    /*
     * loadTasks gets all the uncompleted tasks from the database and adds them into an ArrayList.
     *
     * @return ArrayList of all uncompleted task objects.
     */
    private fun loadTasks() :ArrayList<Task>{
        try {
            val dbHandler = DBHandler(this, null, null, 1)
            return dbHandler.loadUncompletedHandler()
        } catch (e: Exception) {
            Log.d(debugTag, "Got $e when trying to load tasks in Main Activity.$e")
        }
        return ArrayList()
    }

    /*
     * loadTasks gets all the uncompleted tasks from the database and adds them into an ArrayList.
     *
     * @return ArrayList of all uncompleted task objects.
     */
    private fun loadCompletedTasks() :ArrayList<Task>{
        try {
            val dbHandler = DBHandler(this, null, null, 1)
            return dbHandler.loadCompletedHandlers()
        } catch (e: Exception) {
            Log.d(debugTag, "Got $e when trying to load tasks in Main Activity.$e")
        }
        return ArrayList()
    }


    /*
     * Called from each task to delete itself.
     * @param id the of the task to delete.
     */
    private fun deleteTask(id: Int) {
        try {
            //val id: Int = Integer.parseInt(taskId!!.text.toString())
            if (removeTask(id, this)) {
                updateRecyclerView()
            }
        } catch (e: Exception) {
            Log.d(debugTag, "Got $e while trying to delete task in Main Activity!")
        }
    }

    companion object {
        //DBInterface dbInterface;
        const val debugTag = "MainActivity"
    }

    /**
     * openForm opens the add task form activity. This allows the user to add a task to the database.
     * @param selected view
     * */
    fun openForm(view: View?){
        val intent = Intent(this@MainActivity, TaskFormActivity::class.java)
        startActivity(intent)
    }

    /**
     * openCompletedTasks opens the completed Tasks activity. This allows the user to see all their
     * completed tasks.
     * @param selected view
     * */
    fun openCompletedTasks(view: View?){
        val intent = Intent(this@MainActivity, CompletedTasksActivity::class.java)
        startActivity(intent)
    }

    /*
     * completedTask finds the relevant task from the database, takes it out, marks it as completed,
     * and then adds it back into the database.
     * @param id the ID of the task to be marked as completed.
     */
    private fun completedTask(id: Int) {
         var task = Task()
         try {
             task = findTask(id, this) //Could we not just update the completed flag in the database? this just seems wasteful...
             task.setCompleted(1)
             updateTask(task, this)
             updateRecyclerView()
             // call method to update currency value
             incCurrencyValue()

         } catch (e: Exception) {
             Log.d(debugTag, "Got $e while trying to complete task in Main Activity!")
         }
    }
    /*
     * editTask find the given task in the database and changes it to reflect any updates.
     * @param id the ID of the task to edit.
     */
    private fun editTask(id: Int){
        var task = findTask(id, this)

        val popoutView = LayoutInflater.from(this).inflate(R.layout.edit_popout, null)
        val buildPopout = AlertDialog.Builder(this).setView(popoutView).setTitle("Edit Task")
        val showPopout = buildPopout.show()
        popoutView.btn_accept.setOnClickListener {
            showPopout.dismiss()
            val editTaskText = popoutView.edit_task.text.toString()
            val editTaskDescription = popoutView.edit_description.text.toString()
            if(editTaskText.isNotEmpty()) {
                task.setTitle(editTaskText)
            }
            if(editTaskDescription.isNotEmpty()) {
                task.setDescription(editTaskDescription)
            }
            updateTask(task, this)
            updateRecyclerView()
        }

    }

    private fun checkDT(){
        val uncompletedTasks = loadTasks()
        val dateNow = Calendar.getInstance().time

        for(task in uncompletedTasks){
            try {
                val completedBy = task.timeToCompletedBy
                val completedByDate = SimpleDateFormat("dd/MM/yyyy HH:mm").parse(completedBy)
                if (dateNow > completedByDate) {
                    task.setCompleted(2)
                    updateTask(task, this)
                    updateRecyclerView()
                }
            } catch(e: Exception){
                Log.d(debugTag, "No date and time selected!")
            }
        }

    }

    /**
     * incCurrencyValue update the currency value in SharedPreferences.
     */
    fun incCurrencyValue() {
        val currencyPreferences = this.getSharedPreferences(currencyValuePreferenceName, Context.MODE_PRIVATE)
        val currencyValue = currencyPreferences.getInt(currencyKey, 0)

        with (currencyPreferences.edit()) {
            putInt(currencyKey, currencyValue + 5)
            apply()
        }

        displayCurrencyValue()
        levelUp()
    }

    /**
     * incCurrencyValue update the currency value in SharedPreferences.
     */
    fun onClickIncCurrencyValue(view: View?) {
        val currencyPreferences = this.getSharedPreferences(currencyValuePreferenceName, Context.MODE_PRIVATE)
        val currencyValue = currencyPreferences.getInt(currencyKey, 0)

        with (currencyPreferences.edit()) {
            putInt(currencyKey, currencyValue + 5)
            apply()
        }

        displayCurrencyValue()
        levelUp()
    }

    /*
     * displayCurrencyValue get the currency value and update the currency label to this value.
     */
    private fun displayCurrencyValue() {
        val currencyPreferences = this.getSharedPreferences(currencyValuePreferenceName, Context.MODE_PRIVATE)
        val currencyValue = currencyPreferences.getInt(currencyKey, 0)
        val currencyLabel = findViewById<TextView>(R.id.labelCurrency)
        val currencyLevel = currencyValue / 100
        var textLevel = ""

        if(currencyLevel > 0) {
            textLevel = "Level:\n$currencyLevel\n\n"
        }
        currencyLabel.text = textLevel + "Score:\n$currencyValue"
    }

    private fun levelUp() {
        // println("///////////////////////////////////////////////////////////////////////////")
        val currencyPreferences = this.getSharedPreferences(currencyValuePreferenceName, Context.MODE_PRIVATE)
        val currencyValue = currencyPreferences.getInt(currencyKey, 0)
        val currencyLevel = currencyValue % 100
        val backColour = findViewById<RecyclerView>(R.id.taskList)

        //val primary = findViewById<DONT KNOW WHAT GOES HERE>(R.id.colorPrimary)
        if(currencyLevel < 25) {
            backColour.setBackgroundColor(Color.rgb(176, 196, 222)) //Grey
        }
        if(currencyLevel >= 25){
            backColour.setBackgroundColor(Color.rgb(205, 127, 50)) //Bronze
        }
        if(currencyLevel >= 50){
            backColour.setBackgroundColor(Color.rgb(192, 192, 192)) //Silver
        }
        if(currencyLevel >= 75){
            backColour.setBackgroundColor(Color.rgb(165, 124, 0)) //Gold
        }
    }
    // created branch
}
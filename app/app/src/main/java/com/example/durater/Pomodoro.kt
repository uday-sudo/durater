package com.example.durater

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.preference.PreferenceManager
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

//import kotlinx.android.synthetic.main.activity_main.*

class Pomodoro : AppCompatActivity() {

    private var timer = Timer()
    private val channelId = "pomodoroTimer"
    lateinit var wakeLock: PowerManager.WakeLock
    private var completed = 0

    lateinit var textView_pomodoro : TextView
    lateinit var textView_break : TextView
    lateinit var textView_countdown : TextView
    lateinit var textView_completed : TextView
    lateinit var editText_pomodoro : EditText
    lateinit var editText_break : EditText
    lateinit var button_set : Button
    lateinit var fab_stop : FloatingActionButton
    lateinit var fab_pause : FloatingActionButton
    lateinit var fab_play : FloatingActionButton
    lateinit var supportButton : Button
    var workMediaPlayer: MediaPlayer? = null
    var breakMediaPlayer: MediaPlayer? = null

    //val mediaPlayer = MediaPlayer()
    //val play_audio_id = resources.getIdentifier("play.mp3", "raw", packageName)
    //val work_audio_id = resources.getIdentifier("work.mp3", "raw", packageName)
    // 1. Plays the water sound
    fun wplaySound() {
        if (workMediaPlayer == null) {
            workMediaPlayer = MediaPlayer.create(this, R.raw.work_notify)
            workMediaPlayer!!.isLooping = false
            workMediaPlayer!!.start()
        } else workMediaPlayer!!.start()
    }

    // 2. Pause playback
    fun wpauseSound() {
        if (workMediaPlayer?.isPlaying == true) workMediaPlayer?.pause()
    }

    // 3. Stops playback
    fun wstopSound() {
        if (workMediaPlayer != null) {
            workMediaPlayer!!.stop()
            workMediaPlayer!!.release()
            workMediaPlayer = null
        }
    }

    // 1. Plays the water sound
    fun bplaySound() {
        if (breakMediaPlayer == null) {
            breakMediaPlayer = MediaPlayer.create(this, R.raw.break_notify)
            breakMediaPlayer!!.isLooping = false
            breakMediaPlayer!!.start()
        } else breakMediaPlayer!!.start()
    }

    // 2. Pause playback
    fun bpauseSound() {
        if (breakMediaPlayer?.isPlaying == true) breakMediaPlayer?.pause()
    }

    // 3. Stops playback
    fun bstopSound() {
        if (breakMediaPlayer != null) {
            breakMediaPlayer!!.stop()
            breakMediaPlayer!!.release()
            breakMediaPlayer = null
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun sendNotification() {

        // cancel the last notification
        with(NotificationManagerCompat.from(this)) {
            cancel(completed - 1)
        }

        val notificationIntent = Intent(this, Pomodoro::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification =
            NotificationCompat.Builder(this, channelId)
                .setContentTitle("Session $completed Completed")
                .setContentText("Pomodoro Timer Notification")
                .setSmallIcon(R.drawable.ic_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()


        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(completed, notification)
        }
    }

    private fun makeToast(message: String) {

        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 200)
        toast.show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)
        textView_pomodoro = findViewById(R.id.textView_pomodoro)
        textView_break = findViewById(R.id.textView_break)
        textView_countdown = findViewById(R.id.textView_countdown)
        textView_completed = findViewById(R.id.textView_completed)
        editText_pomodoro = findViewById(R.id.editText_pomodoro)
        editText_break = findViewById(R.id.editText_break)
        button_set = findViewById(R.id.button_set)
        fab_stop = findViewById(R.id.fab_stop)
        fab_pause = findViewById(R.id.fab_pause)
        fab_play = findViewById(R.id.fab_play)
        supportButton = findViewById(R.id.support)

        Log.i("timerapp", "onCreate")

        //setSupportActionBar(toolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        textView_completed.setText("Completed sets: "+completed.toString())

        createNotificationChannel()

        //  wakelock keep the CPU for service to keep counting
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "pomodoroTimer::wakeLock"
        )

        //populate timer text with shared preference data
        val sharedPref = getPreferences(MODE_PRIVATE) ?: return
        val savedWorkTimer = sharedPref.getInt("WORK",0)
        val savedBreakTimer = sharedPref.getInt("BREAK",0)

        editText_pomodoro.setText(savedWorkTimer.toString())
        editText_break.setText(savedBreakTimer.toString())

        Log.i("sharedPref","work $savedWorkTimer")
        Log.i("sharedPref","break $savedBreakTimer")

        // default value
        timer.workTimer = savedWorkTimer
        timer.breakTimer = savedBreakTimer
        timer.loadWorkTimer()
        textView_countdown.text = timer.displayTime()

        //open buy me a coffee page
        supportButton.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.buymeacoffee.com/uday101")
            startActivity(openURL)
        }

        fab_play.setOnClickListener {

            Log.i("timerapp", "clicked timer start")

            // don't start a new timer if already counting
            if (timer.isCounting) {
                Log.i("timerapp", "ignore duplicate starting request")
                makeToast("Already started")
                return@setOnClickListener
            }

            startTimer()

        }

        // pause also destroy the service, save the remaining time,  make a new one to continue later
        fab_pause.setOnClickListener {
            Log.i("timerapp", "clicked timer pause")

            if (timer.isCounting) {
                makeToast("Pause timer")
                destroyTimer()
                timer.isCounting = false
                timer.isPause = true

            } else {
                // do nothing if timer is not running, click pause when timer is stopped has no effect
                makeToast("Already pause")
            }

        }

        // stop means cancel the timer
        fab_stop.setOnClickListener {
            Log.i("timerapp", "clicked timer stop(cancel)")
            makeToast("Cancel timer")

            // if it is running, and you clicked cancel, destroy the service
            if (timer.isCounting) {
                destroyTimer()
            }

            // if it is already pause, service is already destroy, you just update gui
            timer.resetTimer()
            timer.loadWorkTimer()
            textView_countdown.text = timer.displayTime()

        }


        button_set.setOnClickListener {

            Log.i("timerapp", "clicked set button")

            val workTime = editText_pomodoro.text.toString()
            val breakTime = editText_break.text.toString()

            val sharedPref = getPreferences(MODE_PRIVATE)
            //save to shared preferences
            val editor = sharedPref.edit()
            editor.putInt("WORK",workTime.toInt())
            editor.putInt("BREAK",breakTime.toInt())
            editor.commit()



            //Log.i("sharedPref","work ${sharedPref.getString("work_timer","3")}")
            //Log.i("sharedPref","break ${sharedPref.getString("break_timer","2")}")



            timer.workTimer = if (workTime.equals("")) 2 else workTime.toInt()
            timer.breakTimer = if (breakTime.equals("")) 2 else breakTime.toInt()

            Log.i(
                "timerapp",
                "workTimer set to ${timer.workTimer}, breakTimer set to ${timer.breakTimer}"
            )


            // if timer is not running and timer is not paused, display the time to count down
            if (!timer.isCounting and !timer.isPause){


                timer.loadWorkTimer()
                textView_countdown.text = timer.displayTime()
                textView_countdown.setTextColor(resources.getColor(R.color.colorWork))

                makeToast("Current session: Work ${timer.workTimer} min, break ${timer.breakTimer} min")

            }else{
                makeToast("Next session: Work ${timer.workTimer} min, break ${timer.breakTimer} min")
            }

            editText_pomodoro.setText(timer.workTimer.toString())
            editText_break.setText(timer.breakTimer.toString())


        }

    }


    // Receiver receive the background count down info to update the countdown
    private val br: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            handleCountDown(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        Log.i("timerapp", "on resume")
        registerReceiver(br, IntentFilter(ForegroundService.COUNTDOWN_BR))
        //handleCountDown(intent)

    }

    override fun onPause() {
        super.onPause()
        Log.i("timerapp", "on pause")
    }

    override fun onStop() {

        Log.i("timerapp", "on Stop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i("timerapp", "on destroy")
        unregisterReceiver(br)

        destroyTimer()
        super.onDestroy()
    }

    // destroyTimer can be call when
    // 1. Timer counted all the way to 0
    // 2. Pause, or cancel
    // 3. App on destroy()
    // so this function could be call when timer is in any state ( running or not)
    private fun destroyTimer(){

        stopService(Intent(this, ForegroundService::class.java))

        if (wakeLock.isHeld){
            wakeLock.release()
        }

    }


    /**
    override fun onBackPressed() {
        Log.i("timerapp", "on back button")
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
    **/

    private fun startTimer() {

        wakeLock.acquire()


        when (timer.workState) {

            WorkState.Break -> {
                if (!timer.isPause) {
                    timer.loadBreakTimer()
                }
                // Change the color of textview to reflect current workstate
                textView_break.setBackgroundColor(Color.parseColor("#FF0000"))
                textView_pomodoro.setBackgroundColor(Color.parseColor("#00000000"))
                // Play notification sound
                bplaySound()

            }
            WorkState.Work -> {

                //Load the time if it is a new timer
                if (!timer.isCounting and !timer.isPause){
                    timer.loadWorkTimer()
                    Log.i("timerapp", "start a new timer with  ${timer.displayTime()}")
                    makeToast("start a new timer with  ${timer.displayTime()}")

                    // resume the time if it is already counting
                }else{
                    Log.i("timerapp", "resume timer from  ${timer.displayTime()}")
                    makeToast("Resume with  ${timer.displayTime()}")
                }
                textView_break.setBackgroundColor(Color.parseColor("#00000000"))
                textView_pomodoro.setBackgroundColor(Color.parseColor("#FF0000"))
                // Play notification sound
                wplaySound()
            }
        }

        timer.isPause = false
        timer.isCounting = true

        val serviceIntent = Intent(this, ForegroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    // on each tick, update the GUI
    private fun handleCountDown(intent: Intent) {

        // do not react if timer is force stopped
        if (intent.hasExtra("toCount") && !intent.hasExtra("forceStopped")) {

            timer.minusOneSecond()
            textView_countdown.text = timer.displayTime()
            Log.i("timerapp", timer.displayTime())

            // reached 0
            if (!timer.isCounting) {

                destroyTimer()

                // switch state when timer is finish
                when (timer.workState) {
                    WorkState.Work -> {

                        completed++
                        textView_completed.setText("Completed sets: "+ completed.toString())
                        sendNotification()

                        timer.workState = WorkState.Break
                        startTimer()
                    }
                    WorkState.Break -> {

                        timer.workState = WorkState.Work
                        startTimer()
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong("timeLeftInSecond", timer.toSeconds())
        outState.putInt("workState", timer.workState.ordinal)
        outState.putBoolean("isCounting", timer.isCounting)
        outState.putBoolean("isPause", timer.isPause)
        outState.putInt("workTimer", timer.workTimer)
        outState.putInt("breakTimer", timer.breakTimer)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        timer.restoreFromSeconds(savedInstanceState.getLong("timeLeftInSecond"))
        timer.workState = WorkState.getValueFromInt(savedInstanceState.getInt("workState"))
        timer.isPause = savedInstanceState.getBoolean("isPause")
        timer.isCounting = savedInstanceState.getBoolean("isCounting")
        timer.workTimer = savedInstanceState.getInt("workTimer")
        timer.breakTimer = savedInstanceState.getInt("breakTimer")

        Log.i("Restore Instance", "timeLeftInSeconds = ${timer.toSeconds()}")
        textView_countdown.text = timer.displayTime()


        //restart timer if the timer is running
        if (timer.isCounting) {
            startTimer()
        }
    }
}
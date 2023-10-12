package com.example.beenthere

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.beenthere.data.Message
import com.example.beenthere.databinding.ActivityVideoBinding
import com.example.beenthere.utils.APP_ID
import com.example.beenthere.utils.UserManager.userName
import com.example.beenthere.utils.token
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas

class VideoActivity : AppCompatActivity() {

    private var mRtcEngine: RtcEngine? = null
    private var channelName: String? = null
    private var userRole = 0
    private var eventId: String? = ""

    private lateinit var chatViewModel: ChatViewModel

    private lateinit var binding: ActivityVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video)

        channelName = intent.getStringExtra("ChannelName")
        userRole = intent.getIntExtra("UserRole", -1)
        eventId = intent.getStringExtra("EventId")
        Log.i("VideoActivity", eventId.toString())

        initAgoraEngineAndJoinChannel()

        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        chatViewModel.messageList.observe(this) { messages ->
            val adapter = MessageAdapter()
            binding.recyclerChat.adapter = adapter
            Log.i("Message", messages.toString())
            adapter.submitList(messages)
        }

        var narration = ""

        binding.editMessage.doAfterTextChanged {
            narration = binding.editMessage.text.toString()
        }


        chatViewModel.setFireStoreListener()

        binding.btnSend.setOnClickListener {

            chatViewModel.addData(
                userName,
                narration,
                Message.SENT_BY_ME,
                chatViewModel.getCurrentTimestamp()
            )

            binding.editMessage.setText("")

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mRtcEngine!!.leaveChannel()
        RtcEngine.destroy()
        mRtcEngine = null
        chatViewModel.removeFirebaseListener()
    }


    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine()

        mRtcEngine!!.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
        mRtcEngine!!.setClientRole(userRole)

        mRtcEngine!!.enableVideo()
        if (userRole == 1) {
            setupLocalVideo()
        } else {
            val localVideoCanvas = findViewById<View>(R.id.local_video_view_container)
            localVideoCanvas.isVisible = false
        }

        joinChannel()
    }


    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread { setupRemoteVideo(uid) }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread { onRemoteUserLeft() }
        }

        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            runOnUiThread { println("Join channel success : $uid") }
        }

    }

    private fun initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, APP_ID, mRtcEventHandler)
        } catch (e: Exception) {
            Log.i("initializeAgoraEngine", e.message.toString())
        }
    }

    private fun setupLocalVideo() {
        val container = findViewById<View>(R.id.local_video_view_container) as FrameLayout
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        surfaceView.setZOrderMediaOverlay(true)
        container.addView(surfaceView)
        mRtcEngine!!.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
    }

    private fun joinChannel() {
        mRtcEngine!!.joinChannel(token, channelName, null, 0)
    }

    private fun setupRemoteVideo(uid: Int) {
        val container = findViewById<View>(R.id.remote_video_view_container) as FrameLayout

        if (container.childCount >= 1) {
            return
        }

        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        container.addView(surfaceView)
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))
    }

    private fun onRemoteUserLeft() {
        val container = findViewById<View>(R.id.remote_video_view_container) as FrameLayout
        container.removeAllViews()
    }

    fun onLocalAudioMuteClicked(view: View) {
        val iv = view as ImageView
        if (iv.isSelected) {
            iv.isSelected = false
            iv.clearColorFilter()
        } else {
            iv.isSelected = true
            iv.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.MULTIPLY)
        }

        mRtcEngine!!.muteLocalAudioStream(iv.isSelected)
    }

    fun onSwitchCameraClicked(view: View) {
        mRtcEngine!!.switchCamera()
    }

    fun onEndCallClicked(view: View) {
        finish()
        chatViewModel.endLiveTalk(eventId)
        eventId?.let { Log.i("VideoActivity", it) }
    }


}
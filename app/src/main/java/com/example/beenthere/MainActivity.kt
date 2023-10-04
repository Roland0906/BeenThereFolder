package com.example.beenthere

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.beenthere.databinding.ActivityMainBinding
import com.example.beenthere.ext.getVmFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navBottomView: BottomNavigationView = findViewById(R.id.bottom_nav)
        val navController = navHostFragment.navController
        navBottomView.setupWithNavController(navController)

        setUpToolbar()


//        chatViewModel.messageList.observe(this){ messages ->
//            val adapter = MessageAdapter()
//            binding.recyclerChat.adapter = adapter
//            adapter.submitList(messages)
//        }
//
//        var narration = ""
//
//        binding.editMessage.doAfterTextChanged {
//            narration = binding.editMessage.text.toString()
//        }
//
//
//
//        binding.inputUser.doAfterTextChanged {
//            userId = binding.inputUser.text.toString() //
//        }
//
//        chatViewModel.setFireStoreListener()
//
//        binding.btnSend.setOnClickListener {
//
//            chatViewModel.addData(userId, narration, Message.SENT_BY_ME, chatViewModel.getCurrentTimestamp())
////            chatViewModel.addToChat(myId, narration, Message.SENT_BY_ME, chatViewModel.getCurrentTimestamp())
//            binding.editMessage.setText("")
////            chatViewModel.callApi(question)
//        }
//
//
//        requestPermission()


    }


    private fun setUpToolbar() {

        val bottomNav = binding.bottomNav
        val color = Color.parseColor("#ae563f")
        val homeColor = Color.parseColor("#CD6C2C")
        val profileColor = Color.parseColor("#eacda3")
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            when (navController.currentDestination?.id) {



                R.id.homeFragment -> {
                    binding.imageToolbarLogo.visibility = View.VISIBLE
                    viewModel.isHome.value = true
                    binding.textToolbarTitle.text = ""
                    binding.toolbar.setBackgroundResource(R.drawable.main_theme_color)
                    bottomNav.visibility = View.VISIBLE
                    bottomNav.setBackgroundColor(homeColor)
//                    window.statusBarColor = Color.parseColor()
                }

                R.id.shareFragment -> {
                    viewModel.isHome.value = true
                    binding.toolbar.setBackgroundResource(R.drawable.main_theme_color)
                    binding.imageToolbarLogo.visibility = View.GONE
                    binding.textToolbarTitle.visibility = View.VISIBLE
                    binding.textToolbarTitle.text = getText(R.string.hello_share_fragment)
                    bottomNav.visibility = View.VISIBLE
                    bottomNav.setBackgroundColor(homeColor)
                }

                R.id.profileFragment -> {
                    viewModel.isHome.value = true
                    binding.imageToolbarLogo.visibility = View.GONE
                    binding.toolbar.setBackgroundColor(profileColor)
                    binding.textToolbarTitle.visibility = View.GONE
                    bottomNav.visibility = View.VISIBLE
                    bottomNav.setBackgroundColor(color)
                }

                R.id.notAloneFragment -> {
                    viewModel.isHome.value = true
                    binding.imageToolbarLogo.visibility = View.VISIBLE
                    binding.textToolbarTitle.visibility = View.GONE
                    binding.toolbar.setBackgroundResource(R.drawable.gradient_border_2_toolbar)
                    bottomNav.visibility = View.VISIBLE
                    bottomNav.setBackgroundColor(color)

                }

                R.id.beenThereFragment -> {
                    binding.imageToolbarLogo.visibility = View.VISIBLE
                    viewModel.isHome.value = true
                    binding.textToolbarTitle.text = ""
                    binding.toolbar.setBackgroundResource(R.drawable.gradient_border_2_toolbar)
                    bottomNav.visibility = View.VISIBLE
                    bottomNav.setBackgroundColor(color)
                }

                else -> {
                    binding.imageToolbarLogo.visibility = View.GONE
                    viewModel.isHome.value = false
                    binding.textToolbarTitle.text = ""
                    bottomNav.visibility = View.GONE
                }

            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        chatViewModel.clearMessageList()
//        chatViewModel.removeFirebaseListener()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.S)
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.BLUETOOTH_CONNECT), 22)
//    }
//
//    fun onSubmit(view: View) {
//        val channelName = findViewById<View>(R.id.channel) as EditText
//        val userRadioButton = findViewById<View>(R.id.radio_group) as RadioGroup
//
//        val checked = userRadioButton.checkedRadioButtonId
//        val audienceButtonId = findViewById<View>(R.id.radio_audience) as RadioButton
//
//        userRole = if (checked == audienceButtonId.id) {
//            0
//        } else {
//            1
//        }
//
//        val intent = Intent(this, VideoActivity::class.java)
//        intent.putExtra("ChannelName", channelName.text.toString())
//        intent.putExtra("UserRole", userRole)
//        startActivity(intent)
//    }


}
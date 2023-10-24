package com.example.beenthere

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.beenthere.databinding.ActivityMainBinding
import com.example.beenthere.ext.getVmFactory
import com.example.beenthere.share.ShareFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


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


        val liveTalksCount = MutableLiveData<Int>()

        val liveTalkDoc = Firebase.firestore.collection("live_talks")
        liveTalkDoc.get()
            .addOnSuccessListener { documents ->
                liveTalksCount.value = documents.size()
                // Now, itemCount contains the number of documents in the "live_talks" collection
                println("Number of items in 'live_talks': $liveTalksCount")
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }


        liveTalksCount.observe(this) {
            if (it > 0) {
                val blinkAnim = AlphaAnimation(0.0f, 1.0f)
                blinkAnim.duration = 1000
                blinkAnim.startOffset = 500
                blinkAnim.repeatMode = Animation.REVERSE
                blinkAnim.repeatCount = Animation.INFINITE
                binding.liveBadge.startAnimation(blinkAnim)
            }
        }
        binding.liveBadge.setOnClickListener {
            it.clearAnimation()
        }



    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val fragmentManager: FragmentManager = navHostFragment.childFragmentManager
//        val fragment: Fragment? = fragmentManager.getPrimaryNavigationFragment()
//        if (fragment is ShareFragment) {
//            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        }
//    }

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
                    binding.toolbar.setBackgroundResource(R.drawable.gradient_border_2_toolbar)
                    bottomNav.visibility = View.VISIBLE
                    bottomNav.setBackgroundColor(color)
//                    window.statusBarColor = Color.parseColor()
                }

                R.id.shareFragment -> {
                    viewModel.isHome.value = true
                    binding.toolbar.setBackgroundResource(R.drawable.gradient_border_2_toolbar)
                    binding.imageToolbarLogo.visibility = View.GONE
                    binding.textToolbarTitle.visibility = View.VISIBLE
                    binding.textToolbarTitle.text = getText(R.string.hello_share_fragment)
                    bottomNav.visibility = View.VISIBLE
                    bottomNav.setBackgroundColor(color)
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

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    v.isCursorVisible = false
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
                else{
                    v.isCursorVisible = true
                }
            }
        }
        return super.dispatchTouchEvent(event)
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
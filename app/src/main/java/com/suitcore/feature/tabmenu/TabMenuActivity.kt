package com.suitcore.feature.tabmenu

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivityTabMenuBinding
import com.suitcore.databinding.LayoutBottomMenuBinding

/**
 * Created by @dodydmw19 on 10, September, 2020
 */

class TabMenuActivity : BaseActivity<ActivityTabMenuBinding>() {

    private var mPagerAdapter: MainPagerAdapter? = null
    private lateinit var linBottomNavBinding: LayoutBottomMenuBinding

    override fun getViewBinding(): ActivityTabMenuBinding = ActivityTabMenuBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        initIncludeViewBinding()
        setUpPagerListener()
        actionClick()
    }

    private fun initIncludeViewBinding(){
        linBottomNavBinding = binding.linBottomNav
    }

    private fun setUpPagerListener() {
        mPagerAdapter = MainPagerAdapter(supportFragmentManager)
        binding.pager.clipToPadding = false
        binding.pager.offscreenPageLimit = 2

        val gap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics).toInt()

        binding.pager.pageMargin = gap
        binding.pager.adapter = mPagerAdapter
        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                setSelectedNavigation(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        binding.pager.setPageTransformer(false) { view, _ ->
            view.alpha = 0f
            view.visibility = View.VISIBLE

            // Start Animation for a short period of time
            view.animate().alpha(1f).duration = view.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        }

    }

    private fun setSelectedNavigation(position: Int) {
        when (position) {
            0 -> {
                setSelectorColorNav(linBottomNavBinding.imgMenu1, linBottomNavBinding.tvMenu1, R.color.colorPrimary, R.font.res_roboto_medium)
                setSelectorColorNav(linBottomNavBinding.imgMenu2, linBottomNavBinding.tvMenu2, R.color.gray, R.font.res_roboto_regular)
            }
            1 -> {
                setSelectorColorNav(linBottomNavBinding.imgMenu1, linBottomNavBinding.tvMenu1, R.color.gray, R.font.res_roboto_regular)
                setSelectorColorNav(linBottomNavBinding.imgMenu2, linBottomNavBinding.tvMenu2, R.color.colorPrimary, R.font.res_roboto_medium)
            }
        }
    }

    private fun setSelectorColorNav(imgMenu: ImageView, tvMenu: TextView, selectedColorRes: Int, selectedFont: Int) {
        imgMenu.setColorFilter(ContextCompat.getColor(this, selectedColorRes), android.graphics.PorterDuff.Mode.SRC_IN)
        tvMenu.typeface = ResourcesCompat.getFont(this, selectedFont)
        tvMenu.setTextColor(ContextCompat.getColor(this, selectedColorRes))
    }

    private fun actionClick() {
        linBottomNavBinding.relButton1.setOnClickListener {
            binding.pager.currentItem = 0
        }

        linBottomNavBinding.relButton2.setOnClickListener {
            binding.pager.currentItem = 1
        }

    }

}
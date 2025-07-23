package com.yc.ycmvvm.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.ref.WeakReference

open class YcViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    init {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                this@YcViewPageAdapter.viewPage = null
            }
        })
    }


    protected var mediator: TabLayoutMediator? = null
    var fragmentList = mutableListOf<WeakReference<Fragment>>()
    var tabNameList = mutableListOf<String>()
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].get()!!
    }

    override fun getItemId(position: Int): Long {
        return fragmentList[position].get().hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragmentList.any { it.get().hashCode().toLong() == itemId }
    }

    protected var viewPage: ViewPager2? = null
    protected var tabLayout: TabLayout? = null
    fun setSelected(position: Int) {
        viewPage?.setCurrentItem(position, false)
    }

    fun setAttached(viewPage: ViewPager2, tabLayout: TabLayout) {
        this.viewPage = viewPage
        this.tabLayout = tabLayout
        viewPage.adapter = this
    }

    open fun <T : Fragment> setFragmentList(newFragmentList: List<T>, tabNameList: List<String>, setSelected: Int? = null) {
        this.fragmentList.clear()
        this.fragmentList.forEach {
            it.get()?.onDestroy()
        }
        this.fragmentList.addAll(newFragmentList.map { WeakReference(it) })
        this.tabNameList.clear()
        this.tabNameList.addAll(tabNameList)
        this.tabLayout!!.removeAllTabs()
        mediator?.detach() // 解绑旧实例
        mediator = TabLayoutMediator(tabLayout!!, viewPage!!) { tab, position ->
            try {
                if (position < this.tabNameList.size) {
                    tab.text = this.tabNameList[position]
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.also { mediator ->
            mediator.attach() // 重新绑定
        }
        this.tabLayout?.post {
            setSelected?.let {
                setSelected(it)
            }
        }
        notifyDataSetChanged()
    }
}
package com.yc.ycmvvm.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yc.ycmvvm.extension.ycGet

class YcViewPageCustomAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val createCustomView: () -> View) :
    YcViewPageAdapter(fragmentManager, lifecycle) {
    open var tabUpdate: ((customView: View, hasSelect: Boolean, tabName: String?, position: Int) -> Unit)? = null
    override fun <T : Fragment> setFragmentList(newFragmentList: List<T>, tabNameList: List<String>, setSelected: Int?) {
        this.fragmentList.clear()
        this.fragmentList.addAll(newFragmentList)
        this.tabLayout!!.removeAllTabs()
        tabLayout!!.clearOnTabSelectedListeners()
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.also {
                    tabUpdate?.invoke(it, true, tabNameList.ycGet(tab.position), tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.also {
                    tabUpdate?.invoke(it, false, tabNameList.ycGet(tab.position), tab.position)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        TabLayoutMediator(tabLayout!!, viewPage!!) { tab, position ->
            try {
                if (tab.customView == null) {
                    tab.setCustomView(createCustomView.invoke())
                }
                if (position < newFragmentList.size) {
                    tabUpdate?.invoke(tab.customView!!, false, tabNameList.ycGet(position), position)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.also { mediator ->
            mediator.detach() // 解除旧绑定
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
package com.yc.ycmvvm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yc.ycmvvm.extension.ycGet

class YcViewPageCustomAdapter<VB : ViewBinding>(fragmentManager: FragmentManager, lifecycle: Lifecycle, val vb: VB) :
    YcViewPageAdapter(fragmentManager, lifecycle) {
    var tabUpdate: (VB.(hasSelect: Boolean, tabName: String?, position: Int) -> Unit)? = null

    override fun <T : Fragment> setFragmentList(newFragmentList: List<T>, tabNameList: List<String>, setSelected: Int?) {
        this.fragmentList.clear()
        this.fragmentList.addAll(newFragmentList)
        this.tabLayout!!.removeAllTabs()
        tabLayout!!.clearOnTabSelectedListeners()
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    tabUpdate?.invoke(vb, true, tabNameList.ycGet(tab.position), tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    tabUpdate?.invoke(vb, false, tabNameList.ycGet(tab.position), tab.position)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        TabLayoutMediator(tabLayout!!, viewPage!!) { tab, position ->
            try {
                if (tab.customView == null) {
                    tab.setCustomView(vb.root)
                }
                if (position < newFragmentList.size) {
                    tabUpdate?.invoke(vb, false, tabNameList.ycGet(tab.position), tab.position)
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
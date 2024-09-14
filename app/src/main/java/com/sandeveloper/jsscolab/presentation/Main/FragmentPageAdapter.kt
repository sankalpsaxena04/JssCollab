package com.sandeveloper.jsscolab.presentation.Main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sandeveloper.jsscolab.presentation.Main.chat.ChatInbox
import com.sandeveloper.jsscolab.presentation.Main.home.HomeFragment
import com.sandeveloper.jsscolab.presentation.Main.home.More

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return  4
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return HomeFragment()
            1 -> return CreatePost()
            2 -> return ChatInbox()
            else -> return More()
        }
    }
}
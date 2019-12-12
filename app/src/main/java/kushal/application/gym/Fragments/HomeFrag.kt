package kushal.application.gym.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.view.*
import kushal.application.gym.Activities.Scanner
import kushal.application.gym.Adapters.CirclePagerIndicatorDecoration
import kushal.application.gym.Adapters.Home_adapter
import kushal.application.gym.R


class HomeFrag : Fragment() {

    var IS_DOWN = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.desc.animate().translationY(-30f)
        view.desc.visibility = View.GONE

        
        view.drop_down.setOnClickListener {
            if (IS_DOWN) {
                view.desc.animate().alpha(1f).translationY(0f).duration = 400
                view.desc.visibility = View.VISIBLE
                view.drop_down.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up, 0)
            } else {
                view.desc.animate().translationY(-30f).alpha(0f).duration = 400
                view.desc.visibility = View.GONE
                view.drop_down.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down, 0)
            }
            IS_DOWN = !IS_DOWN

        }
        view.fab.setOnClickListener {
            it.animate().rotationBy(360f).duration = 800
            //do work here
            startActivity(Intent(activity, Scanner::class.java))
        }


        val list = arrayOf(
            R.drawable.bg_blue,
            R.drawable.bg_green,
            R.drawable.bg_yellow,
            R.drawable.bg_blue,
            R.drawable.bg_green
        )

        view.home_recView.adapter = Home_adapter(list)
        val llm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.home_recView.layoutManager = llm
        view.home_recView.addItemDecoration(CirclePagerIndicatorDecoration())




        return view
    }


}

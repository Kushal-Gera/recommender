package kushal.application.recommender.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kushal.application.recommender.Fragments.*
import kushal.application.recommender.R

val SHARED_PREF = "shared_pref"
val USER_NAME = "name"
val USER_AGE = "age"

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private var IS_SHORT = false
    private var ON_HOME = true
    private val number = 8588910153

    val fManager by lazy {
        supportFragmentManager
    }

    private val menuItemList by lazy {
        arrayListOf<TextView>(
            home_tv, newRide,
            myRide, policies, notifi
        )
    }

    val auth = FirebaseAuth.getInstance()

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sharedPreferences.getBoolean(IS_THEME_DARK, false))
            setTheme(R.style.MyDarkTheme)
        else
            setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)


        if (!sharedPreferences.getBoolean("is_prev", false)) {
            startActivity(Intent(this, DetailsAct::class.java))
        }

        main_name.text = sharedPreferences.getString(USER_NAME, "User")
        main_age.text = sharedPreferences.getString(USER_AGE, "25") + " yrs"

        drawer.setOnClickListener {
            menu.visibility = View.VISIBLE
            layout
                .animate().translationX(350f)
                .translationY(0f)
                .scaleX(0.6f)
                .scaleY(0.6f)
                .duration = 1000

            window.statusBarColor = getColorFromAttr(R.attr.statusBarColorD)
            container.setBackgroundColor(getColorFromAttr(R.attr.backgroundColorDark))

            drawer.visibility = View.INVISIBLE
            IS_SHORT = true


            val dp = sharedPreferences.getString("dp", "none")
            if (!dp.equals("none"))
                Glide.with(this).load(dp).into(main_photo)

        }
        settings.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
        // color set up for menu -> home
        setAllGray()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            home_tv.compoundDrawableTintList =
                resources.getColorStateList(R.color.white)
        }
        home_tv.setTextColor(resources.getColor(R.color.white))

        setUpMenuItems()
        fManager.beginTransaction().replace(R.id.layout, HomeFrag()).commit()

    }

    private fun setUpMenuItems() {

        home_tv.setOnClickListener {
            header_text.text = getString(R.string.home)
            colorToWhite(it)
            fManager.beginTransaction().replace(R.id.layout, HomeFrag()).commit()
            ON_HOME = true

            onBackPressed()
        }
        newRide.setOnClickListener {
            colorToWhite(it)
            header_text.text = getString(R.string.memberships)
            // further changes specific to membership plans
            fManager.beginTransaction().replace(R.id.layout, NewRide()).commit()
            ON_HOME = false

            onBackPressed()
        }
        myRide.setOnClickListener {
            colorToWhite(it)
            header_text.text = getString(R.string.performance)
            fManager.beginTransaction().replace(R.id.layout, MyRides()).commit()
            ON_HOME = false

            onBackPressed()
        }
        policies.setOnClickListener {
            colorToWhite(it)
            header_text.text = getString(R.string.exercises)
            fManager.beginTransaction().replace(R.id.layout, Policies()).commit()
            ON_HOME = false

            onBackPressed()
        }
        notifi.setOnClickListener {
            colorToWhite(it)
            header_text.text = getString(R.string.diet_guide)
            fManager.beginTransaction().replace(R.id.layout, Notifi()).commit()
            ON_HOME = false

            onBackPressed()
        }

        main_photo.setOnClickListener {
            Toast.makeText(this, "Edit in Settings", Toast.LENGTH_SHORT).show()
        }
        contact.setOnClickListener {
            //            onBackPressed()
            val builder = AlertDialog.Builder(
                this,
                R.style.AlertDialogGreen
            )
            builder.setTitle("Contact Us Here")
                .setMessage("Our support team : \n+91 $number")
                .setPositiveButton("WhatsApp") { dialog: DialogInterface, pos: Int ->
                    //whatsApp
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse("https://wa.me/+91$number")
                    startActivity(i)
                }
                .setNegativeButton("Call") { dialogInterface, pos ->
                    //dont call, just take to dialer
                    val i = Intent(Intent.ACTION_DIAL)
                    i.data = Uri.parse("tel:$number")
                    startActivity(i)
                }
            builder.create().show()
        }
        logout.setOnClickListener {
            //            onBackPressed()
            val builder = AlertDialog.Builder(
                this,
                R.style.AlertDialogCustom
            )
            builder.setTitle("Do you really want to Logout ?")
                .setMessage("Don't worry! All progress will be saved")
                .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                    Toast.makeText(this, "Logging Out...", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    startActivity(Intent(this, LoginAct::class.java))
                    finish()
                }
                .setNegativeButton("No") { dialogInterface, i ->
                    //do nothing
                }
            builder.create().show()
        }

    }

    private fun colorToWhite(view: View) {
        setAllGray()
        val it = view as TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            it.compoundDrawableTintList =
                resources.getColorStateList(R.color.white)
        }
        it.setTextColor(resources.getColor(R.color.white))

    }

    private fun setAllGray() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            menuItemList.forEach {

                it.compoundDrawableTintList =
                    ColorStateList.valueOf(getColorFromAttr(R.attr.textSecondary))

                it.setTextColor(getColorFromAttr(R.attr.textSecondary))
            }
        }

    }

    override fun onBackPressed() {
        if (IS_SHORT) {
            layout.animate().translationX(0f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .duration = 600

            Handler().postDelayed({
                drawer.visibility = View.VISIBLE
                menu.visibility = View.INVISIBLE
                window.statusBarColor = getColorFromAttr(R.attr.statusBarColorL)
                container.setBackgroundColor(getColorFromAttr(R.attr.backgroundColor))
            }, 600)

            IS_SHORT = !IS_SHORT

        } else if (!ON_HOME) {
            fManager.beginTransaction().replace(R.id.layout, HomeFrag()).commit()
            header_text.text = getString(R.string.home)
            colorToWhite(home_tv)
            ON_HOME = !ON_HOME
        } else
            super.onBackPressed()

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        main_name.text = sharedPreferences.getString(USER_NAME, "User")
        main_age.text = sharedPreferences.getString(USER_AGE, "25") + " yrs"
    }

    fun getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }


}

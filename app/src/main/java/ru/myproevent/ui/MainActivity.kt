package ru.myproevent.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.github.terrakok.cicerone.androidx.AppNavigator
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.myproevent.App
import ru.myproevent.R
import ru.myproevent.databinding.ActivityMainBinding
import ru.myproevent.ui.presenters.main.MainPresenter
import ru.myproevent.ui.presenters.main.MainView
import ru.myproevent.ui.presenters.main.Menu


class MainActivity : MvpAppCompatActivity(), MainView {
    // TODO: вынести в Dagger
    private val navigator = AppNavigator(this, R.id.container)

    private val presenter by moxyPresenter {
        MainPresenter()
    }
    private lateinit var view: ActivityMainBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Proevent_NoActionBar)
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater).apply {
            // TODO: отрефакторить
            home.setOnClickListener { presenter.openHome() }
            homeHitArea.setOnClickListener { home.performClick() }
            contacts.setOnClickListener { presenter.openContacts() }
            contactsHitArea.setOnClickListener { contacts.performClick() }
            chat.setOnClickListener { presenter.openChat() }
            chatHitArea.setOnClickListener { chat.performClick() }
            events.setOnClickListener { presenter.openEvents() }
            eventsHitArea.setOnClickListener { events.performClick() }
            settings.setOnClickListener { presenter.openSettings() }
            settingsHitArea.setOnClickListener { settings.performClick() }
        }
        setContentView(view.root)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        App.instance.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        App.instance.navigatorHolder.removeNavigator()
    }

    override fun hideBottomNavigation() {
        if (view.bottomNavigation.visibility == GONE) {
            return
        }
        view.bottomNavigation.visibility = GONE
    }

    override fun showBottomNavigation() {
        if (view.bottomNavigation.visibility == VISIBLE) {
            return
        }
        view.bottomNavigation.visibility = VISIBLE
    }

    override fun selectItem(menu: Menu) {
        presenter.itemSelected(menu)
        with(view) {
            val defaultColorState = ColorStateList(
                arrayOf(intArrayOf()),
                intArrayOf(applicationContext.getColor(R.color.PE_blue_gray_light))
            )
            home.backgroundTintList = defaultColorState
            contacts.backgroundTintList = defaultColorState
            chat.backgroundTintList = defaultColorState
            events.backgroundTintList = defaultColorState
            settings.backgroundTintList = defaultColorState

            when (menu) {
                Menu.HOME -> home.backgroundTintList = ColorStateList(
                    arrayOf(intArrayOf()),
                    intArrayOf(applicationContext.getColor(R.color.PE_peach_04))
                )
                Menu.CONTACTS -> contacts.backgroundTintList = ColorStateList(
                    arrayOf(intArrayOf()),
                    intArrayOf(applicationContext.getColor(R.color.PE_peach_04))
                )
                Menu.CHAT -> chat.backgroundTintList = ColorStateList(
                    arrayOf(intArrayOf()),
                    intArrayOf(applicationContext.getColor(R.color.PE_peach_04))
                )
                Menu.EVENTS -> events.backgroundTintList = ColorStateList(
                    arrayOf(intArrayOf()),
                    intArrayOf(applicationContext.getColor(R.color.PE_peach_04))
                )
                Menu.SETTINGS -> settings.backgroundTintList = ColorStateList(
                    arrayOf(intArrayOf()),
                    intArrayOf(applicationContext.getColor(R.color.PE_peach_04))
                )
            }
        }
        showBottomNavigation()
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BackButtonListener && it.backPressed()) {
                return
            }
        }
        presenter.backClicked()
    }
}

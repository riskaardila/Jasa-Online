package com.example.jasaonline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var berandaFragment: BerandaFragment
    lateinit var jasaFragment: JasaFragment
    lateinit var profileFragment: ProfileFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        openFragment(R.id.nav_beranda)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        openFragment(item.itemId)
        return true
    }
    private fun openFragment(fragment_id: Int){
        when (fragment_id) {
            R.id.nav_beranda -> {
                berandaFragment = BerandaFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, berandaFragment)

                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_jasa_pengguna -> {
                jasaFragment = JasaFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, jasaFragment)

                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_profile -> {
                profileFragment = ProfileFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, profileFragment)

                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Keluar Akun")
                builder.setMessage("Apakah anda yakin keluar dari akun saat ini?")
                builder.setIcon(R.drawable.ic_baseline_exit_to_app_24)
                builder.setPositiveButton("Ya") { dialog, _ ->
                    dialog.dismiss()
                    Snackbar.make(drawer_layout, "Anda klik ya!",
                        Snackbar.LENGTH_LONG).show()
                }
                builder.setNegativeButton("Tidak"){dialog, _ ->
                    dialog.dismiss()
                    Snackbar.make(drawer_layout, "Anda klik tidak!",
                        Snackbar.LENGTH_LONG).show()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}

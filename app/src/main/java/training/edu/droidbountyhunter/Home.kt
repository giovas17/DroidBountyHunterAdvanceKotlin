package training.edu.droidbountyhunter

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import training.edu.data.database.DroidBountyHunterDatabase
import training.edu.data.entities.Fugitivo
import training.edu.fragments.About
import training.edu.viewmodels.ListFragmentViewModel

class Home : AppCompatActivity() {

    private var mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
    private var fragments = arrayOf<Fragment?>()
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById<View>(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            val intent = Intent(this@Home, Agregar::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var intent: Intent
        if (item.itemId == R.id.menu_agregar) {
            intent = Intent(this, Agregar::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    class ListFragment : Fragment() {

        private lateinit var listFragmentViewModel: ListFragmentViewModel

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {

            listFragmentViewModel =
                    ListFragmentViewModel(DroidBountyHunterDatabase.getInstance(this.context))

            val args = this.arguments
            val mode = args!!.getInt("mode")

            // Se hace referencia al Fragment generado por XML en los Layouts y
            // se instancia en una View...
            val view = inflater.inflate(R.layout.fragment_list, container, false)
            val list = view.findViewById<View>(R.id.lista) as ListView

            updateList(list, mode)

            // Se genera el Listener para el detalle de cada elemento...
            list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                @Suppress("UNCHECKED_CAST")
                val fugitivos = list.tag as ArrayList<Fugitivo>
                val fugitivo = fugitivos[position]
                val intent = Intent(context, Detalle::class.java)
                intent.putExtra("title", fugitivo.name)
                intent.putExtra("mode", mode)
                intent.putExtra("id", fugitivo.id)
                // se agrega el parametro para la foto

                startActivityForResult(intent, mode)
            }

            return view
        }

        private fun updateList(list : ListView, mode : Int){

            listFragmentViewModel.getFugitivos(mode).observe(this,
                    Observer { fugitivosList ->

                        if (fugitivosList!!.isNotEmpty()) {
                            val fugitivosNames = arrayOfNulls<String>(fugitivosList.size)
                            for (i in fugitivosList.indices) {
                                fugitivosNames[i] = fugitivosList[i].name
                            }
                            val adapter = ArrayAdapter(this.context,
                                    android.R.layout.simple_list_item_1, fugitivosNames)

                            // Assign adapter to ListView
                            list.adapter = adapter
                            list.tag = fugitivosList
                        }
                    })
        }
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        init {
            fragments = arrayOfNulls(3)
        }

        override fun getItem(position: Int): Fragment? {
            // getItem is called to instantiate the fragment for the given page.
            // Return a ListFragment (defined as a static inner class below).
            if (fragments[position] == null) {
                if (position < 2) {
                    fragments[position] = ListFragment()
                    val arguments = Bundle()
                    arguments.putInt("mode", position)
                    fragments[position]?.arguments = arguments
                } else {
                    fragments[position] = About()
                }
            }
            return fragments[position]
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return getString(R.string.title_fugitivos)
                1 -> return getString(R.string.title_capturados)
                2 -> return getString(R.string.title_acercade)
            }
            return null
        }
    }

}
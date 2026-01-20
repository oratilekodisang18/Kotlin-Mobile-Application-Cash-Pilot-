package vcmsa.projects.prog7313_poe_group_10

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var switchOnOff: SwitchCompat
    private lateinit var tvSwitchYes: TextView
    private lateinit var tvSwitchNo: TextView
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Add expenses
        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "budget-tracker-db"
        ).build()

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvExpenses)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val expenses = db.expenseDao().getAllExpenses()
            recyclerView.adapter = ExpenseAdapter(expenses)
        }

        switchOnOff = view.findViewById(R.id.switchOnOff)
        tvSwitchYes = view.findViewById(R.id.tvSwitchYes)
        tvSwitchNo = view.findViewById(R.id.tvSwitchNo)
        fabAdd = view.findViewById(R.id.fabAdd)

        switchOnOff.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvSwitchYes.setTextColor(Color.BLACK)
                tvSwitchNo.setTextColor(Color.WHITE)
            } else {
                tvSwitchYes.setTextColor(Color.WHITE)//parseColor("#4282DC"))
                tvSwitchNo.setTextColor(Color.BLACK)
            }
        }
        // Add expense button
        fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddExpenseActivity::class.java)
            startActivity(intent)
        }

        val spinner: Spinner = view.findViewById(R.id.monthSpinner)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.months_array,
            R.layout.custom_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedMonth = parent?.getItemAtPosition(position).toString()
                Toast.makeText(requireContext(), "Selected: $selectedMonth", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return view
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

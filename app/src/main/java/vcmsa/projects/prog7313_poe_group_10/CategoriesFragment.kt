package vcmsa.projects.prog7313_poe_group_10

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vcmsa.projects.prog7313_poe_group_10.databinding.FragmentCategoriesBinding
import kotlinx.coroutines.launch

class CategoriesFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private lateinit var categoryAdapter: CategoryAdapter

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
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "budget-tracker-db"
        ).build()

        setupRecyclerView()
        setupSpinner()
        setupFab()

        loadCategories()
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(emptyList())
        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCategories.adapter = categoryAdapter
    }

    private fun setupSpinner() {
        val spinner: Spinner = binding.monthSpinner
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
                loadDataForMonth(selectedMonth)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddCategoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val categories = db.categoryDao().getAllCategories()
            categoryAdapter.updateData(categories)
        }
    }

    private fun loadDataForMonth(month: String) {
        // Future implementation: filter categories based on month, if needed
        loadCategories() // currently loads all
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

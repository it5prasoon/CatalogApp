package com.swipe.catalog.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.swipe.catalog.R
import com.swipe.catalog.adapters.ProductAdapter
import com.swipe.catalog.databinding.FragmentHomeBinding
import com.swipe.catalog.model.Product
import com.swipe.catalog.network.ApiResponse
import com.swipe.catalog.utils.CustomToast
import com.swipe.catalog.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.android.ext.android.get

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productList: List<Product>
    private lateinit var searchView: SearchView
    private lateinit var customToast: CustomToast

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        customToast = CustomToast(requireActivity())

        productAdapter = ProductAdapter(get())
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = productAdapter

        homeViewModel.products.observe(requireActivity()) { response ->
            when (response) {

                is ApiResponse.Success -> {
                    productList = response.data
                    productAdapter.submitList(response.data)
                    hideProgressBar()
                }

                is ApiResponse.Error -> {
                    Log.e(
                        "Logs - Error",
                        "Error while getting response ${response.errorMessage}"
                    )
                    showError()
                    hideProgressBar()
                }

                is ApiResponse.Loading -> showProgressBar()
            }
        }

        homeViewModel.fetchProducts()
        setHasOptionsMenu(true)
        return root
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError() {
        customToast.show("Check your network connection.", Toast.LENGTH_LONG)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterProducts(newText)
                return true
            }
        })
    }

    fun filterProducts(query: String) {
        val filteredList = productList.filter { product ->
            product.productName.contains(query, ignoreCase = true)
        }
        Log.i("Logs - Filtered List", filteredList.toString())
        productAdapter.submitList(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

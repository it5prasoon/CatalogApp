package com.swipe.catalog.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.swipe.catalog.R
import com.swipe.catalog.databinding.FragmentAddProductBinding
import com.swipe.catalog.model.AddProductRequest
import com.swipe.catalog.network.ApiResponse
import com.swipe.catalog.utils.CustomToast
import com.swipe.catalog.viewmodels.AddProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

/**
 * Fragment for adding posting products to the server
 */
class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private lateinit var customToast: CustomToast

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: AddProductViewModel by viewModel()
    private lateinit var imageFile: File

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        customToast = CustomToast(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductTypeSpinner()

        binding.btnChooseImage.setOnClickListener {
            launchGallery()
        }

        binding.btnAddProduct.setOnClickListener {
            validateAndSubmitProduct()
        }

        viewModel.addProductResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResponse.Success -> {
                    // Product added successfully
                    customToast.show("Product added successfully", Toast.LENGTH_SHORT)

                    // Clear EditText fields
                    binding.etProductName.text?.clear()
                    binding.etSellingPrice.text?.clear()
                    binding.etTaxRate.text?.clear()

                    // Navigate back to the home fragment
                    findNavController().popBackStack()
                }

                is ApiResponse.Error -> {
                    // Error occurred while adding the product
                    customToast.show(result.errorMessage, Toast.LENGTH_LONG)
                }

                is ApiResponse.Loading -> {
                    // Show loading progress if needed
                }
            }
        }
    }

    private fun setupProductTypeSpinner() {
        val productTypes = arrayOf("Entertainment", "Anime", "Music", "Sports", "Type A", "Type B", "Type C")

        val adapter = ArrayAdapter(requireContext(), R.layout.spinner, productTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProductType.adapter = adapter
    }

    private fun validateAndSubmitProduct() {
        val productType = binding.spinnerProductType.selectedItem as String
        val productName = binding.etProductName.text.toString().trim()
        val sellingPrice = binding.etSellingPrice.text.toString().trim()
        val taxRate = binding.etTaxRate.text.toString().trim()

        if (productName.isEmpty()) {
            binding.etProductName.error = "Please enter a product name"
            return
        }

        if (sellingPrice.isEmpty()) {
            binding.etSellingPrice.error = "Please enter a valid selling price"
            return
        }

        if (taxRate.isEmpty()) {
            binding.etTaxRate.error = "Please enter a valid tax rate"
            return
        }

        val product = AddProductRequest(
            productName,
            productType,
            sellingPrice,
            taxRate,
            imageFile
        )
        viewModel.addProduct(product)
    }

    // Permission required.
    private val requiredPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

    private fun launchGallery() {

        when {
            ContextCompat.checkSelfPermission(requireContext(), requiredPermission)
                    == PackageManager.PERMISSION_GRANTED
            -> {
                val pickIntent = Intent(Intent.ACTION_PICK)
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                openGalleryLauncher.launch(pickIntent)
            }

            shouldShowRequestPermissionRationale(requiredPermission) -> showRationaleDialog()
            else -> requestPermission.launch(arrayOf(requiredPermission))
        }
    }


    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {

                lifecycleScope.launch {
                    try {
                        imageFile = uriToImageFile(result.data?.data!!)
                        binding.tvSelectedImageName.text = imageFile.name
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            }
        }

    private suspend fun uriToImageFile(uri: Uri): File =
        withContext(Dispatchers.IO) {

            var result: File? = null
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = requireContext().contentResolver.query(uri, filePathColumn, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val filePath = cursor.getString(columnIndex)
                    cursor.close()
                    result = File(filePath)
                }
                cursor.close()
            }

            return@withContext result!!
        }

    private val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { isGranted ->
                if (!isGranted.value) {
                    customToast.show("Storage access denied.", Toast.LENGTH_LONG)
                }
            }
        }

    private fun showRationaleDialog() {

        fun helpDialog() {
            AlertDialog
                .Builder(requireContext())
                .apply {
                    setTitle("Grant Access Manually")
                    setMessage("To grant access manually, Hold app icon > Click \"App info\" > Permissions")
                    setPositiveButton("OK") { helpDialog, _ ->
                        helpDialog.dismiss()
                    }
                    show()
                }
        }

        // Rationale Dialog
        AlertDialog
            .Builder(requireContext())
            .apply {
                setTitle("No Storage Access Granted")
                setMessage("Cannot access gallery due to permission restrictions from system.")
                setPositiveButton("Ask Again") { _, _ ->
                    requestPermission.launch(arrayOf(requiredPermission))
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                setNeutralButton("Help") { _, _ ->
                    // Help dialog
                    helpDialog()
                }
                show()
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

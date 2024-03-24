package jolchu.tolik.blinkitcloneapp.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import jolchu.tolik.blinkitcloneapp.Utils
import jolchu.tolik.blinkitcloneapp.databinding.FragmentOTPBinding
import jolchu.tolik.blinkitcloneapp.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class OTPFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentOTPBinding
    private lateinit var userNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOTPBinding.inflate(layoutInflater)

        getUserNumber()
        customizingEnteringOTP()
        sendOtp()
        onBackButtonClicked()
        return binding.root
    }

    private fun sendOtp() {
        Utils.showDialog(requireContext(), "Sending OTP...")
        viewModel.apply {
            sendOTP(userNumber, requireActivity())

            lifecycleScope.launch {
                otpSent.collect { otpSent ->
                    if (otpSent) {
                        Utils.hideDialog()
                        Utils.showToast(requireContext(), "Otp sent...")
                    }
                }
            }
        }
    }

    private fun onBackButtonClicked() {
        binding.tbOtpFragment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun customizingEnteringOTP() {
        val editText = arrayOf(
            binding.etOtp1,
            binding.etOtp2,
            binding.etOtp3,
            binding.etOtp4,
            binding.etOtp5,
            binding.etOtp6
        )
        for (i in editText.indices) {
            editText[i].addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        if (i < editText.size - 1) {
                            editText[i + 1].requestFocus()
                        }
                    } else if (s?.length == 10) {
                        if (i > 0) {
                            editText[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun getUserNumber() {
        val bundle = arguments
        userNumber = bundle?.getString("number").toString()
        binding.tvUserNumber.text = userNumber
    }

}
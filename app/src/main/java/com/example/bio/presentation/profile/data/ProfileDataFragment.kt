package com.example.bio.presentation.profile.data

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bio.R
import com.example.bio.data.dto.UpdateBillsDto
import com.example.bio.data.dto.findOneOrder.FindOneOrderUserBillDto
import com.example.bio.databinding.FragmentProfileDataBinding
import com.example.bio.domain.entities.UpdateBills
import com.example.bio.domain.entities.findOneOrder.FindOneOrderUserBill
import com.example.bio.presentation.adapter.BillsCardAdapter
import com.example.bio.presentation.adapter.CabinetAdapter
import com.example.bio.presentation.data.PairData
import com.example.data.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class ProfileDataFragment : Fragment(), CreateBilDialogFragment.BillDialog {

    private val viewModel: ProfileDataViewModel by viewModels()
    private val binding: FragmentProfileDataBinding by lazy {
        FragmentProfileDataBinding.inflate(layoutInflater)
    }

    private val sharedPreferences: SharedPreferencesManager by lazy {
        SharedPreferencesManager.getInstance(requireContext())
    }
    private val token by lazy {
        sharedPreferences.getString(SharedPreferencesManager.KEYS.TOKEN)
    }

    private var currentBill = FindOneOrderUserBillDto(-1, "", "", "", -1, -1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCabinetFindMy(token)
        viewModel.getBills(token)

        val adapterCabinet = CabinetAdapter()
        binding.rcCabinet.adapter = adapterCabinet

        val adapterBills = BillsCardAdapter(
            { id -> clickBtnDelete(id) },
            { bills -> clickBtnRefactor(bills) }
        )
        binding.rcBills.adapter = adapterBills

        viewModel.cabinetFindMy.onEach {

            val list = listOf(
                PairData("Номер телефона", it.phone),
                PairData("Имя компании", it.company),
                PairData("Тип", it.type),
                PairData("Сфера деятельности", it.area),
                PairData("Сайт компании", it.site),
                PairData("БИН", it.bin),
                PairData("Адрес", it.address),
                PairData("БИК", it.bik),
                PairData("Банк", it.bank),
                PairData("ИИК", it.iik)
            )

            adapterCabinet.submitList(list)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.bills.onEach {

            adapterBills.submitList(it)

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btnCreatePc.setOnClickListener {
            currentBill = FindOneOrderUserBillDto(-1, "", "", "", -1, -1)

            val dialog = CreateBilDialogFragment.newInstance()
            dialog.show(childFragmentManager, "CreateDialog")
        }

        binding.btnSave.setOnClickListener {
            val res = adapterCabinet.createCabinetObject()

            viewModel.putCabinetUpdate(token, res)
        }

    }

    private fun clickBtnDelete(id: Int) {
        viewModel.deleteBills(token, id)
    }

    private fun clickBtnRefactor(bills: FindOneOrderUserBill) {
        currentBill = bills as FindOneOrderUserBillDto

        val dialog = CreateBilDialogFragment.newInstance()
        dialog.show(childFragmentManager, "CreateDialog")
    }

    override fun getData(): FindOneOrderUserBill {
        return currentBill
    }

    override fun setData(bills: FindOneOrderUserBill) {
        val updateBills = UpdateBillsDto(code = bills.code, bank = bills.bank, kbe = bills.kbe)

        if (bills.id == -1) {
            viewModel.createBills(token, updateBills)
        } else {
            viewModel.updateBills(token, updateBills, bills.id)
        }
    }
}